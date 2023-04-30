package solipingen.sassot.mixin.entity.mob;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
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
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.entity.ai.goal.SpearThrowAttackGoal;
import solipingen.sassot.entity.projectile.spear.CopperSpearEntity;
import solipingen.sassot.entity.projectile.spear.FlintSpearEntity;
import solipingen.sassot.entity.projectile.spear.IronSpearEntity;
import solipingen.sassot.entity.projectile.spear.SpearEntity;
import solipingen.sassot.entity.projectile.spear.StoneSpearEntity;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.sound.ModSoundEvents;


@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity implements CrossbowUser, SpearThrowingMob {
    private final PillagerMeleeAttackGoal meleeAttackGoal = new PillagerMeleeAttackGoal((PillagerEntity)(Object)this);
    private final PillagerSpearThrowAttackGoal spearThrowAttackGoal = new PillagerSpearThrowAttackGoal(this, 1.0, 40 - 5*(this.world.getDifficulty().getId() - 1), 10.0f);
    private final CrossbowAttackGoal<PillagerEntity> crossbowAttackGoal = new CrossbowAttackGoal<PillagerEntity>((PillagerEntity)(Object)this, 1.0, 24.0f);


    protected PillagerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectedInit(EntityType<? extends PillagerEntity> entityType, World world, CallbackInfo cbi) {
        this.updateAttackType();
    }

    @Inject(method = "initialize", at = @At("TAIL"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cbireturn) {
        this.updateAttackType();
        if (spawnReason == SpawnReason.STRUCTURE || this.isPatrolLeader()) {
            if (world.getRandom().nextInt(3) == 0) {
                float meleeEquipFloat = world.getRandom().nextFloat();
                if (meleeEquipFloat >= 0.4f) {
                    float swordEquipFloat = world.getRandom().nextFloat();
                    if (swordEquipFloat > 0.67f - 0.15f*(this.world.getDifficulty().getId() - 1)) {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                    }
                    else {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SWORD));
                    }
                }
                else if (meleeEquipFloat < 0.4f) {
                    float spearEquipFloat = world.getRandom().nextFloat();
                    if (spearEquipFloat > 0.67f - (this.world.getDifficulty().getId() - 1)*0.15f) {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR));
                    }
                    else {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SPEAR));
                    }
                }
            }
            this.updateEnchantments(world.getRandom(), difficulty);
        }
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        if (random.nextInt(3) == 0) {
            float meleeEquipFloat = random.nextFloat();
            if (meleeEquipFloat >= 0.4f) {
                float swordEquipFloat = random.nextFloat();
                if (swordEquipFloat > 0.75f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                }
                else if (swordEquipFloat <= 0.75f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty() && swordEquipFloat > 0.3f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SWORD));
                }
                else {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
            }
            else if (meleeEquipFloat < 0.4f) {
                float spearEquipFloat = random.nextFloat();
                if (spearEquipFloat > 0.75f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR));
                }
                else if (spearEquipFloat <= 0.75f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty() && spearEquipFloat > 0.5f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SPEAR));
                }
                else if (spearEquipFloat <= 0.5f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty() && spearEquipFloat > 0.2f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FLINT_SPEAR));
                }
                else {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
                }
            }
        }
    }

    private void updateAttackType() {
        if (this.world == null || this.world.isClient) {
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
        SpearEntity spearEntity = new StoneSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.STONE_SPEAR));
        int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        float speed = 1.6f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        if (spearStack.isOf(ModItems.FLINT_SPEAR)) {
            spearEntity = new FlintSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.FLINT_SPEAR));
            speed = 1.8f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.COPPER_SPEAR)) {
            spearEntity = new CopperSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.COPPER_SPEAR));
            speed = 2.0f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.IRON_SPEAR)) {
            spearEntity = new IronSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.IRON_SPEAR));
            speed = 2.0f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - spearEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        spearEntity.setVelocity(d, e + g * 0.15, f, speed, 13 - this.world.getDifficulty().getId() * 4);
        this.playSound(ModSoundEvents.PILLAGER_SPEAR_THROW, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntity(spearEntity);
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
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
        if (!this.world.isClient) {
            this.updateAttackType();
        }
    }
   
    
    static class PillagerSpearThrowAttackGoal extends SpearThrowAttackGoal {
        private final PillagerEntity pillager;

        public PillagerSpearThrowAttackGoal(SpearThrowingMob spearThrowingMob, double d, int i, float f) {
            super(spearThrowingMob, d, i, f);
            this.pillager = (PillagerEntity)spearThrowingMob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && this.pillager.getMainHandStack().getItem() instanceof SpearItem;
        }

        @Override
        public void start() {
            super.start();
            this.pillager.setAttacking(true);
            this.pillager.setCurrentHand(Hand.MAIN_HAND);
        }

        @Override
        public void stop() {
            super.stop();
            this.pillager.clearActiveItem();
            this.pillager.setAttacking(false);
        }
    }

    class PillagerMeleeAttackGoal extends MeleeAttackGoal {
        public PillagerMeleeAttackGoal(PillagerEntity pillager) {
            super(pillager, 1.0, false);
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            if (this.mob.getVehicle() instanceof RavagerEntity) {
                float f = this.mob.getVehicle().getWidth() - 0.1f;
                return f * 2.0f * (f * 2.0f) + entity.getWidth();
            }
            return super.getSquaredMaxAttackDistance(entity);
        }
    }

    
}
