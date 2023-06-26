package solipingen.sassot.mixin.client.render.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(HeldItemRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class HeldItemRendererMixin {
    @Shadow private ItemStack mainHand;

    
    @ModifyConstant(method = "renderFirstPersonItem", constant = @Constant(floatValue = 0.8f))
    private float modifiedShieldRiptideRender(float originalf, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (player.isUsingRiptide() && item.getItem() instanceof ShieldItem) {
            return 1.2f;
        }
        return originalf;
    }

    @Inject(method = "applySwingOffset", at = @At("HEAD"), cancellable = true)
    private void injectedApplySwingOffset(MatrixStack matrices, Arm arm, float swingProgress, CallbackInfo cbi) {
        if (mainHand.getItem() instanceof SpearItem && swingProgress > 0.0f) {
            int i = arm == Arm.RIGHT ? 1 : -1;
            float f = swingProgress < 0.67f ? 1.0f : MathHelper.cos((swingProgress - 0.67f) * (swingProgress - 0.67f) * (float)Math.PI);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * (45.0f + f * -20.0f)));
            float g = swingProgress < 0.67f ? 1.0f : MathHelper.cos(MathHelper.sqrt(swingProgress - 0.67f) * (float)Math.PI);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -80.0f));
            matrices.translate(0.5f, 0.67f*g, 0.0);
            cbi.cancel();
        }
        else if (mainHand.getItem() instanceof TridentItem && swingProgress > 0.0f) {
            int i = arm == Arm.RIGHT ? 1 : -1;
            float f = swingProgress < 0.67f ? 1.0f : MathHelper.cos((swingProgress - 0.67f) * (swingProgress - 0.67f) * (float)Math.PI);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * (45.0f + f * -20.0f)));
            float g = swingProgress < 0.67f ? 1.0f : MathHelper.cos(MathHelper.sqrt(swingProgress - 0.67f) * (float)Math.PI);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -80.0f));
            matrices.translate(0.25f, 0.75f*g, 0.0);
            cbi.cancel();
        }
        else if (mainHand.getItem() instanceof BlazearmItem && swingProgress > 0.0f) {
            int i = arm == Arm.RIGHT ? 1 : -1;
            float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * (45.0f + f * -20.0f)));
            float g = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)i * g * -20.0f));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -80.0f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * -45.0f));
            matrices.translate(0.5f, 0.5f*g, 0.0);
            cbi.cancel();
        }
    }

    
}
