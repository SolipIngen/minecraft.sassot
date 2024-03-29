package solipingen.sassot.mixin.entity.mob;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.entity.ai.goal.SpearThrowAttackGoal;
import solipingen.sassot.entity.projectile.BlazearmEntity;
import solipingen.sassot.entity.projectile.spear.FlintSpearEntity;
import solipingen.sassot.entity.projectile.spear.StoneSpearEntity;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.mixin.entity.accessors.AbstractSkeletonEntityAccessor;
import solipingen.sassot.sound.ModSoundEvents;


@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonEntityMixin extends AbstractSkeletonEntity implements SpearThrowingMob {
    private final WitherSkeletonSpearThrowAttackGoal spearThrowAttackGoal = new WitherSkeletonSpearThrowAttackGoal(this, 1.0, 40 - 5*(this.getWorld().getDifficulty().getId() - 1), 10.0f);

    
    protected WitherSkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        if (random.nextFloat() < 0.004f*(this.getWorld().getDifficulty().getId() + localDifficulty.getClampedLocalDifficulty())) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BLAZEARM));
        }
        else if (random.nextFloat() >= 0.01f*(this.getWorld().getDifficulty().getId() + localDifficulty.getClampedLocalDifficulty()) && random.nextFloat() < 0.4f + 0.02f*(this.getWorld().getDifficulty().getId() + localDifficulty.getClampedLocalDifficulty())) {
            if (this.random.nextInt(3) == 0) {
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
    private void injectedUpdateEnchantments(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        super.updateEnchantments(random, localDifficulty);
    }

    @Inject(method = "initialize", at = @At("TAIL"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cbireturn) {
        this.updateEnchantments(world.getRandom(), difficulty);
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
        ItemStack spearStack = this.getMainHandStack();
        PersistentProjectileEntity spearEntity = (PersistentProjectileEntity)new StoneSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.STONE_SPEAR));
        int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        float speed = 1.6f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        SoundEvent soundEvent = ModSoundEvents.WITHER_SKELETON_SPEAR_THROW;
        if (spearStack.isOf(ModItems.FLINT_SPEAR)) {
            spearEntity = (PersistentProjectileEntity)new FlintSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.FLINT_SPEAR));
            speed = 1.8f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.BLAZEARM)) {
            spearEntity = (PersistentProjectileEntity)new BlazearmEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.BLAZEARM));
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


    static class WitherSkeletonSpearThrowAttackGoal extends SpearThrowAttackGoal {
        private final WitherSkeletonEntity witherSkeleton;


        public WitherSkeletonSpearThrowAttackGoal(SpearThrowingMob spearThrowingMob, double d, int i, float f) {
            super(spearThrowingMob, d, i, f);
            this.witherSkeleton = (WitherSkeletonEntity)spearThrowingMob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && (this.witherSkeleton.isHolding((stack) -> stack.getItem() instanceof SpearItem) || this.witherSkeleton.isHolding((stack) -> stack.isOf(ModItems.BLAZEARM)));
        }

        @Override
        public void start() {
            super.start();
            this.witherSkeleton.setAttacking(true);
            this.witherSkeleton.setCurrentHand(Hand.MAIN_HAND);
        }

        @Override
        public void stop() {
            super.stop();
            this.witherSkeleton.clearActiveItem();
            this.witherSkeleton.setAttacking(false);
        }
    }


    
}
