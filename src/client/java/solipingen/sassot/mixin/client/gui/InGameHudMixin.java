package solipingen.sassot.mixin.client.gui;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import solipingen.sassot.mixin.block.accessors.AbstractBlockAccessor;
import solipingen.sassot.registry.tag.ModItemTags;
import solipingen.sassot.util.interfaces.mixin.client.MinecraftClientInterface;


@Mixin(InGameHud.class)
@Environment(value = EnvType.CLIENT)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;


    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"), cancellable = true)
    private void injectedRenderCrosshair(DrawContext context, CallbackInfo cbi) {
        if (this.client.targetedEntity == null && this.client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            ItemStack mainHandStack = this.client.player.getMainHandStack();
            if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS) || mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isIn(ModItemTags.HACKING_WEAPONS)) {
                BlockHitResult blockHitResult = (BlockHitResult)this.client.crosshairTarget;
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = this.client.world.getBlockState(blockPos);
                if (!((AbstractBlockAccessor)blockState.getBlock()).getCollidable() && (blockState.getBlock().getHardness() == 0.0f || blockState.getBlock() instanceof VineBlock)) {
                    ((MinecraftClientInterface)this.client).updateCrosshairTargetThroughBlock(1.0f);
                }
            }
        }
        float distanceThreshold = this.client.player.isCreative() ? this.client.interactionManager.getReachDistance() + 1.0f : this.client.interactionManager.getReachDistance();
        if (this.client.targetedEntity != null && (float)this.client.targetedEntity.squaredDistanceTo(this.client.player) > distanceThreshold*distanceThreshold) {
            cbi.cancel();
        }
    }

}
