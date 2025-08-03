package solipingen.sassot.mixin.entity.player;

import java.util.List;
import java.util.Objects;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
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
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.player.PlayerEntityInterface;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityInterface {
    @Shadow @Final PlayerInventory inventory;
    @Shadow protected boolean isSubmergedInWater;
    @Nullable @Unique private ItemStack fishingRodStack;


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
            if (iLivingEntity.getIsUsingFlare() && this.isTouchingWaterOrRain()) {
                this.setNoDrag(false);
            } else if (iLivingEntity.getIsUsingWhirlwind() && (this.isTouchingWater() || this.isInLava())) {
                this.setNoDrag(false);
            } else if (iLivingEntity.getIsUsingTridentRiptide() && (!this.isTouchingWaterOrRain() || this.isInLava())) {
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
                RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
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
                        float damageAmount = EnchantmentHelper.getDamage(serverWorld, this.riptideStack, otherEntity, this.getDamageSources().playerAttack((PlayerEntity) (Object) this), (float) this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE));
                        if (this.riptideStack.isOf(Items.TRIDENT) && !livingOtherEntity.getType().isIn(EntityTypeTags.SENSITIVE_TO_IMPALING) && livingOtherEntity.isTouchingWaterOrRain()) {
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
                        damageAmount *= (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.RIPTIDE), this.riptideStack) > 0 && (livingOtherEntity.isTouchingWaterOrRain() || livingOtherEntity.getType().isIn(EntityTypeTags.SENSITIVE_TO_IMPALING) || livingOtherEntity.hurtByWater())) ? 2.0f : 1.0f;
                        damageAmount *= (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), this.riptideStack) > 0 && (livingOtherEntity.getType().isIn(ModEntityTypeTags.SENSITIVE_TO_LEANING) || livingOtherEntity.isOnFire() || livingOtherEntity.isInLava() || otherEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK) || livingOtherEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE))) ? 2.0f : 1.0f;
                        damageAmount *= this.getWorld().isThundering() ? 1.5f : 1.0f;
                        if (!(livingOtherEntity.isSubmergedInWater() || livingOtherEntity.isSubmergedIn(FluidTags.LAVA)) && EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.WHIRLWIND), this.riptideStack) > 0 && !livingOtherEntity.getType().isIn(ModEntityTypeTags.IMMUNE_TO_WHIRLWIND)) {
                            livingOtherEntity.damage(serverWorld, this.getDamageSources().playerAttack((PlayerEntity)(Object)this), damageAmount);
                            if (((EntityInterface) livingOtherEntity).isBeingSnowedOn()) {
                                livingOtherEntity.setFrozenTicks(livingOtherEntity.getMinFreezeDamageTicks() * (j + 1));
                            }
                            if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE)) * 1.5 * j * distanceModifier <= 0.0)) {
                                Vec3d vec3d = livingOtherEntity.getVelocity();
                                Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE)) * 1.5 * j * distanceModifier);
                                otherEntity.setVelocity(vec3d.x - vec3d2.x, livingOtherEntity.isOnGround() ? Math.min(vec3d.y + (1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE)) * 1.5 * j * distanceModifier, 0.4) : vec3d.y, vec3d.z - vec3d2.z);
                            }
                        } else if (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.RIPTIDE), this.riptideStack) > 0) {
                            livingOtherEntity.damage(serverWorld, this.getDamageSources().playerAttack((PlayerEntity)(Object)this), damageAmount);
                            if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE)) * 2.0 * j * distanceModifier <= 0.0)) {
                                Vec3d vec3d = livingOtherEntity.getVelocity();
                                Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE)) * 2.0 * j * distanceModifier);
                                otherEntity.setVelocity(vec3d.x - vec3d2.x, vec3d.y, vec3d.z - vec3d2.z);
                            }
                        } else if (!livingOtherEntity.isTouchingWaterOrRain() && EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), this.riptideStack) > 0 && !livingOtherEntity.getType().isIn(ModEntityTypeTags.IMMUNE_TO_FLARE)) {
                            livingOtherEntity.damage(serverWorld, this.getDamageSources().playerAttack((PlayerEntity)(Object)this), damageAmount);
                            if (!livingOtherEntity.isOnFire()) {
                                livingOtherEntity.setOnFireFor((int) Math.ceil(3.0 * j * distanceModifier));
                            }
                            if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE)) * 2.0 * j * distanceModifier <= 0.0)) {
                                Vec3d vec3d = livingOtherEntity.getVelocity();
                                Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE)) * 2.0 * j * distanceModifier);
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
        float f = this.inventory.getSelectedStack().getMiningSpeedMultiplier(block);
        if (f > 1.0f) {
            int i = (int)Math.sqrt(this.getAttributeValue(EntityAttributes.MINING_EFFICIENCY) - 1.0);
            if (i > 0) {
                f *= (float)Math.log(Math.E - 1.0 + Math.pow(f, i/Math.PI));
            }
        }
        if (StatusEffectUtil.hasHaste(this)) {
            f *= 1.0f + 0.5f*(StatusEffectUtil.getHasteAmplifier(this) + 1);
        }
        if (this.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            f *= (switch (Objects.requireNonNull(this.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier()) {
                case 0 -> 0.3f;
                case 1 -> 0.09f;
                case 2 -> 0.0027f;
                default -> 8.1e-4f;
            });
        }
        f *= (float)this.getAttributeValue(EntityAttributes.BLOCK_BREAK_SPEED);
        if (this.getAttributeInstance(EntityAttributes.SUBMERGED_MINING_SPEED) != null && (this.isSubmergedIn(FluidTags.WATER) || this.isSubmergedIn(FluidTags.LAVA))) {
            f *= (float)Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.SUBMERGED_MINING_SPEED)).getValue();
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

    @Inject(method = "takeShieldHit", at = @At("HEAD"), cancellable = true)
    private void injectedPlayerTakeShieldHit(ServerWorld world, LivingEntity attacker, CallbackInfo cbi) {
        super.takeShieldHit(world, attacker);
        ItemStack itemStack = this.getBlockingItem();
        BlocksAttacksComponent blocksAttacksComponent = itemStack != null ? itemStack.get(DataComponentTypes.BLOCKS_ATTACKS) : null;
        if (blocksAttacksComponent != null) {
            float disableSeconds = attacker.getWeaponDisableBlockingForSeconds();
            if (disableSeconds > 0.0f) {
                float randomf = this.random.nextFloat();
                float thresholdf = 1.0f;
                RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
                int unyieldingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.UNYIELDING), itemStack);
                if (itemStack.getItem() instanceof ModShieldItem modShieldItem) {
                    thresholdf -= 0.01f*blocksAttacksComponent.itemDamage().threshold() + modShieldItem.getUnyieldingModifier()*unyieldingLevel;
                }
                else if (itemStack.isOf(Items.SHIELD)) {
                    thresholdf -= 0.01f*blocksAttacksComponent.itemDamage().threshold() + 0.05f*unyieldingLevel;
                }
                ItemStack attackingStack = attacker.getWeaponStack();
                if (this.isTouchingWaterOrRain()) {
                    thresholdf += 0.05f*EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.IMPALING), attackingStack);
                }
                else if (this.isOnFire() || this.isInLava()) {
                    thresholdf += 0.05f*EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.LEANING), attackingStack);
                }
                if (randomf < thresholdf) {
                    blocksAttacksComponent.applyShieldCooldown(world, this, disableSeconds + (this.random.nextFloat() - 0.5f), itemStack);
                }
            }
        }
        cbi.cancel();
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
