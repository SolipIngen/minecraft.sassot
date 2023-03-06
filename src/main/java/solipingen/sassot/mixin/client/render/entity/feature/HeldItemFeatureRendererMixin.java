package solipingen.sassot.mixin.client.render.entity.feature;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(HeldItemFeatureRenderer.class)
@Environment(value=EnvType.CLIENT)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow @Final private HeldItemRenderer heldItemRenderer;
    

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void injectedRenderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo cbi) {
        if (entity.handSwinging && (stack.getItem() instanceof SpearItem || stack.getItem() instanceof TridentItem)) {
            matrices.push();
            ((ModelWithArms)((HeldItemFeatureRenderer<?, ?>)(Object)this).getContextModel()).setArmAngle(arm, matrices);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-30.0f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
            boolean bl = arm == Arm.LEFT;
            matrices.translate((bl ? -1.0f : 1.0f) / 16.0f, entity.handSwingProgress < 0.33f ? 0.33f : -(0.33f/0.67f)*(entity.handSwingProgress - 0.33f) + 0.33f, -0.425f);
            this.heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light);
            matrices.pop();
            cbi.cancel();
        }
        else if (entity.handSwinging && stack.getItem() instanceof BlazearmItem) {
            matrices.push();
            ((ModelWithArms)((HeldItemFeatureRenderer<?, ?>)(Object)this).getContextModel()).setArmAngle(arm, matrices);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-45.0f));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
            boolean bl = arm == Arm.LEFT;
            matrices.translate((bl ? -1.0f : 1.0f) / 16.0f, 0.67f*entity.handSwingProgress, -0.425f);
            this.heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light);
            matrices.pop();
            cbi.cancel();
        }
    }
    


}
