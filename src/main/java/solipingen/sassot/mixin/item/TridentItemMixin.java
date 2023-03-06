package solipingen.sassot.mixin.item;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.item.Vanishable;
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


@Mixin(TridentItem.class)
public class TridentItemMixin extends Item implements Vanishable {

    
    public TridentItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyConstant(method = "<init>", constant = @Constant(doubleValue = 8.0))
    private double modifiedAttackDamageModifier(double originalAttackDamageModifier) {
        return 9.0;
    }

    @ModifyConstant(method = "<init>", constant = @Constant(doubleValue = (double)-2.9f))
    private double modifiedAttackSpeedModifier(double originalAttackSpeedModifier) {
        return -2.4;
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
    private void redirectedSetVelocity(TridentEntity tridentEntity, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        tridentEntity.setVelocity(shooter, pitch, yaw, roll, speed, divergence);
        tridentEntity.addVelocity(shooter.getVelocity());
    }

    @Inject(method = "onStoppedUsing", at = @At("TAIL"))
    private void injectedOnStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo cbi) {
        int j = EnchantmentHelper.getRiptide(stack);
        if (user instanceof PlayerEntity && j > 0) {
            PlayerEntity playerEntity = (PlayerEntity)user;
            playerEntity.setNoDrag(true);
            Box riptideBox = playerEntity.getBoundingBox().expand(6.0*j, 3.0*j, 6.0*j);
            List<Entity> entityList = playerEntity.world.getOtherEntities(playerEntity, riptideBox);
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
                    float attackAddition = EnchantmentHelper.getAttackDamage(stack, livingOtherEntity.getGroup());
                    damageAmount += attackAddition;
                    damageAmount *= 0.5f*j*(float)distanceModifier;
                    damageAmount *= (livingOtherEntity.isWet() || livingOtherEntity.getGroup() == EntityGroup.AQUATIC || livingOtherEntity instanceof DrownedEntity || livingOtherEntity.hurtByWater()) ? 2.0f : 1.0f;
                    damageAmount *= playerEntity.world.isThundering() ? 1.5f : 1.0f;
                    livingOtherEntity.damage(DamageSource.player(playerEntity), damageAmount);
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

    @ModifyConstant(method = "postMine", constant = @Constant(intValue = 2))
    private int modifiedDurabilityDamage(int damage) {
        return 0;
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
