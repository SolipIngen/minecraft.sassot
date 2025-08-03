package solipingen.sassot.mixin.entity;

import java.util.List;
import java.util.Optional;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.entity.projectile.BlazearmEntity;
import solipingen.sassot.entity.projectile.spear.SpearEntity;
import solipingen.sassot.item.ModShieldItem;
import solipingen.sassot.registry.tag.ModDamageTypeTags;
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
    @Shadow protected int itemUseTimeLeft;
    @Shadow protected int riptideTicks;
    @Unique @Nullable private PersistentProjectileEntity skeweringEntity;
    @Unique private int skeweredTime;
    @Unique private boolean isUsingWhirlwind;
    @Unique private boolean isUsingFlare;
    @Unique private boolean isUsingTridentRiptide;

    @Invoker("takeShieldHit")
    public abstract void invokeTakeShieldHit(ServerWorld world, LivingEntity attacker);

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

    @Inject(method = "writeCustomData", at = @At("HEAD"))
    private void injectedWriteCustomDataToNbt(WriteView view, CallbackInfo cbi) {
        view.putBoolean("Skewered", this.getIsSkewered());
        if (this.getSkeweringEntity() != null) {
            view.putString("Skewering_Entity", this.getSkeweringEntity().getUuidAsString());
        }
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(ReadView view, CallbackInfo cbi) {
        this.setIsSkewered(view.getBoolean("Skewered", false));
        List<Entity> entityList = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(1.0),
                entity -> entity.getUuidAsString().matches(view.getString("Skewering_Entity", "")) && entity instanceof PersistentProjectileEntity);
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
        if (this.getWorld() instanceof ServerWorld serverWorld && this.canBeSkewered() && this.getIsSkewered() && this.skeweringEntity != null) {
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
//                if (this.isInsideWall()) {
//                    Vec3d ricochetVelocity = spearVelocity.normalize().negate().multiply(Math.sqrt((double)(this.getWidth()*this.getWidth() + this.getHeight()*this.getHeight())) + 0.01);
//                    this.setPos(spearPos.x + ricochetVelocity.x, spearPos.y + 0.75*(ricochetVelocity.y / Math.abs(ricochetVelocity.y))*this.getHeight(), spearPos.z + ricochetVelocity.z);
//                }
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
//                if (this.isInsideWall()) {
//                    Vec3d ricochetVelocity = tridentVelocity.normalize().negate().multiply(Math.sqrt((double)(this.getWidth()*this.getWidth() + this.getHeight()*this.getHeight())) + 0.01);
//                    this.setPos(tridentPos.x + ricochetVelocity.x, tridentPos.y + 0.75*(ricochetVelocity.y / Math.abs(ricochetVelocity.y))*this.getHeight(), tridentPos.z + ricochetVelocity.z);
//                }
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
//                if (this.isInsideWall()) {
//                    Vec3d ricochetVelocity = blazearmVelocity.normalize().negate().multiply(Math.sqrt((double)(this.getWidth()*this.getWidth() + this.getHeight()*this.getHeight())) + 0.01);
//                    this.setPos(blazearmPos.x + ricochetVelocity.x, blazearmPos.y + 0.75*(ricochetVelocity.y / Math.abs(ricochetVelocity.y))*this.getHeight(), blazearmPos.z + ricochetVelocity.z);
//                }
            }
            float unstuckRandomF = this.random.nextFloat();
            float unstuckThresholdD = 1.0f / (1.0f + (float)Math.exp(-(this.age - this.skeweredTime - 200L)));
            if (this.skeweringEntity != null && (unstuckRandomF < unstuckThresholdD || this.skeweringEntity.isRemoved())) {
                if (this.damage(serverWorld, this.skeweringEntity.getDamageSources().trident(this.skeweringEntity, null), 2.0f)) {
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

    @Inject(method = "getDamageBlockedAmount", at = @At("HEAD"), cancellable = true)
    private void injectedGetDamageBlockedAmount(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Float> cbireturn) {
        float damageBlocked = 0.0f;
        if (amount > 0.0f) {
            ItemStack itemStack = ((LivingEntity)(Object)this).getBlockingItem();
            if (itemStack != null) {
                BlocksAttacksComponent blocksAttacksComponent = itemStack.get(DataComponentTypes.BLOCKS_ATTACKS);
                if (blocksAttacksComponent != null) {
                    java.util.Objects.requireNonNull(source);
                    Vec3d vec3d = source.getPosition();
                    Entity entity = source.getSource();
                    RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
                    int shieldingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.SHIELDING), itemStack);
                    double angle = Math.PI;
                    if (vec3d != null) {
                        Vec3d vec3d2 = this.getRotationVector(0.0F, this.getHeadYaw());
                        Vec3d vec3d3 = vec3d.subtract(this.getPos());
                        vec3d3 = (new Vec3d(vec3d3.x, 0.0, vec3d3.z)).normalize();
                        angle = Math.max(0.0, Math.acos(vec3d3.dotProduct(vec3d2)) - 15.0*shieldingLevel*Math.PI/180.0);
                    }
                    damageBlocked = blocksAttacksComponent.getDamageReductionAmount(source, amount, angle);
                    if (damageBlocked > 0.0f) {
                        Optional<TagKey<DamageType>> bypassOptional = blocksAttacksComponent.bypassedBy();
                        if (!bypassOptional.map(source::isIn).orElse(false) || source.isIn(ModDamageTypeTags.IS_ALSO_BLOCKABLE_BY_SHIELD)) {
                            if (entity instanceof ProjectileEntity) {
                                int deflectionLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.PROJECTILE_DEFLECTION), itemStack);
                                if (entity instanceof PersistentProjectileEntity persistentProjectileEntity && persistentProjectileEntity.getPierceLevel() > 0) {
                                    if (shieldingLevel > persistentProjectileEntity.getPierceLevel() || (shieldingLevel <= 0 && shieldingLevel == persistentProjectileEntity.getPierceLevel())) {
                                        cbireturn.setReturnValue(damageBlocked);
                                    }
                                    else if (shieldingLevel > 0 && shieldingLevel == persistentProjectileEntity.getPierceLevel()) {
                                        if (this.random.nextFloat() < 0.5f + 0.125f*deflectionLevel) {
                                            cbireturn.setReturnValue(damageBlocked);
                                        }
                                    }
                                }
                                Vec3d initialVelocity = entity.getVelocity();
                                Vec3d vec3d2 = initialVelocity.multiply(Math.pow(1.0 + 0.25*deflectionLevel + this.random.nextDouble()/3.0, deflectionLevel));
                                entity.setVelocity(vec3d2);
                                if (((LivingEntity)(Object)this) instanceof PlayerEntity playerEntity) {
                                    float minDamageToBreak = blocksAttacksComponent.itemDamage().threshold();
                                    int unyieldingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.UNYIELDING), itemStack);
                                    float randomf = this.random.nextFloat();
                                    float thresholdf = (1.0f/(1.0f + (float)Math.exp(-((1.0f - 0.15f*deflectionLevel)*amount - 20.0)/(1 + this.getWorld().getDifficulty().getId()))));
                                    float difficultyAdjustment = 0.1f*this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty()*this.getWorld().getDifficulty().getId();
                                    if (itemStack.getItem() instanceof ModShieldItem modShieldItem) {
                                        thresholdf -= 0.01f*minDamageToBreak - difficultyAdjustment + modShieldItem.getUnyieldingModifier()*unyieldingLevel;
                                    }
                                    else if (itemStack.isOf(Items.SHIELD)) {
                                        thresholdf -= 0.01f*minDamageToBreak - difficultyAdjustment + 0.05f*unyieldingLevel;
                                    }
                                    if (randomf < thresholdf) {
                                        blocksAttacksComponent.applyShieldCooldown(world, playerEntity, Math.max(0.0f, (float)Math.log(amount) - 0.5f*unyieldingLevel + (this.random.nextFloat() - 0.5f)), itemStack);
                                    }
                                }
                            }
                            else if (entity instanceof LivingEntity livingEntity) {
                                int shockReboundLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.SHOCK_REBOUND), itemStack);
                                if (shockReboundLevel > 0) {
                                    float knockbackResistance = (float)livingEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE);
                                    float reboundDamage = 0.2f*shockReboundLevel*amount*knockbackResistance;
                                    if (knockbackResistance > 0.0f) {
                                        livingEntity.damage(world, this.getDamageSources().thorns(this), reboundDamage);
                                    }
                                    livingEntity.takeKnockback(0.04*shockReboundLevel * Math.max(amount - reboundDamage, 0.0f), this.getX() - entity.getX(), this.getZ() - entity.getZ());
                                    ((LivingEntity)(Object)this).takeKnockback(0.02*Math.max((4 - shockReboundLevel), 0)*amount, entity.getX() - this.getX(), entity.getZ() - this.getZ());
                                }
                                if (!source.isIn(DamageTypeTags.IS_PROJECTILE)) {
                                    this.invokeTakeShieldHit(world, livingEntity);
                                }
                            }
                        }
                        else {
                            int echoingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.ECHOING), itemStack);
                            if (echoingLevel > 0 && source.isIn(ModDamageTypeTags.ECHOING) && !source.isIn(ModDamageTypeTags.IS_ALSO_BLOCKABLE_BY_SHIELD)) {
                                damageBlocked = (1.0f - (float)Math.pow(2.0/3.0, echoingLevel))*amount;
                                double d1 = Math.pow(0.4, echoingLevel)*(1.0 - ((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
                                double e1 = Math.pow(0.8, echoingLevel)*(1.0 - ((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
                                if (entity != null) {
                                    Vec3d vec3d2 = new Vec3d(this.getX() - entity.getX(), this.getY() - entity.getY(), this.getZ() - entity.getZ());
                                    vec3d2 = vec3d2.normalize().multiply(e1, d1, e1);
                                    ((LivingEntity)(Object)this).addVelocity(vec3d2);
                                    float distance = this.distanceTo(entity);
                                    float distanceModifier = Math.max(MathHelper.square(distance), 1.0f);
                                    if (entity instanceof LivingEntity livingEntity) {
                                        livingEntity.damage(world, this.getDamageSources().sonicBoom(entity), damageBlocked/distanceModifier);
                                        double d2 = Math.pow(0.4, Math.max(4 - echoingLevel, 0))*damageBlocked/distanceModifier*(1.0 - livingEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
                                        double e2 = Math.pow(0.8, Math.max(4 - echoingLevel, 0))*damageBlocked/distanceModifier*(1.0 - livingEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
                                        Vec3d vec3d3 = new Vec3d(entity.getX() - this.getX(), entity.getY() - this.getY(), entity.getZ() - this.getZ());
                                        vec3d3 = vec3d3.normalize().multiply(e2, d2, e2);
                                        entity.addVelocity(vec3d3);
                                    }
                                }
                                Box echoingBox = this.getBoundingBox().expand(8.0*echoingLevel);
                                List<Entity> otherEntityList = this.getWorld().getOtherEntities(this, echoingBox);
                                otherEntityList.remove(entity);
                                for (Entity otherEntity : otherEntityList) {
                                    if (otherEntity instanceof LivingEntity otherLivingEntity) {
                                        float otherDistance = this.distanceTo(otherEntity);
                                        float otherDistanceModifier = Math.max(MathHelper.square(otherDistance), 1.0f);
                                        otherEntity.damage(world, this.getDamageSources().sonicBoom(entity), damageBlocked/otherDistanceModifier);
                                        double d3 = Math.pow(0.4, Math.max(4 - echoingLevel, 0))*damageBlocked/otherDistanceModifier*(1.0 - otherLivingEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
                                        double e3 = Math.pow(0.8, Math.max(4 - echoingLevel, 0))*damageBlocked/otherDistanceModifier*(1.0 - otherLivingEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
                                        Vec3d vec3d4 = new Vec3d(otherEntity.getX() - this.getX(), otherEntity.getY() - this.getY(), otherEntity.getZ() - this.getZ());
                                        vec3d4 = vec3d4.normalize().multiply(e3, d3, e3);
                                        otherEntity.addVelocity(vec3d4);
                                    }
                                }
                                world.spawnParticles(ParticleTypes.SONIC_BOOM, this.getX(), this.getEyePos().y - 0.5, this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                                world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.SHIELD_ECHO, this.getSoundCategory(), 0.5f + 0.1f*echoingLevel, 0.67f + 0.05f*echoingLevel + 0.05f*this.random.nextFloat());
                            }
                            else {
                                damageBlocked = 0.0f;
                            }
                        }
                        blocksAttacksComponent.onShieldHit(this.getWorld(), itemStack, ((LivingEntity)(Object)this), ((LivingEntity)(Object)this).getActiveHand(), damageBlocked);
                    }
                }
            }
        }
        cbireturn.setReturnValue(damageBlocked);
    }

    @Inject(method = "getBlockingItem", at = @At("HEAD"), cancellable = true)
    private void injectedGetBlockingItem(CallbackInfoReturnable<ItemStack> cbireturn) {
        if (!((LivingEntity)(Object)this).isUsingItem()) {
            cbireturn.setReturnValue(null);
        }
        else {
            BlocksAttacksComponent blocksAttacksComponent = this.activeItemStack.get(DataComponentTypes.BLOCKS_ATTACKS);
            if (blocksAttacksComponent != null) {
                int i = this.activeItemStack.getItem().getMaxUseTime(this.activeItemStack, (LivingEntity)(Object)this) - this.itemUseTimeLeft;
                int j = blocksAttacksComponent.getBlockDelayTicks();
                RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
                int unyieldingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.UNYIELDING), this.activeItemStack);
                if (i >= j - Math.round(0.15*unyieldingLevel*j)) {
                    cbireturn.setReturnValue(this.activeItemStack);
                }
                else {
                    cbireturn.setReturnValue(null);
                }
            }
            else {
                cbireturn.setReturnValue(null);
            }
        }
    }

    @Inject(method = "damage", at = @At("TAIL"), cancellable = true)
    private void injectedWitherSkeletonArrowDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbireturn) {
        if ((source.getSource() instanceof SpearEntity || source.getSource() instanceof BlazearmEntity)
            && (source.getAttacker() != null && source.getAttacker().getType() == EntityType.WITHER_SKELETON)) {
            ((LivingEntity)(Object)this).addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 160), source.getAttacker());
        }
    }

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    private void modifiedFallFamage(double fallDistance, float damagePerDistance, CallbackInfoReturnable<Integer> cbireturn) {
        if (this.getIsSkewered()) {
            this.fallDistance = 0.0f;
            cbireturn.setReturnValue(0);
        }
    }

    @Redirect(method = "tickRiptide", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;horizontalCollision:Z", opcode = Opcodes.GETFIELD))
    private boolean redirectedHorizontalCollision(LivingEntity livingEntity) {
        return false;
    }

    @ModifyVariable(method = "takeKnockback", at = @At("HEAD"), ordinal = 0)
    private double modifiedTakeKnockback(double strength) {
        ItemStack itemStack = ((LivingEntity)(Object)this).getBlockingItem();
        if (itemStack != null && itemStack.getItem() instanceof ModShieldItem modShieldItem) {
            if (modShieldItem.getUnyieldingModifier() > 0.05f) {
                strength *= Math.max(1.0 - Math.log(14.0*modShieldItem.getUnyieldingModifier()), 0.0);
            }
            RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            int unyieldingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.UNYIELDING), itemStack);
            strength *= 1.0 - 0.1*unyieldingLevel;
        }
        return strength;
    }

    @Inject(method = "onKilledBy", at = @At("TAIL"))
    private void injectedOnKilledBy(@Nullable LivingEntity adversary, CallbackInfo cbi) {
        if (adversary instanceof VillagerEntity && adversary.getWorld() instanceof ServerWorld serverWorld) {
            VillagerEntity villager = (VillagerEntity)adversary;
            VillagerProfession profession = villager.getVillagerData().profession().value();
            if (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN) {
                int i = villager.getVillagerData().level();
                int xpToDrop = ((LivingEntity)(Object)this).getExperienceToDrop(serverWorld, adversary);
                if (xpToDrop >= 1) {
                    villager.setExperience(villager.getExperience() + 2*i*xpToDrop);
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

    @Inject(method = "getWeaponDisableBlockingForSeconds", at = @At("HEAD"), cancellable = true)
    private void injectedGetWeaponDisableBlockingForSeconds(CallbackInfoReturnable<Float> cbireturn) {
        ItemStack weaponStack = ((LivingEntity)(Object)this).getWeaponStack();
        WeaponComponent weaponComponent = weaponStack.get(DataComponentTypes.WEAPON);
        float thresholdf = 1.0f/(1.0f + (float)Math.exp(-(((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.ATTACK_DAMAGE) - 20.0)/(1 + this.getWorld().getDifficulty().getId())));
        float weaponAdjustment = weaponStack.isIn(ModItemTags.HACKING_WEAPONS) ? 0.25f
                : weaponStack.isIn(ModItemTags.THRUSTING_WEAPONS) ? 0.15f
                : weaponStack.isIn(ModItemTags.SWEEPING_WEAPONS) ? 0.075f : 0.0f;
        if (((LivingEntity)(Object)this) instanceof PlayerEntity playerEntity) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            int level;
            if (weaponStack.isIn(ModItemTags.HACKING_WEAPONS)) {
                RegistryEntry<Enchantment> enchantment = enchantmentLookup.getOrThrow(ModEnchantments.HACKING);
                level = EnchantmentHelper.getLevel(enchantment, weaponStack);
                weaponAdjustment += 1.0f/3.0f*level/enchantment.value().getMaxLevel();
            }
            else if (weaponStack.isIn(ModItemTags.THRUSTING_WEAPONS)) {
                RegistryEntry<Enchantment> enchantment = enchantmentLookup.getOrThrow(ModEnchantments.THRUSTING);
                level = EnchantmentHelper.getLevel(enchantment, weaponStack);
                weaponAdjustment += 0.25f*level/enchantment.value().getMaxLevel();
            }
            else if (weaponStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
                weaponAdjustment += 0.2f*(float)playerEntity.getAttributeValue(EntityAttributes.SWEEPING_DAMAGE_RATIO);
            }
            boolean criticalBl = playerEntity.getAttackCooldownProgress(0.5f) > 0.9f && this.fallDistance > 0.0f && !this.isOnGround() && !playerEntity.isClimbing() && !this.isTouchingWater() && !this.hasVehicle() && !this.isSprinting();
            if (this.isSprinting() || criticalBl) {
                thresholdf += 0.1f;
            }
        }
        else {
            thresholdf = 1.0f/(1.0f + (float)Math.exp(-(((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.ATTACK_DAMAGE) - 20.0)/(1 + this.getWorld().getDifficulty().getId())));
        }
        float randomf = this.random.nextFloat();
        if (randomf < thresholdf + weaponAdjustment) {
            cbireturn.setReturnValue(weaponComponent != null && weaponComponent.disableBlockingForSeconds() > 0.0f ? weaponComponent.disableBlockingForSeconds()
                    : (float)Math.log(((LivingEntity)(Object)this).getAttributeValue(EntityAttributes.ATTACK_DAMAGE)));
        }
        else {
            cbireturn.setReturnValue(0.0f);
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
