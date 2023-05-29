package solipingen.sassot.client.render.entity.projectile.spear;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.client.render.entity.model.ModEntityModelLayers;
import solipingen.sassot.client.render.entity.model.projectile.spear.FlintSpearEntityModel;
import solipingen.sassot.entity.projectile.spear.FlintSpearEntity;


@Environment(value = EnvType.CLIENT)
public class FlintSpearEntityRenderer extends EntityRenderer<FlintSpearEntity> {
    public static final Identifier TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/spear/flint_spear.png");
    private final FlintSpearEntityModel model;

    
    public FlintSpearEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new FlintSpearEntityModel(context.getPart(ModEntityModelLayers.FLINT_SPEAR_ENTITY_MODEL_LAYER));
    }

    @Override
    public void render(FlintSpearEntity spearEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, spearEntity.prevYaw, spearEntity.getYaw()) - 90.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, spearEntity.prevPitch, spearEntity.getPitch()) - 90.0f));
        matrixStack.translate(0.0f, -1.5f, 0.0f);
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(spearEntity)), false, spearEntity.isEnchanted());
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
        super.render(spearEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(FlintSpearEntity spearEntity) {
        return TEXTURE;
    }
}
