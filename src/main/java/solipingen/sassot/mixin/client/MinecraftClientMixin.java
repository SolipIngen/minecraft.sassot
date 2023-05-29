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
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.RaycastContext;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.mixin.block.accessors.AbstractBlockAccessor;
import solipingen.sassot.registry.tag.ModItemTags;
import solipingen.sassot.util.interfaces.mixin.client.MinecraftClientInterface;


@Mixin(MinecraftClient.class)
@Environment(value = EnvType.CLIENT)
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
            if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS) || mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isIn(ModItemTags.HACKING_WEAPONS)) {
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
            if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS) || mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isIn(ModItemTags.HACKING_WEAPONS)) {
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
        if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
            attackReach += 0.5;
        }
        else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
            attackReach += 1.0;
        }
        double e = d;
        if (this.interactionManager.hasExtendedReach()) {
            if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
                d = e = 6.5;
            }
            else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
                d = e = 7.0;
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
            BlockHitResult blockHitResult = this.world.raycast(new RaycastContext(vec3d, vec3d4, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity2));
            if ((bl && g > attackReach*attackReach) || blockHitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = new BlockPos(MathHelper.floor(vec3d4.getX()), MathHelper.floor(vec3d4.getY()), MathHelper.floor(vec3d4.getZ()));
                this.crosshairTarget = BlockHitResult.createMissed(vec3d4, Direction.getFacing(vec3d2.x, vec3d2.y, vec3d2.z), blockPos);
            } 
            else {
                this.crosshairTarget = entityHitResult;
                this.targetedEntity = entityHitResult.getEntity();
            }
        }
    }

    
}
