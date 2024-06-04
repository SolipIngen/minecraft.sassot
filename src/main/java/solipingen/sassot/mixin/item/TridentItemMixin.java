package solipingen.sassot.mixin.item;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.EntityTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import solipingen.sassot.advancement.ModCriteria;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


@Mixin(TridentItem.class)
public class TridentItemMixin extends Item {

    
    public TridentItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static Item.Settings modifiedInit(Item.Settings settings) {
        return settings.maxDamage(1095);
    }

    @Inject(method = "createAttributeModifiers", at = @At("HEAD"), cancellable = true)
    private static void injectedAttributeModifiers(CallbackInfoReturnable<AttributeModifiersComponent> cbireturn) {
        cbireturn.setReturnValue(AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 9.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(Item.ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -2.4f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE,
                        new EntityAttributeModifier(SpearItem.REACH_MODIFIER_ID, "Tool modifier", 1.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                        new EntityAttributeModifier(SpearItem.ATTACK_RANGE_MODIFIER_ID, "Weapon modifier", 1.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build());
    }

    @Inject(method = "createToolComponent", at = @At("HEAD"), cancellable = true)
    private static void injectedToolComponent(CallbackInfoReturnable<ToolComponent> cbireturn) {
        cbireturn.setReturnValue(new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(List.of(Blocks.COBWEB), 20.0f),
                ToolComponent.Rule.of(BlockTags.SWORD_EFFICIENT, 2.0f)), 1.0f, 1));
    }

    @ModifyConstant(method = "onStoppedUsing", constant = @Constant(floatValue = 2.5f))
    private float modifiedReleaseSpeed(float originalf, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int strengthLevel = user.hasStatusEffect(StatusEffects.STRENGTH) ? user.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = user.hasStatusEffect(StatusEffects.WEAKNESS) ? user.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        return originalf + 0.2f*strengthLevel - 0.2f*weaknessLevel;
    }

    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean redirectedIsTouchingWaterOrRain(PlayerEntity playerEntity) {
        return playerEntity.isWet();
    }

    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    private void redirectedSetVelocity(TridentEntity tridentEntity, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence, ItemStack stack) {
        tridentEntity.setVelocity(shooter, pitch, yaw, roll, speed, divergence);
        tridentEntity.setPierceLevel((byte)EnchantmentHelper.getLevel(Enchantments.PIERCING, stack));
    }

    @Inject(method = "onStoppedUsing", at = @At("TAIL"))
    private void injectedOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo cbi) {
        int j = EnchantmentHelper.getRiptide(stack);
        if (user instanceof PlayerEntity && j > 0) {
            PlayerEntity playerEntity = (PlayerEntity)user;
            ((LivingEntityInterface)playerEntity).setIsUsingTridentRiptide(true);
            playerEntity.setNoDrag(true);
            Box riptideBox = playerEntity.getBoundingBox().expand(6.0*j, 3.0*j, 6.0*j);
            List<Entity> entityList = playerEntity.getWorld().getOtherEntities(playerEntity, riptideBox);
            for (Entity otherEntity : entityList) {
                if (playerEntity.squaredDistanceTo(otherEntity) > (6.0*j)*(6.0*j)) continue;
                boolean reachBl = false;
                for (int i = 0; i < MathHelper.ceil(otherEntity.getHeight() + 1.0f); ++i) {
                    Vec3d reachVec3d = new Vec3d(otherEntity.getX(), otherEntity.getBodyY(1.0/MathHelper.ceil(otherEntity.getHeight())*i), otherEntity.getZ());
                    BlockHitResult hitResult = world.raycast(new RaycastContext(playerEntity.getPos(), reachVec3d, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, playerEntity));
                    if (((HitResult)hitResult).getType() != HitResult.Type.MISS) continue;
                    reachBl = true;
                    break;
                }
                if (!reachBl) continue;
                float damageAmount = (float)playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                double xDiff = playerEntity.getX() - otherEntity.getX();
                double yDiff = playerEntity.getY() - otherEntity.getY();
                double zDiff = playerEntity.getZ() - otherEntity.getZ();
                double distanceModifier = 1.0/(1.0 + Math.sqrt(xDiff*xDiff + yDiff*yDiff + zDiff*zDiff));
                if (otherEntity instanceof LivingEntity) {
                    LivingEntity livingOtherEntity = (LivingEntity)otherEntity;
                    float attackAddition = EnchantmentHelper.getAttackDamage(stack, livingOtherEntity.getType());
                    if (stack.isOf(Items.TRIDENT) && !livingOtherEntity.getType().isIn(EntityTypeTags.SENSITIVE_TO_IMPALING) && livingOtherEntity.isWet()) {
                        attackAddition += 1.0f*EnchantmentHelper.getLevel(Enchantments.IMPALING, stack);
                    }
                    damageAmount += attackAddition;
                    damageAmount *= 0.5f*j*(float)distanceModifier;
                    damageAmount *= (livingOtherEntity.isWet() || livingOtherEntity.getType().isIn(EntityTypeTags.SENSITIVE_TO_IMPALING) || livingOtherEntity.hurtByWater()) ? 2.0f : 1.0f;
                    damageAmount *= playerEntity.getWorld().isThundering() ? 1.5f : 1.0f;
                    livingOtherEntity.damage(playerEntity.getDamageSources().playerAttack(playerEntity), damageAmount);
                    if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*2.0*j*distanceModifier <= 0.0)) {
                        Vec3d vec3d = livingOtherEntity.getVelocity();
                        Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*2.0*j*distanceModifier);
                        otherEntity.setVelocity(vec3d.x - vec3d2.x, vec3d.y, vec3d.z - vec3d2.z);
                    }
                }
                else {
                    Vec3d vec3d = otherEntity.getVelocity();
                    Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply(2.0*j*distanceModifier);
                    otherEntity.setVelocity(vec3d.x - vec3d2.x, otherEntity.isOnGround() ? Math.min(vec3d.y + 2.0*j*distanceModifier, 0.4) : vec3d.y, vec3d.z - vec3d2.z);
                }
            }
            Iterable<BlockPos> blockPosList = BlockPos.iterateOutwards(playerEntity.getBlockPos(), MathHelper.ceil(playerEntity.getWidth()), MathHelper.ceil(playerEntity.getHeight()), MathHelper.ceil(playerEntity.getWidth()));
            for (BlockPos currentBlockPos : blockPosList) {
                if (world.getBlockState(currentBlockPos).isOf(Blocks.POWDER_SNOW)) {
                    world.breakBlock(currentBlockPos, false);
                }
            }
        }
        if (user instanceof ServerPlayerEntity) {
            ModCriteria.THROW_SPEAR.trigger((ServerPlayerEntity)user, stack);
        }
    }

    @ModifyConstant(method = "getEnchantability", constant = @Constant(intValue = 1))
    private int modifiedEnchantability(int enchantability) {
        return 10;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.PRISMARINE_SHARD);
    }
    
    
}
