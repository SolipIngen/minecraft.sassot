package solipingen.sassot.mixin.server.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.registry.tag.ModEntityTypeTags;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {


    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "getDamageAgainst", at = @At("HEAD"), cancellable = true)
    private void injectedGetDamageAgainst(Entity target, float baseDamage, DamageSource damageSource, CallbackInfoReturnable<Float> cbireturn) {
        float damage = EnchantmentHelper.getDamage(((ServerPlayerEntity)(Object)this).getServerWorld(), this.getWeaponStack(), target, damageSource, baseDamage);
        ItemStack mainHandStack = this.getWeaponStack();
        RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        boolean criticalBl = this.getAttackCooldownProgress(0.5f) > 0.9f && this.fallDistance > 0.0f && !this.isOnGround() && !this.isClimbing()
                && !this.isTouchingWater() && !this.hasVehicle() && target instanceof LivingEntity && !this.isSprinting();
        if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS)) {
            if (mainHandStack.isOf(Items.TRIDENT) && !target.getType().isIn(EntityTypeTags.SENSITIVE_TO_IMPALING) && target.isWet()) {
                damage += 1.0f* EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.IMPALING), mainHandStack);
            }
        }
        else if (mainHandStack.isIn(ModItemTags.HACKING_WEAPONS)) {
            damage *= 1.0f + (((this.isSprinting() && !((!this.isOnGround() && this.isTouchingWater())
                    || this.isSubmergedInWater() || this.isInLava() || this.isInSwimmingPose())) || criticalBl) ?
                    0.15f*EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.HACKING), mainHandStack) : 0.0f);
            if (mainHandStack.isOf(ModItems.BLAZEARM)) {
                BlockState targetEntityMagmaBlockState = Blocks.AIR.getDefaultState();
                Iterable<BlockPos> targetEntityBlockPosIterable = BlockPos.iterateOutwards(target.getBlockPos(), 1, 1, 1);
                for (BlockPos targetEntityBlockPos : targetEntityBlockPosIterable) {
                    if (targetEntityBlockPos.getManhattanDistance(target.getBlockPos()) > 1) continue;
                    targetEntityMagmaBlockState = this.getWorld().getBlockState(targetEntityBlockPos);
                    if (targetEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK)) break;
                }
                if (!target.getType().isIn(ModEntityTypeTags.SENSITIVE_TO_LEANING) && (target.isOnFire() || target.isInLava() || targetEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK) || (target instanceof LivingEntity && ((LivingEntity)target).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)))) {
                    damage += 1.0f*EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.LEANING), mainHandStack);
                }
            }
        }
        cbireturn.setReturnValue(damage);
    }


}
