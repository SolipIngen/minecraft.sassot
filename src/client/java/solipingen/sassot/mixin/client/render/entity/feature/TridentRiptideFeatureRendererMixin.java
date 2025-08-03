package solipingen.sassot.mixin.client.render.entity.feature;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.model.TridentRiptideEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.client.util.interfaces.mixin.render.LivingEntityRenderStateInterface;


@Mixin(TridentRiptideFeatureRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class TridentRiptideFeatureRendererMixin extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    @Shadow @Final private TridentRiptideEntityModel model;


    public TridentRiptideFeatureRendererMixin(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/PlayerEntityRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    private void injectedRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, PlayerEntityRenderState playerEntityRenderState, float f, float g, CallbackInfo cbi) {
        if (playerEntityRenderState.usingRiptide) {
            LivingEntityRenderStateInterface iPlayerEntityRenderState = (LivingEntityRenderStateInterface)playerEntityRenderState;
            Identifier texture = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/spear_whirlwind.png");
            if (iPlayerEntityRenderState.getIsUsingWhirlwind() && iPlayerEntityRenderState.getIsBeingSnowedOn() || iPlayerEntityRenderState.getInPowderSnow()) {
                texture = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/spear_whirlwind_snow.png");
            }
            if (!iPlayerEntityRenderState.getIsUsingWhirlwind() && !iPlayerEntityRenderState.getIsUsingFlare()) {
                texture = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/trident_riptide.png");
            }
            if (iPlayerEntityRenderState.getIsUsingFlare() && !iPlayerEntityRenderState.getIsUsingWhirlwind()) {
                texture = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/blazearm_flare.png");
            }
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(texture));
            this.model.setAngles(playerEntityRenderState);
            this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
            cbi.cancel();
        }
    }


}
