package solipingen.sassot.mixin.entity;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.entity.projectile.BlazearmEntity;
import solipingen.sassot.entity.projectile.spear.SpearEntity;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.ModShieldItem;
import solipingen.sassot.mixin.entity.accessors.MobEntityInvoker;
import solipingen.sassot.registry.tag.ModItemTags;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.projectile.TridentEntityInterface;
import solipingen.sassot.village.ModVillagerProfessions;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityInterface {
    @Unique private static final TrackedData<Boolean> IS_SKEWERED = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique private static final TrackedData<String> SKEWERING_ENTITY = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.STRING);
    @Shadow @Final protected static int USING_ITEM_FLAG;
    @Shadow protected ItemStack activeItemStack;
    @Shadow protected int riptideTicks;
    @Unique @Nullable private PersistentProjectileEntity skeweringEntity;
    @Unique private int skeweredTime;
    @Unique private boolean isUsingWhirlwind;
    @Unique private boolean isUsingFlare;
    @Unique private boolean isUsingTridentRiptide;

    @Invoker("applyDamage")
    public abstract void invokeApplyDamage(DamageSource source, float amount);

    @Invoker("setLivingFlag")
    public abstract void invokeSetLivingFlag(int mask, boolean value);


    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectedInitDataTracker(DataTracker.Builder builder, CallbackInfo cbi) {
        builder.add(IS_SKEWERED, false);
        builder.add(SKEWERING_ENTITY, "");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo cbi) {
        nbt.putBoolean("Skewered", this.getIsSkewered());
        if (this.getSkeweringEntity() != null) {
            nbt.putString("Skewering_Entity", this.getSkeweringEntity().getUuidAsString());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.setIsSkewered(nbt.getBoolean("Skewered"));
        List<Entity> entityList = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(1.0),
                entity -> entity.getUuidAsString().matches(nbt.getString("Skewering_Entity")) && entity instanceof PersistentProjectileEntity);
        if (!entityList.isEmpty()) {
            this.setSkeweringEntity((PersistentProjectileEntity)entityList.getFirst());
        }
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), ordinal = 0)
    private Vec3d modifiedMovementInput(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement()) {
            if (!this.canBeSkewered() && this.getIsSkewered()) {
                this.setIsSkewered(false);
            }
            if (this.getIsSkewered()) {
                return new Vec3d(0.0, 0.0, 0.0);
            }
        }
        return movementInput;
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
        if (this.canBeSkewered() && this.getIsSkewered() && this.skeweringEntity != null) {
            if (this.hasVehicle()) {
                this.stopRiding();
            }
            if (this.skeweringEntity instanceof SpearEntity spearEntity) {
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
            else if (this.skeweringEntity instanceof TridentEntity tridentEntity) {
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
            else if (this.skeweringEntity instanceof BlazearmEntity blazearmEntity) {
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
            float unstuckRandomF = this.random.nextFloat();
            float unstuckThresholdD = 1.0f / (1.0f + (float)Math.exp(-(this.age - this.skeweredTime - 200L)));
            if (this.skeweringEntity != null && (unstuckRandomF < unstuckThresholdD || this.skeweringEntity.isRemoved())) {
                if (this.damage(this.skeweringEntity.getDamageSources().trident(this.skeweringEntity, null), 2.0f)) {
                    this.setIsSkewered(false);
                    this.setPos(this.getX(), this.getY() + 1.0, this.getZ());
                    this.onDamaged(this.skeweringEntity.getDamageSources().trident(this.skeweringEntity, null));
                    this.setSkeweringEntity(null);
                }
            }
        }
    }

    @ModifyVariable(method = "setMovementSpeed", at = @At("HEAD"), ordinal = 0)
    private float modifiedMovementSpeed(float movementSpeed) {
        if (this.getIsSkewered()) {
            return 0.0f;
        }
        return movementSpeed;
    }

    @Override
    public void requestTeleport(double destX, double destY, double destZ) {
        super.requestTeleport(destX, destY, destZ);
        if (this.getIsSkewered() || this.getSkeweringEntity() != null) {
            this.setIsSkewered(false);
            this.setSkeweringEntity(null);
        }
    }

    @Inject(method = "teleport", at = @At("TAIL"), cancellable = true)
    private void injectedTeleport(double x, double y, double z, boolean particleEffects, CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.getIsSkewered() || this.getSkeweringEntity() != null) {
            this.setIsSkewered(false);
            this.setSkeweringEntity(null);
        }
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"), cancellable = true)
    private void injectedShieldDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.isInvulnerableTo(source)) {
            cbireturn.setReturnValue(false);
        }
        Vec3d vec3d = source.getPosition();
        Entity entity = source.getSource();
        RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        int echoingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.ECHOING), this.activeItemStack);
        int shockReboundLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.SHOCK_REBOUND), this.activeItemStack);
        int deflectionLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.PROJECTILE_DEFLECTION), this.activeItemStack);
        if (((LivingEntity)(Object)this).isBlocking() && vec3d != null) {
            if (entity != null && entity instanceof LivingEntity livingEntity && shockReboundLevel > 0) {
                float knockbackResistance = (float)livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
                float reboundDamage = 0.2f*shockReboundLevel*amount*knockbackResistance;
                if (knockbackResistance > 0.0f) {
                    livingEntity.damage(this.getDamageSources().thorns(this), reboundDamage);
                }
                livingEntity.takeKnockback(0.04*shockReboundLevel*Math.max(amount - reboundDamage, 0.0f), this.getX() - entity.getX(), this.getZ() - entity.getZ());
                ((LivingEntity)(Object)this).takeKnockback(0.02*Math.max((4 - shockReboundLevel), 0)*amount, entity.getX() - this.getX(), entity.getZ() - this.getZ());
            }
            if (entity != null && entity instanceof ProjectileEntity) {
                if (deflectionLevel > 0) {
                    if (entity instanceof PersistentProjectileEntity) {
                        ((PersistentProjectileEntity)entity).setDamage(((PersistentProjectileEntity)entity).getDamage());
                    }
                    Vec3d initialVelocity = entity.getVelocity();
                    Vec3d vec3d2 = initialVelocity.multiply(Math.pow(1.0 + 0.25*deflectionLevel + this.random.nextDouble()/3.0, deflectionLevel));
                    entity.setVelocity(vec3d2);
                    amount *= 1.0f - 0.15f*deflectionLevel;
                }
                if (((LivingEntity)(Object)this) instanceof PlayerEntity) {
                    float randomf = this.random.nextFloat();
                    float thresholdf = (1.0f/(1.0f + (float)Math.exp(-(amount - 20.0)/(1 + this.getWorld().getDifficulty().getId()))));
                    Item activeShieldItem = this.activeItemStack.getItem();
                    int unyieldingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.UNYIELDING), this.activeItemStack);
                    if (this.activeItemStack.isOf(Items.SHIELD)) {
                        thresholdf -= 0.01f*3.0f + 0.05f*unyieldingLevel;
                        if (randomf < thresholdf) {
                            ((PlayerEntity)(Object)this).getItemCooldownManager().set(activeShieldItem, 60 - 20*unyieldingLevel);
                            if (!this.getWorld().isClient()) {
                                boolean bl = ((LivingEntity)(Object)this).isUsingItem();
                                this.invokeSetLivingFlag(USING_ITEM_FLAG, false);
                                if (bl) {
                                    this.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
                                }
                            }
                            this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
                        }
                    }
                    else if (this.activeItemStack.getItem() instanceof ModShieldItem) {
                        ModShieldItem modShieldItem = (ModShieldItem)this.activeItemStack.getItem();
                        thresholdf -= 0.01f*modShieldItem.getMinDamageToBreak() + modShieldItem.getUnyieldingModifier()*unyieldingLevel;
                        if (randomf < thresholdf) {
                            ((PlayerEntity)(Object)this).getItemCooldownManager().set(activeShieldItem, modShieldItem.getDisabledTicks() - 20*unyieldingLevel);
                            if (!this.getWorld().isClient()) {
                                boolean bl = ((LivingEntity)(Object)this).isUsingItem();
                                this.invokeSetLivingFlag(USING_ITEM_FLAG, false);
                                if (bl) {
                                    this.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
                                }
                            }
                            this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
                        }
                    }
                }
            }
            if (echoingLevel > 0 && (source.isIn(DamageTypeTags.BYPASSES_SHIELD) || source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS)) && !(source.isIn(DamageTypeTags.BYPASSES_RESISTANCE) || source.isIn(DamageTypeTags.BYPASSES_EFFECTS))) {
                float damagef = (float)Math.pow(2.0/3.0, echoingLevel)*amount;
                if (!(source.isOf(DamageTypes.FALLING_ANVIL) || source.isOf(DamageTypes.FALLING_STALACTITE) || source.isOf(DamageTypes.STALAGMITE))) {
                    this.invokeApplyDamage(source, damagef);
                }
                double d1 = Math.pow(0.4, echoingLevel)*(1.0 - ((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                double e1 = Math.pow(0.8, echoingLevel)*(1.0 - ((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                if (entity != null) {
                    Vec3d vec3d2 = new Vec3d(this.getX() - entity.getX(), this.getY() - entity.getY(), this.getZ() - entity.getZ());
                    vec3d2 = vec3d2.normalize().multiply(e1, d1, e1);
                    ((LivingEntity)(Object)this).addVelocity(vec3d2);
                    float distance = this.distanceTo(entity);
                    float distanceModifier = Math.max(MathHelper.square(distance), 1.0f);
                    if (entity != null && entity instanceof LivingEntity) {
                        ((LivingEntity)entity).damage(this.getDamageSources().sonicBoom(entity), (amount - damagef)/distanceModifier);
                        double d2 = Math.pow(0.4, Math.max(4 - echoingLevel, 0))*(amount - damagef)/distanceModifier*(1.0 - ((LivingEntity)entity).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                        double e2 = Math.pow(0.8, Math.max(4 - echoingLevel, 0))*(amount - damagef)/distanceModifier*(1.0 - ((LivingEntity)entity).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                        Vec3d vec3d3 = new Vec3d(entity.getX() - this.getX(), entity.getY() - this.getY(), entity.getZ() - this.getZ());
                        vec3d3 = vec3d3.normalize().multiply(e2, d2, e2);
                        ((LivingEntity)(Object)entity).addVelocity(vec3d3);
                    }
                }
                Box echoingBox = this.getBoundingBox().expand(8.0*echoingLevel);
                List<Entity> otherEntityList = this.getWorld().getOtherEntities(this, echoingBox);
                if (otherEntityList.contains(entity)) {
                    otherEntityList.remove(entity);
                }
                for (Entity otherEntity : otherEntityList) {
                    if (otherEntity instanceof LivingEntity) {
                        float otherDistance = this.distanceTo(otherEntity);
                        float otherDistanceModifier = Math.max(MathHelper.square(otherDistance), 1.0f);
                        otherEntity.damage(this.getDamageSources().sonicBoom(entity), (amount - damagef)/otherDistanceModifier);
                        double d3 = Math.pow(0.4, Math.max(4 - echoingLevel, 0))*(amount - damagef)/otherDistanceModifier*(1.0 - ((LivingEntity)otherEntity).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                        double e3 = Math.pow(0.8, Math.max(4 - echoingLevel, 0))*(amount - damagef)/otherDistanceModifier*(1.0 - ((LivingEntity)otherEntity).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                        Vec3d vec3d4 = new Vec3d(otherEntity.getX() - this.getX(), otherEntity.getY() - this.getY(), otherEntity.getZ() - this.getZ());
                        vec3d4 = vec3d4.normalize().multiply(e3, d3, e3);
                        otherEntity.addVelocity(vec3d4);
                    }
                }
                if (this.getWorld() instanceof ServerWorld serverWorld && (!(source.isOf(DamageTypes.FALLING_ANVIL) || source.isOf(DamageTypes.FALLING_STALACTITE) || source.isOf(DamageTypes.STALAGMITE)))) {
                    serverWorld.spawnParticles(ParticleTypes.SONIC_BOOM, this.getX(), this.getEyePos().y - 0.5, this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                    serverWorld.playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.SHIELD_ECHO, this.getSoundCategory(), 0.5f + 0.1f*echoingLevel, 0.67f + 0.05f*echoingLevel + 0.05f*this.random.nextFloat());
                }
            }
        }
    }

    @Inject(method = "damage", at = @At("TAIL"), cancellable = true)
    private void injectedDamage(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cbireturn) {
        if ((damageSource.getSource() instanceof SpearEntity || damageSource.getSource() instanceof BlazearmEntity)
            && (damageSource.getAttacker() != null && damageSource.getAttacker().getType() == EntityType.WITHER_SKELETON)) {
            ((LivingEntity)(Object)this).addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 160), damageSource.getAttacker());
        }
    }

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    private void modifiedFallFamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cbireturn) {
        if (this.getIsSkewered()) {
            this.fallDistance = 0.0f;
            cbireturn.setReturnValue(0);
        }
    }

    @Redirect(method = "tickRiptide", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;horizontalCollision:Z", opcode = Opcodes.GETFIELD))
    private boolean redirectedHorizontalCollision(LivingEntity livingEntity) {
        return false;
    }

    @Inject(method = "blockedByShield", at = @At("HEAD"), cancellable = true)
    private void injectedBlockedByShield(DamageSource source, CallbackInfoReturnable<Boolean> cbireturn) {
        Entity entity = source.getSource();
        Vec3d vec3d = source.getPosition();
        RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        int shieldingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.SHIELDING), this.activeItemStack);
        int deflectionLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.PROJECTILE_DEFLECTION), this.activeItemStack);
        int echoingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.ECHOING), this.activeItemStack);
        if (((LivingEntity)(Object)this).isBlocking() && vec3d != null) {
            Vec3d vec3d2 = this.getRotationVec(1.0f);
            Vec3d vec3d3 = vec3d.relativize(this.getPos()).normalize();
            vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);
            double shieldingRangeDotProduct = vec3d3.length()*vec3d2.length()*Math.cos((90 - 15*shieldingLevel) * Math.PI/180);
            double vectorDotProduct = vec3d3.dotProduct(vec3d2);
            if ((source.isOf(DamageTypes.FALLING_ANVIL) || source.isOf(DamageTypes.FALLING_STALACTITE) || source.isOf(DamageTypes.STALAGMITE)) && shieldingLevel > 0) {
                cbireturn.setReturnValue(vectorDotProduct < shieldingRangeDotProduct);
            }
            if ((source.isIn(DamageTypeTags.BYPASSES_SHIELD) || source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS)) && !(source.isOf(DamageTypes.FALLING_ANVIL) || source.isOf(DamageTypes.FALLING_STALACTITE) || source.isOf(DamageTypes.STALAGMITE)) && echoingLevel > 0) {
                cbireturn.setReturnValue(vectorDotProduct < shieldingRangeDotProduct);
            }
            if (entity != null && entity instanceof ProjectileEntity) {
                int piercingLevel = 0;
                if (entity instanceof PersistentProjectileEntity) {
                    piercingLevel = Math.max(((PersistentProjectileEntity)entity).getPierceLevel(),
                            EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.PIERCING), ((PersistentProjectileEntity)entity).getItemStack()));
                }
                if (shieldingLevel > piercingLevel || (shieldingLevel == piercingLevel && shieldingLevel == 0)) {
                    cbireturn.setReturnValue(vectorDotProduct < shieldingRangeDotProduct);
                }
                else if (shieldingLevel == piercingLevel && shieldingLevel > 0) {
                    cbireturn.setReturnValue(this.random.nextFloat() < 0.5f + 0.125f*deflectionLevel && vectorDotProduct < shieldingRangeDotProduct);
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
        RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        double modifiedStrength = strength*(1.0 - ((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*(1.0 - 0.2*EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.UNYIELDING), this.activeItemStack));
        if (this.activeItemStack.isOf(ModItems.IRON_SHIELD)) {
            modifiedStrength *= 0.75;
        }
        else if (this.activeItemStack.isOf(ModItems.DIAMOND_SHIELD)) {
            modifiedStrength *= 0.67;
        }
        else if (this.activeItemStack.isOf(ModItems.NETHERITE_SHIELD)) {
            modifiedStrength *= 0.5;
        }
        this.velocityDirty = true;
        Vec3d vec3d = this.getVelocity();
        Vec3d vec3d2 = new Vec3d(x, 0.0, z).normalize().multiply(modifiedStrength);
        this.setVelocity(vec3d.x / 2.0 - vec3d2.x, this.isOnGround() ? Math.min(0.4, vec3d.y / 2.0 + modifiedStrength) : vec3d.y, vec3d.z / 2.0 - vec3d2.z);
        cbi.cancel();
    }

    @Redirect(method = "handleStatus", at = @At(value = "FIELD", target = "Lnet/minecraft/sound/SoundEvents;ITEM_SHIELD_BLOCK:Lnet/minecraft/sound/SoundEvent;", opcode = Opcodes.GETSTATIC))
    private SoundEvent redirectedShieldBlockSound() {
        if (this.activeItemStack.getItem() instanceof ModShieldItem) {
            ModShieldItem modShieldItem = (ModShieldItem)this.activeItemStack.getItem();
            return modShieldItem.getHitSoundEvent();
        }
        else {
            return SoundEvents.ITEM_SHIELD_BLOCK;
        }
    }

    @Inject(method = "onKilledBy", at = @At("TAIL"))
    private void injectedOnKilledBy(@Nullable LivingEntity adversary, CallbackInfo cbi) {
        if (adversary instanceof VillagerEntity && adversary.getWorld() instanceof ServerWorld) {
            VillagerEntity villager = (VillagerEntity)adversary;
            VillagerProfession profession = villager.getVillagerData().getProfession();
            if (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN) {
                int i = villager.getVillagerData().getLevel();
                if (((LivingEntity)(Object)this) instanceof MobEntity && ((MobEntityInvoker)this).invokeGetXpToDrop() >= 1) {
                    villager.setExperience(villager.getExperience() + 2*i*((MobEntityInvoker)this).invokeGetXpToDrop());
                }
                else if (((LivingEntity)(Object)this) instanceof PlayerEntity && ((MobEntityInvoker)this).invokeGetXpToDrop() >= 1) {
                    villager.setExperience(villager.getExperience() + 2*i*((MobEntityInvoker)this).invokeGetXpToDrop());
                }
            }
            villager.reinitializeBrain((ServerWorld)villager.getWorld());
        }
    }

    @Inject(method = "getPreferredEquipmentSlot", at = @At("TAIL"), cancellable = true)
    private void injectedGetPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cbireturn) {
        if (stack.getItem() instanceof ShieldItem) {
            cbireturn.setReturnValue(EquipmentSlot.OFFHAND);
        }
    }

    @Inject(method = "disablesShield", at = @At("HEAD"), cancellable = true)
    private void injectedDisablesShield(CallbackInfoReturnable<Boolean> cbireturn) {
        float randomf = this.random.nextFloat();
        float thresholdf = 1.0f/(1.0f + (float)Math.exp(-(((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) - 20.0)/(1 + this.getWorld().getDifficulty().getId())));
        ItemStack mainHandStack = ((LivingEntity)(Object)this).getMainHandStack();
        float difficultyAdjustment = 0.1f*this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty()*this.getWorld().getDifficulty().getId();
        if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
            cbireturn.setReturnValue(randomf < thresholdf + 0.075f + difficultyAdjustment);
        }
        else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
            cbireturn.setReturnValue(randomf < thresholdf + 0.15f + difficultyAdjustment);
        }
        else if (mainHandStack.isIn(ModItemTags.HACKING_WEAPONS) && !mainHandStack.isOf(ModItems.BLAZEARM)) {
            cbireturn.setReturnValue(randomf < thresholdf + 0.25f + difficultyAdjustment);
        }
        else {
            cbireturn.setReturnValue(randomf < thresholdf + difficultyAdjustment);
        }
    }

    @Inject(method = "addPowderSnowSlowIfNeeded", at = @At("HEAD"), cancellable = true)
    private void injectedAddPowderSnowSlowIfNeeded(CallbackInfo cbi) {
        if (((LivingEntity)(Object)this).isUsingRiptide()) {
            cbi.cancel();
        }
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
    public boolean canBeSkewered() {
        if (((LivingEntity)(Object)this) instanceof PlayerEntity playerEntity) {
            return !(playerEntity.isCreative() || playerEntity.isSpectator());
        }
        return !(((LivingEntity)(Object)this) instanceof EndermanEntity);
    }

    @Override
    @Nullable
    public PersistentProjectileEntity getSkeweringEntity() {
        return this.skeweringEntity;
    }

    @Override
    public void setSkeweringEntity(@Nullable PersistentProjectileEntity entity) {
        this.skeweringEntity = entity;
        this.dataTracker.set(SKEWERING_ENTITY, entity != null ? entity.getUuidAsString() : "");
        if (entity != null) {
            this.skeweredTime = this.age;
        }
        else {
            this.skeweredTime = 0;
        }
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
    public boolean getIsUsingTridentRiptide() {
        return this.isUsingTridentRiptide;
    }

    @Override
    public void setIsUsingTridentRiptide(boolean usingRiptide) {
        this.isUsingTridentRiptide = usingRiptide;
    }

    @Override
    public int getRiptideTicks() {
        return this.riptideTicks;
    }



}
