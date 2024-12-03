package solipingen.sassot.mixin.entity.mob;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.entity.ai.goal.WitherSkeletonSpearThrowAttackGoal;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.mixin.entity.accessors.AbstractSkeletonEntityAccessor;
import solipingen.sassot.sound.ModSoundEvents;


@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonEntityMixin extends AbstractSkeletonEntity implements SpearThrowingMob {
    @Unique private final WitherSkeletonSpearThrowAttackGoal spearThrowAttackGoal = new WitherSkeletonSpearThrowAttackGoal(this, 1.0, 40 - 5*(this.getWorld().getDifficulty().getId() - 1), 10.0f);

    
    protected WitherSkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        if (random.nextFloat() < 0.004f*(localDifficulty.getGlobalDifficulty().getId() + localDifficulty.getClampedLocalDifficulty())) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BLAZEARM));
        }
        else if (random.nextFloat() >= 0.01f*(localDifficulty.getGlobalDifficulty().getId() + localDifficulty.getClampedLocalDifficulty()) && random.nextFloat() < 0.4f + 0.02f*(this.getWorld().getDifficulty().getId() + localDifficulty.getClampedLocalDifficulty())) {
            if (random.nextInt(3) == 0) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FLINT_SPEAR));
            }
            else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
            }
        }
        else {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }
    }

    @Inject(method = "updateEnchantments", at = @At("TAIL"))
    private void injectedUpdateEnchantments(ServerWorldAccess world, Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        super.updateEnchantments(world, random, localDifficulty);
    }

    @Inject(method = "initialize", at = @At("TAIL"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cbireturn) {
        this.updateEnchantments(world, world.getRandom(), difficulty);
    }

    @Override
    public void updateAttackType() {
        World world = this.getWorld();
        if (world == null || world.isClient) {
            return;
        }
        this.goalSelector.remove(((AbstractSkeletonEntityAccessor)this).getMeleeAttackGoal());
        this.goalSelector.remove(this.spearThrowAttackGoal);
        ItemStack itemStack = this.getMainHandStack();
        if (itemStack.getItem() instanceof SwordItem) {
            this.goalSelector.add(3, ((AbstractSkeletonEntityAccessor)this).getMeleeAttackGoal());
        }
        else if (itemStack.getItem() instanceof SpearItem || itemStack.getItem() instanceof BlazearmItem) {
            this.goalSelector.add(3, this.spearThrowAttackGoal);
        }
    }

    @Override
    public void spearAttack(LivingEntity target, float pullProgress) {
        ItemStack spearStack = this.getMainHandStack().copy();
        Vec3d shootPos = new Vec3d(this.getX(), this.getEyeY() - 0.1, this.getZ());
        ProjectileEntity spearEntity;
        if (spearStack.getItem() instanceof SpearItem spearItem) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            if (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.LOYALTY), spearStack) > 0) {
                ItemEnchantmentsComponent.Builder enchantmentsComponentBuilder = new ItemEnchantmentsComponent.Builder(spearStack.getEnchantments());
                enchantmentsComponentBuilder.remove(entry -> entry.matchesKey(Enchantments.LOYALTY));
                spearStack.set(DataComponentTypes.ENCHANTMENTS, enchantmentsComponentBuilder.build());
            }
            spearEntity = spearItem.createEntity(this.getWorld(), shootPos, spearStack, Direction.getFacing(this.getRotationVecClient()));
            spearEntity.setOwner(this);
            if (spearEntity instanceof PersistentProjectileEntity) {
                ((PersistentProjectileEntity)spearEntity).pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            }
            int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
            int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
            float speed = 1.6f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            SoundEvent soundEvent = ModSoundEvents.WITHER_SKELETON_SPEAR_THROW;
            if (spearStack.isOf(ModItems.FLINT_SPEAR)) {
                speed = 1.8f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            }
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333) - spearEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            spearEntity.setVelocity(d, e + g * 0.15, f, speed, 14 - this.getWorld().getDifficulty().getId() * 4);
            this.playSound(soundEvent, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
            this.getWorld().spawnEntity(spearEntity);
        }
        else if (spearStack.getItem() instanceof BlazearmItem blazearmItem) {
            spearEntity = blazearmItem.createEntity(this.getWorld(), shootPos, spearStack, Direction.getFacing(this.getRotationVecClient()));
            if (spearEntity instanceof PersistentProjectileEntity) {
                ((PersistentProjectileEntity)spearEntity).pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            }
            int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
            int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
            float speed = 1.6f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            SoundEvent soundEvent = ModSoundEvents.WITHER_SKELETON_SPEAR_THROW;
            if (spearStack.isOf(ModItems.BLAZEARM)) {
                speed = 2.0f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
                soundEvent = ModSoundEvents.WITHER_SKELETON_BLAZEARM_THROW;
            }
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333) - spearEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            spearEntity.setVelocity(d, e + g * 0.15, f, speed, 14 - this.getWorld().getDifficulty().getId() * 4);
            this.playSound(soundEvent, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
            this.getWorld().spawnEntity(spearEntity);
        }
    }


    
}
