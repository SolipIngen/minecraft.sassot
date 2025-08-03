package solipingen.sassot.mixin.client.render.entity.feature;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.client.util.interfaces.mixin.render.LivingEntityRenderStateInterface;


@Mixin(HeldItemFeatureRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class HeldItemFeatureRendererMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ModelWithArms> extends FeatureRenderer<S, M> {
//    @Shadow @Final private HeldItemRenderer heldItemRenderer;

    public HeldItemFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }


    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void injectedRenderItem(S entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo cbi) {
        if (entityState instanceof PlayerEntityRenderState playerState) {
            LivingEntityRenderStateInterface iEntityState = (LivingEntityRenderStateInterface)entityState;
            if (playerState.handSwinging && iEntityState.getIsAttackingWithSpear()) {
                matrices.push();
                ((ModelWithArms)((HeldItemFeatureRenderer<?, ?>)(Object)this).getContextModel()).setArmAngle(arm, matrices);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-30.0f));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
                boolean bl = arm == Arm.LEFT;
                matrices.translate((bl ? -1.0f : 1.0f) / 16.0f, playerState.handSwingProgress < 0.33f ? 0.33f : -(0.33f/0.67f)*(entityState.limbSwingAnimationProgress - 0.33f) + 0.33f, -0.425f);
                itemState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
                matrices.pop();
                cbi.cancel();
            }
            else if (playerState.handSwinging && iEntityState.getIsAttackingWithBlazearm()) {
                matrices.push();
                ((ModelWithArms)((HeldItemFeatureRenderer<?, ?>)(Object)this).getContextModel()).setArmAngle(arm, matrices);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-45.0f));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
                boolean bl = arm == Arm.LEFT;
                matrices.translate((bl ? -1.0f : 1.0f) / 16.0f, 0.67f*playerState.handSwingProgress, -0.425f);
                itemState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
                matrices.pop();
                cbi.cancel();
            }
        }
    }
    


}
