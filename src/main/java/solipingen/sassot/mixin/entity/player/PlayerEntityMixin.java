package solipingen.sassot.mixin.entity.player;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.ModShieldItem;
import solipingen.sassot.registry.tag.ModEntityTypeTags;
import solipingen.sassot.registry.tag.ModItemTags;
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.player.PlayerEntityInterface;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityInterface {
    @Shadow @Final PlayerInventory inventory;
    @Nullable @Unique private ItemStack fishingRodStack;

    @Invoker("getDamageAgainst")
    public abstract float invokeGetDamageAgainst(Entity target, float baseDamage, DamageSource damageSource);


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void injectedRiptideTick(CallbackInfo cbi) {
        if (this.riptideStack != null && !this.riptideStack.isEmpty()) {
            LivingEntityInterface iLivingEntity = (LivingEntityInterface) this;
            if (this.isUsingRiptide()) {
                this.setNoDrag(true);
            }
            if (iLivingEntity.getIsUsingFlare() && this.isWet()) {
                this.setNoDrag(false);
            } else if (iLivingEntity.getIsUsingWhirlwind() && (this.isInsideWaterOrBubbleColumn() || this.isInLava())) {
                this.setNoDrag(false);
            } else if (iLivingEntity.getIsUsingTridentRiptide() && (!this.isWet() || this.isInLava())) {
                this.setNoDrag(false);
            }
            if (this.riptideTicks <= 0) {
                iLivingEntity.setIsUsingWhirlwind(false);
                iLivingEntity.setIsUsingFlare(false);
                iLivingEntity.setIsUsingTridentRiptide(false);
                this.setNoDrag(false);
                this.riptideStack = null;
                this.setLivingFlag(4, false);
            }
            if (this.getWorld() instanceof ServerWorld serverWorld && this.isUsingRiptide()) {
                RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
                int j = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.RIPTIDE), this.riptideStack) > 0 ?
                        EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.RIPTIDE), this.riptideStack) :
                        (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), this.riptideStack) > 0 ?
                                EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), this.riptideStack) :
                                EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.WHIRLWIND), this.riptideStack));
                boolean riptideFlareBl = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.RIPTIDE), this.riptideStack) > 0
                        || EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), this.riptideStack) > 0;
                Box riptideBox = this.getBoundingBox().expand((riptideFlareBl ? 6.0 : 4.0) * j, (riptideFlareBl ? 3.0 : 2.0) * j, (riptideFlareBl ? 6.0 : 4.0) * j);
                List<Entity> entityList = serverWorld.getOtherEntities(this, riptideBox);
                for (Entity otherEntity : entityList) {
                    boolean reachBl = false;
                    if (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.WHIRLWIND), riptideStack) > 0 && this.squaredDistanceTo(otherEntity) > (4.0 * j) * (4.0 * j))
                        continue;
                    if (riptideFlareBl && this.squaredDistanceTo(otherEntity) > (6.0 * j) * (6.0 * j)) continue;
                    for (int i = 0; i < MathHelper.ceil(otherEntity.getHeight() + 1.0f); ++i) {
                        Vec3d reachVec3d = new Vec3d(otherEntity.getX(), otherEntity.getBodyY(1.0 / MathHelper.ceil(otherEntity.getHeight()) * i), otherEntity.getZ());
                        BlockHitResult hitResult = this.getWorld().raycast(new RaycastContext(this.getPos(), reachVec3d, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
                        if (((HitResult) hitResult).getType() != HitResult.Type.MISS) continue;
                        reachBl = true;
                        break;
                    }
                    if (!reachBl) continue;
                    double xDiff = this.getX() - otherEntity.getX();
                    double yDiff = this.getY() - otherEntity.getY();
                    double zDiff = this.getZ() - otherEntity.getZ();
                    double distanceModifier = 1.0 / (1.0 + Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff));
                    BlockState otherEntityMagmaBlockState = Blocks.AIR.getDefaultState();
                    Iterable<BlockPos> otherEntityBlockPosIterable = BlockPos.iterateOutwards(otherEntity.getBlockPos(), 1, 1, 1);
                    for (BlockPos otherEntityBlockPos : otherEntityBlockPosIterable) {
                        if (otherEntityBlockPos.getManhattanDistance(otherEntity.getBlockPos()) > 1) continue;
                        otherEntityMagmaBlockState = this.getWorld().getBlockState(otherEntityBlockPos);
                        if (otherEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK)) break;
                    }
                    if (otherEntity instanceof LivingEntity livingOtherEntity) {
                        float damageAmount = EnchantmentHelper.getDamage(serverWorld, this.riptideStack, otherEntity, this.getDamageSources().playerAttack((PlayerEntity) (Object) this), (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                        if (this.riptideStack.isOf(Items.TRIDENT) && !livingOtherEntity.getType().isIn(EntityTypeTags.SENSITIVE_TO_IMPALING) && livingOtherEntity.isWet()) {
                            damageAmount += 1.0f * EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.IMPALING), this.riptideStack);
                        } else if (this.riptideStack.isOf(ModItems.BLAZEARM)) {
                            BlockState targetEntityMagmaBlockState = Blocks.AIR.getDefaultState();
                            Iterable<BlockPos> targetEntityBlockPosIterable = BlockPos.iterateOutwards(livingOtherEntity.getBlockPos(), 1, 1, 1);
                            for (BlockPos targetEntityBlockPos : targetEntityBlockPosIterable) {
                                if (targetEntityBlockPos.getManhattanDistance(livingOtherEntity.getBlockPos()) > 1)
                                    continue;
                                targetEntityMagmaBlockState = this.getWorld().getBlockState(targetEntityBlockPos);
                                if (targetEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK)) break;
                            }
                            if (!livingOtherEntity.getType().isIn(ModEntityTypeTags.SENSITIVE_TO_LEANING) && (livingOtherEntity.isOnFire() || livingOtherEntity.isInLava() || targetEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK) || livingOtherEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE))) {
                                damageAmount += 1.0f * EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.LEANING), this.riptideStack);
                            }
                        }
                        damageAmount *= (riptideFlareBl ? 0.5f : 0.25f) * j * (float) distanceModifier;
                        damageAmount *= (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.WHIRLWIND), this.riptideStack) > 0 && (livingOtherEntity.getType().isIn(ModEntityTypeTags.SENSITIVE_TO_WHIRLWIND) || ((EntityInterface) livingOtherEntity).isBeingSnowedOn())) ? 2.0f : 1.0f;
                        damageAmount *= (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.RIPTIDE), this.riptideStack) > 0 && (livingOtherEntity.isWet() || livingOtherEntity.getType().isIn(EntityTypeTags.SENSITIVE_TO_IMPALING) || livingOtherEntity.hurtByWater())) ? 2.0f : 1.0f;
                        damageAmount *= (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), this.riptideStack) > 0 && (livingOtherEntity.getType().isIn(ModEntityTypeTags.SENSITIVE_TO_LEANING) || livingOtherEntity.isOnFire() || livingOtherEntity.isInLava() || otherEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK) || livingOtherEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE))) ? 2.0f : 1.0f;
                        damageAmount *= this.getWorld().isThundering() ? 1.5f : 1.0f;
                        if (!(livingOtherEntity.isSubmergedInWater() || livingOtherEntity.isSubmergedIn(FluidTags.LAVA)) && EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.WHIRLWIND), this.riptideStack) > 0 && !livingOtherEntity.getType().isIn(ModEntityTypeTags.IMMUNE_TO_WHIRLWIND)) {
                            livingOtherEntity.damage(this.getDamageSources().playerAttack((PlayerEntity) (Object) this), damageAmount);
                            if (((EntityInterface) livingOtherEntity).isBeingSnowedOn()) {
                                livingOtherEntity.setFrozenTicks(livingOtherEntity.getMinFreezeDamageTicks() * (j + 1));
                            }
                            if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) * 1.5 * j * distanceModifier <= 0.0)) {
                                Vec3d vec3d = livingOtherEntity.getVelocity();
                                Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) * 1.5 * j * distanceModifier);
                                otherEntity.setVelocity(vec3d.x - vec3d2.x, livingOtherEntity.isOnGround() ? Math.min(vec3d.y + (1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) * 1.5 * j * distanceModifier, 0.4) : vec3d.y, vec3d.z - vec3d2.z);
                            }
                        } else if (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.RIPTIDE), this.riptideStack) > 0) {
                            livingOtherEntity.damage(this.getDamageSources().playerAttack((PlayerEntity) (Object) this), damageAmount);
                            if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) * 2.0 * j * distanceModifier <= 0.0)) {
                                Vec3d vec3d = livingOtherEntity.getVelocity();
                                Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) * 2.0 * j * distanceModifier);
                                otherEntity.setVelocity(vec3d.x - vec3d2.x, vec3d.y, vec3d.z - vec3d2.z);
                            }
                        } else if (!livingOtherEntity.isWet() && EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), this.riptideStack) > 0 && !livingOtherEntity.getType().isIn(ModEntityTypeTags.IMMUNE_TO_FLARE)) {
                            livingOtherEntity.damage(this.getDamageSources().playerAttack((PlayerEntity) (Object) this), damageAmount);
                            if (!livingOtherEntity.isOnFire()) {
                                livingOtherEntity.setOnFireFor((int) Math.ceil(3.0 * j * distanceModifier));
                            }
                            if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) * 2.0 * j * distanceModifier <= 0.0)) {
                                Vec3d vec3d = livingOtherEntity.getVelocity();
                                Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) * 2.0 * j * distanceModifier);
                                otherEntity.setVelocity(vec3d.x - vec3d2.x, vec3d.y, vec3d.z - vec3d2.z);
                            }
                        }
                    } else {
                        if (!(otherEntity.isSubmergedInWater() || otherEntity.isSubmergedIn(FluidTags.LAVA)) && EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.WHIRLWIND), this.riptideStack) > 0) {
                            Vec3d vec3d = otherEntity.getVelocity();
                            Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply(1.0 - 1.5 * j * distanceModifier);
                            otherEntity.setVelocity(vec3d.x - vec3d2.x, otherEntity.isOnGround() ? Math.min(vec3d.y + (1.0 - 1.5 * j * distanceModifier), 0.4) : vec3d.y, vec3d.z - vec3d2.z);
                        } else if (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.RIPTIDE), this.riptideStack) > 0) {
                            Vec3d vec3d = otherEntity.getVelocity();
                            Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply(1.0 - 2.0 * j * distanceModifier);
                            otherEntity.setVelocity(vec3d.x - vec3d2.x, vec3d.y, vec3d.z - vec3d2.z);
                        } else if ((otherEntity.isFireImmune() || otherEntity.isOnFire() || otherEntity.isInLava() || otherEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK)) && EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), this.riptideStack) > 0) {
                            Vec3d vec3d = otherEntity.getVelocity();
                            Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply(1.0 - 2.0 * j * distanceModifier);
                            otherEntity.setVelocity(vec3d.x - vec3d2.x, vec3d.y, vec3d.z - vec3d2.z);
                        }
                    }
                }
                Iterable<BlockPos> blockPosList = BlockPos.iterateOutwards(this.getBlockPos(), MathHelper.ceil(this.getWidth()), MathHelper.ceil(this.getHeight()), MathHelper.ceil(this.getWidth()));
                for (BlockPos currentBlockPos : blockPosList) {
                    if (this.getWorld().getBlockState(currentBlockPos).isOf(Blocks.POWDER_SNOW)) {
                        this.getWorld().breakBlock(currentBlockPos, true);
                    }
                }
            }
        }
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), ordinal = 0)
    private Vec3d modifiedMovementInput(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement()) {
            if (!((LivingEntityInterface)this).canBeSkewered() && ((LivingEntityInterface)this).getIsSkewered()) {
                ((LivingEntityInterface)this).setIsSkewered(false);
            }
            if (((LivingEntityInterface)this).getIsSkewered()) {
                return new Vec3d(0.0, 0.0, 0.0);
            }
        }
        return movementInput;
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At("HEAD"), cancellable = true)
    private void modifiedGetBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cbireturn) {
        float f = this.inventory.getBlockBreakingSpeed(block);
        if (f > 1.0f) {
            int i = (int)Math.sqrt(this.getAttributeValue(EntityAttributes.PLAYER_MINING_EFFICIENCY) - 1.0);
            if (i > 0) {
                f *= (float)Math.log(Math.E - 1.0 + Math.pow(f, i/Math.PI));
            }
        }
        if (StatusEffectUtil.hasHaste(this)) {
            f *= 1.0f + 0.5f*(StatusEffectUtil.getHasteAmplifier(this) + 1);
        }
        if (this.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            f *= (switch (this.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
                case 0 -> 0.3f;
                case 1 -> 0.09f;
                case 2 -> 0.0027f;
                default -> 8.1e-4f;
            });
        }
        f *= (float)this.getAttributeValue(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
        if (this.isSubmergedIn(FluidTags.WATER) || this.isSubmergedIn(FluidTags.LAVA)) {
            f *= (float)this.getAttributeInstance(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED).getValue();
        }
        if (!this.isOnGround()) {
            f /= 5.0f;
        }
        cbireturn.setReturnValue(f);
    }

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5f))
    private float modifiedCriticalFactor(float originalFactor) {
        return 1.0f + MathHelper.square(1.0f/3.0f*(float)Math.abs(this.getVelocity().getY()));
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V"))
    private void redirectedAttackSetSprinting(PlayerEntity playerEntity, boolean sprintingBl) {
        this.setSprinting(this.isSprinting());
    }

    @Inject(method = "damageShield", at = @At("HEAD"), cancellable = true)
    private void injectedDamageShield(float amount, CallbackInfo cbi) {
        World world = this.getWorld();
        if (!(this.activeItemStack.getItem() instanceof ShieldItem)) {
            cbi.cancel();
        }
        if (!world.isClient()) {
            ((PlayerEntity)(Object)this).incrementStat(Stats.USED.getOrCreateStat(this.activeItemStack.getItem()));
        }
        RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        int unyieldingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.UNYIELDING), this.activeItemStack);
        if (this.activeItemStack.isOf(Items.SHIELD) && amount >= 3.0f) {
            int i = MathHelper.ceil((1.0f - 0.25f*unyieldingLevel)*(amount - 3.0f));
            Hand hand = this.getActiveHand();
            this.activeItemStack.damage(i, this, LivingEntity.getSlotForHand(hand));
        }
        if (this.activeItemStack.getItem() instanceof ModShieldItem && amount >= ((ModShieldItem)this.activeItemStack.getItem()).getMinDamageToBreak()) {
            int i = MathHelper.ceil((1.0f - 0.25f*unyieldingLevel)*(amount - ((ModShieldItem)this.activeItemStack.getItem()).getMinDamageToBreak()));
            Hand hand = this.getActiveHand();
            this.activeItemStack.damage(i, this, LivingEntity.getSlotForHand(hand));
        }
        if (this.activeItemStack.isEmpty()) {
            Hand hand = this.getActiveHand();
            if (hand == Hand.MAIN_HAND) {
                this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            } 
            else {
                this.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
            this.activeItemStack = ItemStack.EMPTY;
            this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8f, 0.8f + world.random.nextFloat()*0.4f);
        }
        cbi.cancel();
    }

    @Inject(method = "takeShieldHit", at = @At("HEAD"), cancellable = true)
    private void injectedPlayerTakeShieldHit(LivingEntity attacker, CallbackInfo cbi) {
        super.takeShieldHit(attacker);
        if (attacker.disablesShield()) {
            ((PlayerEntity)(Object)this).disableShield();
        }
        cbi.cancel();
    }

    @Override
    public boolean disablesShield() {
        float thresholdf = 1.0f/(1.0f + (float)Math.exp(-(this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) - 20.0)/4));
        float attackDamage = this.invokeGetDamageAgainst(this, (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE), this.getDamageSources().playerAttack((PlayerEntity)(Object)this));
        RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        if (this.getMainHandStack().isIn(ModItemTags.SWEEPING_WEAPONS)) {
            thresholdf += 0.02f*(1.0f + (float)this.getAttributeValue(EntityAttributes.PLAYER_SWEEPING_DAMAGE_RATIO))*attackDamage;
        }
        else if (this.getMainHandStack().isIn(ModItemTags.THRUSTING_WEAPONS)) {
            if (this.isWet()) {
                thresholdf += 0.05f*(1.0f + 0.2f*EnchantmentHelper.getEquipmentLevel(enchantmentLookup.getOrThrow(Enchantments.IMPALING), this))*attackDamage;
            }
            thresholdf += 0.03f*(1.0f + 0.2f*EnchantmentHelper.getEquipmentLevel(enchantmentLookup.getOrThrow(ModEnchantments.THRUSTING), this))*attackDamage;
        }
        else if (this.getMainHandStack().isIn(ModItemTags.HACKING_WEAPONS)) {
            if (this.isOnFire() || this.isInLava()) {
                thresholdf += 0.05f*(1.0f + 0.2f*EnchantmentHelper.getEquipmentLevel(enchantmentLookup.getOrThrow(ModEnchantments.LEANING), this))*attackDamage;
            }
            thresholdf += 0.04f*(1.0f + 0.33f*EnchantmentHelper.getEquipmentLevel(enchantmentLookup.getOrThrow(ModEnchantments.HACKING), this))*attackDamage;
        }
        boolean criticalBl = ((PlayerEntity)(Object)this).getAttackCooldownProgress(0.5f) > 0.9f && this.fallDistance > 0.0f && !this.isOnGround() && !this.isClimbing() && !this.isTouchingWater() && !this.hasVehicle() && !this.isSprinting();
        if (this.isSprinting() || criticalBl) {
            thresholdf += 0.15f;
        }
        return this.random.nextFloat() < thresholdf;
    }

    @Inject(method = "disableShield", at = @At("HEAD"), cancellable = true)
    private void injectedUnyieldingDisableShield(CallbackInfo cbi) {
        World world = this.getWorld();
        if (this.activeItemStack.getItem() instanceof ShieldItem) {
            float randomf = this.random.nextFloat();
            float thresholdf = 1.0f;
            Item activeShieldItem = this.activeItemStack.getItem();
            RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int unyieldingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.UNYIELDING), this.activeItemStack);
            if (this.activeItemStack.isOf(Items.SHIELD)) {
                thresholdf -= 0.01f*3.0f + 0.05f*unyieldingLevel;
                if (randomf < thresholdf) {
                    ((PlayerEntity)(Object)this).getItemCooldownManager().set(activeShieldItem, 60 + this.random.nextBetween(-10, 10) - unyieldingLevel*20);
                    this.clearActiveItem();
                    this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
                }
            }
            else if (this.activeItemStack.getItem() instanceof ModShieldItem) {
                ModShieldItem modShieldItem = (ModShieldItem)this.activeItemStack.getItem();
                thresholdf -= 0.01f*modShieldItem.getMinDamageToBreak() + modShieldItem.getUnyieldingModifier()*unyieldingLevel;
                if (randomf < thresholdf) {
                    ((PlayerEntity)(Object)this).getItemCooldownManager().set(activeShieldItem, modShieldItem.getDisabledTicks() + this.random.nextBetween(-10, 10) - unyieldingLevel*20);
                    this.clearActiveItem();
                    this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
                }
            }
            if (!world.isClient) {
                ((PlayerEntity)(Object)this).incrementStat(Stats.USED.getOrCreateStat(activeShieldItem));
            }
            cbi.cancel();
        }
    }

    @Nullable
    @Override
    public ItemStack getFishingRodStack() {
        return this.fishingRodStack;
    }

    @Override
    public void setFishingRodStack(ItemStack itemStack) {
        this.fishingRodStack = itemStack;
    }


}
