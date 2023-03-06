package solipingen.sassot.mixin.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.mixin.block.accessors.AbstractBlockAccessor;
import solipingen.sassot.util.interfaces.mixin.client.MinecraftClientInterface;


@Mixin(MinecraftClient.class)
@Environment(value=EnvType.CLIENT)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler, MinecraftClientInterface {
    @Shadow @Nullable public ClientPlayerEntity player;
    @Shadow @Nullable public ClientWorld world;
    @Shadow @Nullable public Entity targetedEntity;
    @Shadow @Nullable public HitResult crosshairTarget;
    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;


    public MinecraftClientMixin(String string) {
        super(string);
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"))
    private void injectedHandleBlockBreaking(boolean breaking, CallbackInfo cbi) {
        if (this.crosshairTarget != null && this.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            ItemStack mainHandStack = this.player.getMainHandStack();
            if (mainHandStack.getItem() instanceof SwordItem || mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof TridentItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof AxeItem) {
                BlockHitResult blockHitResult = (BlockHitResult)this.crosshairTarget;
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = this.world.getBlockState(blockPos);
                if (!((AbstractBlockAccessor)blockState.getBlock()).getCollidable() && (blockState.getBlock().getHardness() == 0.0f || blockState.getBlock() instanceof VineBlock)) {
                    this.updateCrosshairTargetThroughBlock(1.0f);
                }
            }
        }
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void injectedDoAttack(CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.crosshairTarget != null && this.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            ItemStack mainHandStack = this.player.getMainHandStack();
            if (mainHandStack.getItem() instanceof SwordItem || mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof TridentItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof AxeItem) {
                BlockHitResult blockHitResult = (BlockHitResult)this.crosshairTarget;
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = this.world.getBlockState(blockPos);
                if (!((AbstractBlockAccessor)blockState.getBlock()).getCollidable() && (blockState.getBlock().getHardness() == 0.0f || blockState.getBlock() instanceof VineBlock)) {
                    this.updateCrosshairTargetThroughBlock(1.0f);
                }
            }
        }
    }

    @Override
    public void updateCrosshairTargetThroughBlock(float tickDelta) {
        Entity entity2 = ((MinecraftClient)(Object)this).getCameraEntity();
        ItemStack mainHandStack = this.player.getMainHandStack();
        if (entity2 == null) {
            return;
        }
        if (this.world == null) {
            return;
        }
        double d = this.interactionManager.getReachDistance();
        this.crosshairTarget = entity2.raycast(d, tickDelta, false);
        if (this.crosshairTarget.getType() == Type.BLOCK) {
            this.crosshairTarget = null;
            this.targetedEntity = null;
        }
        Vec3d vec3d = entity2.getCameraPosVec(tickDelta);
        boolean bl = false;
        double attackReach = 3.0;
        if (mainHandStack.getItem() instanceof SwordItem) {
            attackReach += 1.0;
        }
        else if (mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof TridentItem) {
            int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, mainHandStack);
            double thrustAddition = (player.isOnGround() && !player.isSprinting() && !(mainHandStack.getItem() instanceof BlazearmItem)) ? thrustLevel/3.0 : 0.0;
            attackReach += 2.0 + thrustAddition;
        }
        double e = d;
        if (this.interactionManager.hasExtendedReach()) {
            if (mainHandStack.getItem() instanceof SwordItem) {
                d = e = 7.0;
            }
            else if (mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof TridentItem) {
                int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, mainHandStack);
                double thrustAddition = (player.isOnGround() && !player.isSprinting() && !(mainHandStack.getItem() instanceof BlazearmItem)) ? thrustLevel/3.0 : 0.0;
                d = e = 8.0 + thrustAddition;
            }
            else {
                d = e = 6.0;
            }
        } 
        else {
            if (e > attackReach) {
                bl = true;
            }
            d = e;
        }
        e *= e;
        if (this.crosshairTarget != null) {
            e = this.crosshairTarget.getPos().squaredDistanceTo(vec3d);
        }
        Vec3d vec3d2 = entity2.getRotationVec(1.0f);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
        Box box = entity2.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(entity2, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit(), e);
        if (entityHitResult == null) {
            this.crosshairTarget = entity2.raycast(this.interactionManager.getReachDistance(), tickDelta, false);
        }
        else {
            Vec3d vec3d4 = entityHitResult.getPos();
            double g = vec3d.squaredDistanceTo(vec3d4);
            if (bl && g > attackReach*attackReach) {
                this.crosshairTarget = BlockHitResult.createMissed(vec3d4, Direction.getFacing(vec3d2.x, vec3d2.y, vec3d2.z), new BlockPos(vec3d4));
            } 
            else {
                this.crosshairTarget = entityHitResult;
                this.targetedEntity = entityHitResult.getEntity();
            }
        }
    }

    
}
