package solipingen.sassot.client.render.entity.model.projectile.spear;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.entity.projectile.spear.WoodenSpearEntity;


@Environment(value = EnvType.CLIENT)
public class WoodenSpearEntityModel extends EntityModel<WoodenSpearEntity> {
	public static final Identifier TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/spear/wooden_spear.png");
	private final ModelPart pole;
	private final ModelPart tip;
	

	public WoodenSpearEntityModel(ModelPart root) {
		this.pole = root.getChild("pole");
		this.tip = root.getChild("tip");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("pole", ModelPartBuilder.create().uv(0, 6).cuboid(0.0f, 0.0f, 0.0f, 1.0f, 27.0f, 1.0f, new Dilation(0.0f)), ModelTransform.NONE);
		modelPartData.addChild("tip", ModelPartBuilder.create()
			.uv(5, 0).cuboid(0.0f, 27.0f, 0.0f, 1.0f, 1.2f, 1.0f, new Dilation(0.0f))
			.uv(8, 0).cuboid(0.1f, 28.2f, 0.1f, 0.8f, 1.0f, 0.8f, new Dilation(0.0f))
			.uv(11, 0).cuboid(0.2f, 29.2f, 0.2f, 0.6f, 1.0f, 0.6f, new Dilation(0.0f))
			.uv(14, 0).cuboid(0.3f, 30.2f, 0.3f, 0.4f, 1.0f, 0.4f, new Dilation(0.0f))
			.uv(17, 0).cuboid(0.4f, 31.0f, 0.4f, 0.2f, 1.0f, 0.2f, new Dilation(0.0f)), ModelTransform.NONE);
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(WoodenSpearEntity var1, float var2, float var3, float var4, float var5, float var6) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.pole.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.tip.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

}