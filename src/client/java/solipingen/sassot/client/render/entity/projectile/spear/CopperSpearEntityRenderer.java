package solipingen.sassot.client.render.entity.projectile.spear;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.TridentEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.client.render.entity.model.ModEntityModelLayers;
import solipingen.sassot.client.render.entity.model.projectile.spear.CopperSpearEntityModel;
import solipingen.sassot.entity.projectile.spear.CopperSpearEntity;


@Environment(value = EnvType.CLIENT)
public class CopperSpearEntityRenderer extends EntityRenderer<CopperSpearEntity, TridentEntityRenderState> {
    public static final Identifier TEXTURE = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/spear/copper_spear.png");
    private final CopperSpearEntityModel model;

    
    public CopperSpearEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new CopperSpearEntityModel(context.getPart(ModEntityModelLayers.COPPER_SPEAR_ENTITY_MODEL_LAYER));
    }

    @Override
    public void render(TridentEntityRenderState spearEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(spearEntityRenderState.yaw - 90.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(spearEntityRenderState.pitch - 90.0f));
        matrixStack.translate(0.0f, -1.7f, 0.0f);
        VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(TEXTURE), false, spearEntityRenderState.enchanted);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(spearEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public TridentEntityRenderState createRenderState() {
        return new TridentEntityRenderState();
    }

    @Override
    public void updateRenderState(CopperSpearEntity spearEntity, TridentEntityRenderState tridentEntityRenderState, float f) {
        super.updateRenderState(spearEntity, tridentEntityRenderState, f);
        tridentEntityRenderState.yaw = spearEntity.getLerpedYaw(f);
        tridentEntityRenderState.pitch = spearEntity.getLerpedPitch(f);
        tridentEntityRenderState.enchanted = spearEntity.isEnchanted();
    }
}
