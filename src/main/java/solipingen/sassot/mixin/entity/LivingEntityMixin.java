package solipingen.sassot.mixin.entity;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.entity.projectile.BlazearmEntity;
import solipingen.sassot.entity.projectile.spear.SpearEntity;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.ModShieldItem;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.projectile.TridentEntityInterface;
import solipingen.sassot.village.ModVillagerProfessions;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityInterface {
    private static final TrackedData<Boolean> IS_SKEWERED = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Shadow protected ItemStack activeItemStack;
    @Shadow private int lastAttackedTime;
    @Shadow private long lastDamageTime;
    @Shadow @Nullable private DamageSource lastDamageSource;
    @Shadow protected int riptideTicks;
    private boolean isUsingWhirlwind;
    private boolean isUsingFlare;

    @Invoker("playHurtSound")
    public abstract void invokePlayHurtSound(DamageSource source);


    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean getIsSkewered() {
        return this.dataTracker.get(IS_SKEWERED);
    }

    @Override
    public void setIsSkewered(boolean isSkewered) {
        this.dataTracker.set(IS_SKEWERED, isSkewered);
    }

    @Override
    public long getLastDamageTime() {
        return this.lastDamageTime;
    }

    @Override
    public DamageSource getLastDamageSource() {
        return this.lastDamageSource;
    }

    @Override
    public void setLastDamageSource(DamageSource damageSource) {
        this.lastDamageSource = damageSource;
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectedInitDataTracker(CallbackInfo cbi) {
        this.dataTracker.startTracking(IS_SKEWERED, false);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo cbi) {
        nbt.putBoolean("Skewered", this.getIsSkewered());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.setIsSkewered(nbt.getBoolean("Skewered"));
    }

    @Override
    public boolean getIsUsingWhirlwind() {
        return this.isUsingWhirlwind;
    }

    @Override
    public void setIsUsingWhirlwind(boolean usingWhirlwind) {
        this.isUsingWhirlwind = usingWhirlwind;
    }

    @Override
    public boolean getIsUsingFlare() {
        return this.isUsingFlare;
    }

    @Override
    public void setIsUsingFlare(boolean usingFlare) {
        this.isUsingFlare = usingFlare;
    }

    @Override
    public int getRiptideTicks() {
        return this.riptideTicks;
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isTouchingWater()Z"))
    private boolean redirectedIsTouchingWater(LivingEntity livingEntity) {
        return this.isTouchingWater() && !((LivingEntity)(Object)this).hasNoDrag();
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInLava()Z"))
    private boolean redirectedIsInLava(LivingEntity livingEntity) {
        return this.isInLava() && !((LivingEntity)(Object)this).hasNoDrag();
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void injectedTickMovement(CallbackInfo cbi) {
        if (this.lastDamageSource != null && this.lastDamageSource.getAttacker() != null && this.getIsSkewered()) {
            if (this.hasVehicle()) {
                this.stopRiding();
            }
            Entity entity = this.lastDamageSource.getSource();
            if (entity instanceof SpearEntity) {
                SpearEntity spearEntity = (SpearEntity)entity;
                Vec3d spearPos = spearEntity.getPos();
                Vec3d spearVelocity = spearEntity.getVelocity();
                Vec3d nextSpearPos = spearPos.add(spearVelocity);
                if (!spearEntity.isRemoved()) {
                    this.setPos(nextSpearPos.x, nextSpearPos.y - this.getHeight(), nextSpearPos.z);
                    if (spearEntity.getInGroundTime() > 0 && spearEntity.getLoyalty() == 0) {
                        this.setPos(spearPos.x, spearPos.y, spearPos.z);
                    }
                }
                if (this.isInsideWall()) {
                    Vec3d ricochetVelocity = spearVelocity.normalize().negate().multiply(Math.sqrt((double)(this.getWidth()*this.getWidth() + this.getHeight()*this.getHeight())) + 0.01);
                    this.setPos(spearPos.x + ricochetVelocity.x, spearPos.y + 0.75*(ricochetVelocity.y / Math.abs(ricochetVelocity.y))*this.getHeight(), spearPos.z + ricochetVelocity.z);
                }
            }
            else if (entity instanceof TridentEntity) {
                TridentEntity tridentEntity = (TridentEntity)entity;
                Vec3d tridentPos = tridentEntity.getPos();
                Vec3d tridentVelocity = tridentEntity.getVelocity();
                Vec3d nextTridentPos = tridentPos.add(tridentVelocity);
                if (!tridentEntity.isRemoved()) {
                    this.setPos(nextTridentPos.x, nextTridentPos.y - this.getHeight(), nextTridentPos.z);
                    if (((TridentEntityInterface)tridentEntity).getInGroundTime() > 0 && ((TridentEntityInterface)tridentEntity).getLoyalty() == 0) {
                        this.setPos(tridentPos.x, tridentPos.y, tridentPos.z);
                    }
                }
                if (this.isInsideWall()) {
                    Vec3d ricochetVelocity = tridentVelocity.normalize().negate().multiply(Math.sqrt((double)(this.getWidth()*this.getWidth() + this.getHeight()*this.getHeight())) + 0.01);
                    this.setPos(tridentPos.x + ricochetVelocity.x, tridentPos.y + 0.75*(ricochetVelocity.y / Math.abs(ricochetVelocity.y))*this.getHeight(), tridentPos.z + ricochetVelocity.z);
                }
            }
            else if (entity instanceof BlazearmEntity) {
                BlazearmEntity blazearmEntity = (BlazearmEntity)entity;
                Vec3d blazearmPos = blazearmEntity.getPos();
                Vec3d blazearmVelocity = blazearmEntity.getVelocity();
                Vec3d nextBlazearmPos = blazearmPos.add(blazearmVelocity);
                if (!blazearmEntity.isRemoved()) {
                    this.setPos(nextBlazearmPos.x, nextBlazearmPos.y - this.getHeight(), nextBlazearmPos.z);
                    if (blazearmEntity.getInGroundTime() > 0 && blazearmEntity.getLoyalty() == 0) {
                        this.setPos(blazearmPos.x, blazearmPos.y, blazearmPos.z);
                    }
                }
                if (this.isInsideWall()) {
                    Vec3d ricochetVelocity = blazearmVelocity.normalize().negate().multiply(Math.sqrt((double)(this.getWidth()*this.getWidth() + this.getHeight()*this.getHeight())) + 0.01);
                    this.setPos(blazearmPos.x + ricochetVelocity.x, blazearmPos.y + 0.75*(ricochetVelocity.y / Math.abs(ricochetVelocity.y))*this.getHeight(), blazearmPos.z + ricochetVelocity.z);
                }
            }
            
            double unstuckRandomD = this.random.nextDouble();
            double unstuckThresholdD = 1.0 / (1.0 + Math.exp(-((double)(this.age - this.lastAttackedTime) - 200.0)));
            if (unstuckRandomD < unstuckThresholdD && entity != null) {
                this.setIsSkewered(false);
                if (this.damage(this.lastDamageSource, 2.0f)) {
                    this.setPos(this.getX(), this.getY() + 1.0, this.getZ());
                    this.lastDamageSource = DamageSource.trident(entity, null);
                }
            }
            else if (unstuckRandomD >= unstuckThresholdD && entity != null && entity.isRemoved()) {
                this.setIsSkewered(false);
                if (this.damage(this.lastDamageSource, 2.0f)) {
                    this.setPos(this.getX(), this.getY() + 1.0, this.getZ());
                    this.lastDamageSource = DamageSource.trident(entity, null);
                }
            }
        }
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"), cancellable = true)
    private void injectedShieldDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.isInvulnerableTo(source)) {
            return;
        }
        Vec3d vec3d = source.getPosition();
        Entity entity = source.getSource();
        ItemStack activeStack = this.activeItemStack;
        int echoingLevel = EnchantmentHelper.getLevel(ModEnchantments.ECHOING, activeStack);
        int shockReboundLevel = EnchantmentHelper.getLevel(ModEnchantments.SHOCK_REBOUND, activeStack);
        int deflectionLevel = EnchantmentHelper.getLevel(ModEnchantments.PROJECTILE_DEFLECTION, activeStack);
        if (((LivingEntity)(Object)this).isBlocking() && vec3d != null) {
            if (shockReboundLevel > 0 && entity instanceof LivingEntity) {
                ((LivingEntity)entity).damage(DamageSource.thorns(this), 0.1f*shockReboundLevel*amount);
                ((LivingEntity)entity).takeKnockback(0.02*shockReboundLevel*amount, this.getX() - entity.getX(), this.getX() - entity.getX());
            }

            if (deflectionLevel > 0 && entity instanceof ProjectileEntity) {
                Vec3d initialVelocity = entity.getVelocity();
                Vec3d vec3d2 = initialVelocity.multiply(Math.pow(1.0 + 0.25*deflectionLevel + this.random.nextDouble()/3.0, deflectionLevel));
                entity.setVelocity(vec3d2);
            }

            if (echoingLevel > 0 && (source.isMagic() || source.bypassesProtection()) && (source != DamageSource.OUT_OF_WORLD || source != DamageSource.STARVE)) {
                float health = ((LivingEntity)(Object)this).getHealth();
                float damagef = (float)Math.pow(0.5, echoingLevel)*amount;
                int i = EnchantmentHelper.getProtectionAmount(this.getArmorItems(), source);
                if (((LivingEntity)(Object)this).hasStatusEffect(StatusEffects.RESISTANCE)) {
                    damagef *= 1.0f - 0.2f*(1 + ((LivingEntity)(Object)this).getStatusEffect(StatusEffects.RESISTANCE).getAmplifier());
                }
                damagef = Math.max(damagef, 0.0f);
                if (i > 0 && !source.bypassesProtection()) {
                    damagef = DamageUtil.getInflictedDamage(damagef, i);
                }

                if (((LivingEntity)(Object)this).getAbsorptionAmount() > 0.0f) {
                    if (damagef >= ((LivingEntity)(Object)this).getAbsorptionAmount()) {
                        float r = damagef - ((LivingEntity)(Object)this).getAbsorptionAmount();
                        ((LivingEntity)(Object)this).setAbsorptionAmount(0.0f);
                        ((LivingEntity)(Object)this).setHealth(health - r);
                    } else {
                        ((LivingEntity)(Object)this).setAbsorptionAmount(((LivingEntity)(Object)this).getAbsorptionAmount() - damagef);
                    }
                    ((LivingEntity)(Object)this).getDamageTracker().onDamage(source, health, damagef);
                    this.emitGameEvent(GameEvent.ENTITY_DAMAGE);
                    this.invokePlayHurtSound(source);
                }
                else {
                    ((LivingEntity)(Object)this).setHealth(health - damagef);
                    ((LivingEntity)(Object)this).getDamageTracker().onDamage(source, health, damagef);
                    this.emitGameEvent(GameEvent.ENTITY_DAMAGE);
                    this.invokePlayHurtSound(source);
                }

                float distance = this.distanceTo(entity);
                float distanceModifier = (float)Math.max(Math.pow(distance, 2), 1.0);
                if (entity instanceof LivingEntity) {
                    ((LivingEntity)entity).damage(DamageSource.sonicBoom(entity), (amount - damagef)/distanceModifier);
                }

                Box echoingBox = this.getBoundingBox().expand(8.0*echoingLevel);
                List<Entity> otherEntityList = this.world.getOtherEntities(this, echoingBox);
                if (otherEntityList.contains(entity)) {
                    otherEntityList.remove(entity);
                }
                for (Entity otherEntity : otherEntityList) {
                    if (otherEntity instanceof LivingEntity) {
                        float otherDistance = this.distanceTo(otherEntity);
                        float otherDistanceModifier = (float)Math.max(Math.pow(otherDistance, 2), 1.0);
                        ((LivingEntity)otherEntity).damage(DamageSource.sonicBoom(entity), (amount - damagef)/otherDistanceModifier);
                    }
                }

                if (!this.world.isClient) {
                    ((ServerWorld)this.world).spawnParticles(ParticleTypes.SONIC_BOOM, this.getX(), this.getEyePos().y - 0.5, this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                    System.out.println(this.getEyePos().y);
                }
                this.world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.SHIELD_ECHO, this.getSoundCategory(), 0.5f + 0.1f*echoingLevel, 0.7f + 0.15f*echoingLevel + 0.15f*this.random.nextFloat());
            }
        }
    }

    @ModifyVariable(method = "handleFallDamage", at = @At("STORE"), ordinal = 0)
    private int modifiedFallFamage(int originalInt, float fallDistance, float damageMultiplier, DamageSource damageSource) {
        if (this.getIsSkewered()) {
            this.fallDistance = 0.0f;
            return 0;
        }
        return originalInt;
    }

    @Redirect(method = "tickRiptide", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;horizontalCollision:Z", opcode = Opcodes.GETFIELD))
    private boolean redirectedHorizontalCollision(LivingEntity livingEntity) {
        return livingEntity.horizontalCollision && !livingEntity.inPowderSnow;
    }

    @Inject(method = "blockedByShield", at = @At("HEAD"), cancellable = true)
    private void injectedBlockedByShield(DamageSource source, CallbackInfoReturnable<Boolean> cbireturn) {
        Entity entity = source.getSource();
        ItemStack activeStack = this.activeItemStack;
        Vec3d vec3d = source.getPosition();
        int shieldingLevel = EnchantmentHelper.getLevel(ModEnchantments.SHIELDING, activeStack);
        int echoingLevel = EnchantmentHelper.getLevel(ModEnchantments.ECHOING, activeStack);
        if (((LivingEntity)(Object)this).isBlocking() && vec3d != null) {
            Vec3d vec3d2 = this.getRotationVec(1.0f);
            Vec3d vec3d3 = vec3d.relativize(this.getPos()).normalize();
            vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);

            double shieldingRangeDotProduct = vec3d3.length()*vec3d2.length()*Math.cos((90 - 15*shieldingLevel) * Math.PI/180);
            double vectorDotProduct = vec3d3.dotProduct(vec3d2);

            if ((source.isMagic() || source.bypassesProtection()) && echoingLevel > 0) {
                cbireturn.setReturnValue(vectorDotProduct < shieldingRangeDotProduct);
            }
            if (entity instanceof ProjectileEntity) {
                int piercingLevel = 0;
                if (entity instanceof PersistentProjectileEntity) {
                    piercingLevel = ((PersistentProjectileEntity)entity).getPierceLevel();
                }
                if (shieldingLevel > piercingLevel || (shieldingLevel == piercingLevel && shieldingLevel == 0)) {
                    cbireturn.setReturnValue(vectorDotProduct < shieldingRangeDotProduct);
                }
                else if (shieldingLevel == piercingLevel && shieldingLevel > 0) {
                    boolean piercingRandomBl = this.random.nextBoolean();
                    cbireturn.setReturnValue(piercingRandomBl && vectorDotProduct < shieldingRangeDotProduct);
                }
                else {
                    cbireturn.setReturnValue(false);
                }
            }
            else {
                cbireturn.setReturnValue(vectorDotProduct < shieldingRangeDotProduct);
            }
        }
        else {
            cbireturn.setReturnValue(false);
        }
    }

    @Inject(method = "takeKnockback", at = @At("HEAD"), cancellable = true)
    private void injectedTakeKnockback(double strength, double x, double z, CallbackInfo cbi) {
        if ((strength *= 1.0 - ((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) <= 0.0) {
            return;
        }
        double modifiedStrength = strength*(1.0 - ((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*(1.0 - 0.2*EnchantmentHelper.getLevel(ModEnchantments.UNYIELDING, this.activeItemStack));
        if (this.activeItemStack.isOf(ModItems.IRON_SHIELD)) {
            modifiedStrength *= 0.7;
        }
        else if (this.activeItemStack.isOf(ModItems.DIAMOND_SHIELD)) {
            modifiedStrength *= 0.4;
        }
        else if (this.activeItemStack.isOf(ModItems.NETHERITE_SHIELD)) {
            modifiedStrength *= 0.25;
        }
        this.velocityDirty = true;
        Vec3d vec3d = this.getVelocity();
        Vec3d vec3d2 = new Vec3d(x, 0.0, z).normalize().multiply(modifiedStrength);
        this.setVelocity(vec3d.x / 2.0 - vec3d2.x, this.onGround ? Math.min(0.4, vec3d.y / 2.0 + modifiedStrength) : vec3d.y, vec3d.z / 2.0 - vec3d2.z);
        cbi.cancel();
    }

    @Redirect(method = "handleStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
    private void redirectedPlaySound(LivingEntity livingEntity, SoundEvent originalSoundEvent, float originalVolume, float originalPitch) {
        if (originalSoundEvent == SoundEvents.ITEM_SHIELD_BLOCK && this.activeItemStack.getItem() instanceof ModShieldItem) {
            ModShieldItem modShieldItem = (ModShieldItem)this.activeItemStack.getItem();
            this.playSound(modShieldItem.getHitSoundEvent(), 0.8f, 0.8f + this.world.random.nextFloat() * 0.4f);
        }
        else {
            this.playSound(originalSoundEvent, originalVolume, originalPitch);
        }
    }

    @Inject(method = "onKilledBy", at = @At("TAIL"))
    private void injectedOnKilledBy(@Nullable LivingEntity adversary, CallbackInfo cbi) {
        if (adversary instanceof VillagerEntity && adversary.world instanceof ServerWorld) {
            VillagerEntity villager = (VillagerEntity)adversary;
            VillagerProfession profession = villager.getVillagerData().getProfession();
            if (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN) {
                int i = villager.getVillagerData().getLevel();
                if (((LivingEntity)(Object)this) instanceof MobEntity && ((MobEntity)(Object)this).getXpToDrop() >= 1) {
                    villager.setExperience(villager.getExperience() + i*this.random.nextBetween(1, ((MobEntity)(Object)this).getXpToDrop()));
                }
                else if (((LivingEntity)(Object)this) instanceof PlayerEntity && ((PlayerEntity)(Object)this).getXpToDrop() >= 1) {
                    villager.setExperience(villager.getExperience() + i*this.random.nextBetween(1, ((PlayerEntity)(Object)this).getXpToDrop()));
                }
                villager.reinitializeBrain((ServerWorld)adversary.world);
            }
        }
    }

    @Inject(method = "getPreferredEquipmentSlot", at = @At("TAIL"), cancellable = true)
    private static void injectedGetPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cbireturn) {
        if (stack.getItem() instanceof ShieldItem) {
            cbireturn.setReturnValue(EquipmentSlot.OFFHAND);
        }
    }

    @Inject(method = "disablesShield", at = @At("HEAD"), cancellable = true)
    private void injectedDisablesShield(CallbackInfoReturnable<Boolean> cbireturn) {
        ItemStack mainHandStack = ((LivingEntity)(Object)this).getMainHandStack();
        Item mainHandItem = mainHandStack.getItem();
        float randomf = this.world.getRandom().nextFloat();
        float localDifficultyAdjustment = 0.05f*this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
        if (mainHandItem instanceof SwordItem) {
            if (randomf < (0.075f + localDifficultyAdjustment) * this.world.getDifficulty().getId()) {
                cbireturn.setReturnValue(true);
            }
        }
        else if (mainHandStack.getItem() instanceof SpearItem || mainHandItem instanceof TridentItem || mainHandItem instanceof BlazearmItem) {
            if (randomf < (0.15f + localDifficultyAdjustment) * this.world.getDifficulty().getId()) {
                cbireturn.setReturnValue(true);
            }
        }
        else if (mainHandItem instanceof AxeItem) {
            if (randomf < (0.25f + localDifficultyAdjustment) * this.world.getDifficulty().getId()) {
                cbireturn.setReturnValue(true);
            }
        }
        else {
            cbireturn.setReturnValue(false);
        }
    }

    @Inject(method = "addPowderSnowSlowIfNeeded", at = @At("HEAD"), cancellable = true)
    private void injectedAddPowderSnowSlowIfNeeded(CallbackInfo cbi) {
        if (((LivingEntity)(Object)this).isUsingRiptide()) {
            cbi.cancel();
        }
    }



}
