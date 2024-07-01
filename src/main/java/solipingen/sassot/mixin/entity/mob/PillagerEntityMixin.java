package solipingen.sassot.mixin.entity.mob;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.entity.ai.goal.PillagerMeleeAttackGoal;
import solipingen.sassot.entity.ai.goal.PillagerSpearThrowAttackGoal;
import solipingen.sassot.entity.projectile.spear.*;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.sound.ModSoundEvents;


@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity implements CrossbowUser, SpearThrowingMob {
    @Unique private final PillagerMeleeAttackGoal meleeAttackGoal = new PillagerMeleeAttackGoal((PillagerEntity)(Object)this);
    @Unique private final PillagerSpearThrowAttackGoal spearThrowAttackGoal = new PillagerSpearThrowAttackGoal(this, 1.0, 40 - 5*(this.getWorld().getDifficulty().getId() - 1), 10.0f);
    @Unique private final CrossbowAttackGoal<PillagerEntity> crossbowAttackGoal = new CrossbowAttackGoal<PillagerEntity>((PillagerEntity)(Object)this, 1.0, 24.0f);


    protected PillagerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectedInit(EntityType<? extends PillagerEntity> entityType, World world, CallbackInfo cbi) {
        this.updateAttackType();
    }

    @Inject(method = "initialize", at = @At("TAIL"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cbireturn) {
        this.updateAttackType();
        if (spawnReason == SpawnReason.STRUCTURE || this.isPatrolLeader()) {
            if (world.getRandom().nextInt(3) == 0) {
                float meleeEquipFloat = world.getRandom().nextFloat();
                if (meleeEquipFloat >= 0.4f) {
                    float swordEquipFloat = world.getRandom().nextFloat();
                    if (swordEquipFloat > 0.67f - 0.15f*(this.getWorld().getDifficulty().getId() - 1)) {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                    }
                    else {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SWORD));
                    }
                }
                else if (meleeEquipFloat < 0.4f) {
                    float spearEquipFloat = world.getRandom().nextFloat();
                    if (spearEquipFloat > 0.67f - (this.getWorld().getDifficulty().getId() - 1)*0.15f) {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR));
                    }
                    else {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SPEAR));
                    }
                }
            }
            this.updateEnchantments(world, world.getRandom(), difficulty);
        }
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        if (random.nextInt(3) == 0) {
            float meleeEquipFloat = random.nextFloat();
            if (meleeEquipFloat >= 0.4f) {
                float swordEquipFloat = random.nextFloat();
                if (swordEquipFloat > 0.75f - 0.05f*(this.getWorld().getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                }
                else if (swordEquipFloat <= 0.75f - 0.05f*(this.getWorld().getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty() && swordEquipFloat > 0.3f - 0.05f*(this.getWorld().getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SWORD));
                }
                else {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
            }
            else if (meleeEquipFloat < 0.4f) {
                float spearEquipFloat = random.nextFloat();
                if (spearEquipFloat > 0.75f - 0.05f*(this.getWorld().getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR));
                }
                else if (spearEquipFloat <= 0.75f - 0.05f*(this.getWorld().getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty() && spearEquipFloat > 0.5f - 0.05f*(this.getWorld().getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SPEAR));
                }
                else if (spearEquipFloat <= 0.5f - 0.05f*(this.getWorld().getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty() && spearEquipFloat > 0.2f - 0.05f*(this.getWorld().getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FLINT_SPEAR));
                }
                else {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
                }
            }
        }
    }

    @Unique
    private void updateAttackType() {
        World world = this.getWorld();
        if (world == null || world.isClient) {
            return;
        }
        this.goalSelector.remove(this.meleeAttackGoal);
        this.goalSelector.remove(this.spearThrowAttackGoal);
        this.goalSelector.remove(this.crossbowAttackGoal);
        ItemStack itemStack = this.getMainHandStack();
        if (itemStack.getItem() instanceof RangedWeaponItem) {
            this.goalSelector.add(3, this.crossbowAttackGoal);
        } 
        else if (itemStack.getItem() instanceof SwordItem) {
            this.goalSelector.add(3, this.meleeAttackGoal);
        }
        else if (itemStack.getItem() instanceof SpearItem) {
            this.goalSelector.add(3, this.spearThrowAttackGoal);
        }
    }

    @Override
    public void spearAttack(LivingEntity target, float pullProgress) {
        ItemStack spearStack = this.getMainHandStack();
        if (spearStack.getItem() instanceof SpearItem spearItem) {
            Vec3d shootPos = new Vec3d(this.getX(), this.getEyeY() - 0.1, this.getZ());
            ProjectileEntity spearEntity = spearItem.createEntity(this.getWorld(), shootPos, spearStack, Direction.getFacing(this.getRotationVecClient()));
            spearEntity.setOwner(this);
            if (spearEntity instanceof PersistentProjectileEntity) {
                ((PersistentProjectileEntity)spearEntity).pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            }
            int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
            int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
            float speed = StoneSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            if (spearStack.isOf(ModItems.FLINT_SPEAR)) {
                speed = FlintSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            }
            else if (spearStack.isOf(ModItems.COPPER_SPEAR)) {
                speed = CopperSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            }
            else if (spearStack.isOf(ModItems.GOLDEN_SPEAR)) {
                speed = GoldenSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            }
            else if (spearStack.isOf(ModItems.IRON_SPEAR)) {
                speed = IronSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            }
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333) - spearEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            spearEntity.setVelocity(d, e + g * 0.15, f, speed, 13 - this.getWorld().getDifficulty().getId() * 4);
            this.playSound(ModSoundEvents.PILLAGER_SPEAR_THROW, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
            this.getWorld().spawnEntity(spearEntity);
        }
    }

    @Inject(method = "shootAt", at = @At("HEAD"), cancellable = true)
    private void injectedAttack(LivingEntity target, float pullProgress, CallbackInfo cbi) {
        if (this.getMainHandStack().getItem() instanceof SwordItem) {
            super.tryAttack(target);
            cbi.cancel();
        }
        else if (this.getMainHandStack().getItem() instanceof SpearItem) {
            this.spearAttack(target, pullProgress);
            cbi.cancel();
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.updateAttackType();
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        super.equipStack(slot, stack);
        World world = this.getWorld();
        if (!world.isClient) {
            this.updateAttackType();
        }
    }
   

    
}
