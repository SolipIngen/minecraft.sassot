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
import solipingen.sassot.entity.projectile.spear.GoldenSpearEntity;


@Environment(value = EnvType.CLIENT)
public class GoldenSpearEntityModel extends EntityModel<GoldenSpearEntity> {
	public static final Identifier TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/spear/golden_spear.png");
	private final ModelPart pole;
	private final ModelPart base;
	private final ModelPart support;
	private final ModelPart tip;

	
	public GoldenSpearEntityModel(ModelPart root) {
		this.pole = root.getChild("pole");
		this.base = root.getChild("base");
		this.support = root.getChild("support");
		this.tip = root.getChild("tip");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("pole", ModelPartBuilder.create().uv(0, 5).cuboid(0.0f, 0.0f, 0.0f, 1.0f, 27.0f, 1.0f, new Dilation(0.0f)), ModelTransform.NONE);
		modelPartData.addChild("base", ModelPartBuilder.create()
			.uv(5, 18).cuboid(0.0f, 0.0f, -0.5f, 1.0f, 3.0f, 0.5f, new Dilation(0.0f))
			.uv(9, 18).cuboid(0.0f, 0.0f, 1.0f, 1.0f, 3.0f, 0.5f, new Dilation(0.0f))
			.uv(13, 18).cuboid(-0.5f, 0.0f, 0.0f, 0.5f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(17, 18).cuboid(1.0f, 0.0f, 0.0f, 0.5f, 3.0f, 1.0f, new Dilation(0.0f)), ModelTransform.NONE);
 		modelPartData.addChild("support", ModelPartBuilder.create()
			.uv(5, 3).cuboid(0.0f, 26.5f, -0.2f, 1.0f, 0.5f, 0.2f, new Dilation(0.0f))
			.uv(8, 3).cuboid(0.0f, 26.5f, 1.0f, 1.0f, 0.5f, 0.2f, new Dilation(0.0f))
			.uv(11, 3).cuboid(-0.2f, 26.5f, 0.0f, 0.2f, 0.5f, 1.0f, new Dilation(0.0f))
			.uv(14, 3).cuboid(1.0f, 26.5f, 0.0f, 0.2f, 0.5f, 1.0f, new Dilation(0.0f)), ModelTransform.NONE);
		modelPartData.addChild("tip", ModelPartBuilder.create()
			.uv(28, 0).cuboid(-0.25f, 27.0f, -0.25f, 1.5f, 0.5f, 1.5f, new Dilation(0.0f))
			.uv(8, 0).cuboid(-0.125f, 27.5f, -0.125f, 1.25f, 1.0f, 1.25f, new Dilation(0.0f))
			.uv(11, 0).cuboid(0.0f, 28.5f, 0.0f, 1.0f, 1.0f, 1.0f, new Dilation(0.0f))
			.uv(14, 0).cuboid(0.2f, 29.5f, 0.2f, 0.6f, 1.0f, 0.6f, new Dilation(0.0f))
			.uv(17, 0).cuboid(0.3f, 30.5f, 0.3f, 0.4f, 1.0f, 0.4f, new Dilation(0.0f)), ModelTransform.NONE);
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(GoldenSpearEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.pole.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.support.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.tip.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
	
}