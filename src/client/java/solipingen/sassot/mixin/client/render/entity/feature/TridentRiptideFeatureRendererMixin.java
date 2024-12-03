package solipingen.sassot.mixin.client.render.entity.feature;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.RotationAxis;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


@Mixin(TridentRiptideFeatureRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class TridentRiptideFeatureRendererMixin<T extends LivingEntity> {
    @Shadow @Final private ModelPart aura;


    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void injectedRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo cbi) {
        if (livingEntity.isUsingRiptide()) {
            Identifier texture = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/spear_whirlwind.png");
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            if (iLivingEntity.getIsUsingWhirlwind() && (((EntityInterface)livingEntity).isBeingSnowedOn() || livingEntity.inPowderSnow)){
                texture = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/spear_whirlwind_snow.png");
            }
            if (!iLivingEntity.getIsUsingWhirlwind() && !iLivingEntity.getIsUsingFlare()) {
                texture = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/trident_riptide.png");
            }
            if (iLivingEntity.getIsUsingFlare() && !iLivingEntity.getIsUsingWhirlwind()) {
                texture = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/blazearm_flare.png");
            }
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
            for(int m = 0; m < 3; ++m) {
                matrixStack.push();
                float n = j * (float)(-(45 + m * 5));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(n));
                float o = 0.75f * (float)m;
                matrixStack.scale(o, o, o);
                matrixStack.translate(0.0F, -0.2f + 0.6f * (float)m, 0.0f);
                this.aura.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
                matrixStack.pop();
            }
            cbi.cancel();
        }
    }


}
