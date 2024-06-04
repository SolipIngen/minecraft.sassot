// Made with Blockbench 4.10.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package solipingen.sassot.client.render.entity.model.projectile.spear;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import solipingen.sassot.entity.projectile.spear.StoneSpearEntity;


@Environment(EnvType.CLIENT)
public class StoneSpearEntityModel extends EntityModel<StoneSpearEntity> {
	private final ModelPart pole;
	private final ModelPart support;
	private final ModelPart tip;


	public StoneSpearEntityModel(ModelPart root) {
		this.pole = root.getChild("pole");
		this.support = root.getChild("support");
		this.tip = root.getChild("tip");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData pole = modelPartData.addChild("pole", ModelPartBuilder.create().uv(0, 0).cuboid(7.0f, -8.0f, -8.0f, 1.0f, 27.0f, 1.0f, new Dilation(0.0f)), ModelTransform.pivot(-8.0f, 8.0f, 8.0f));

		ModelPartData support = modelPartData.addChild("support", ModelPartBuilder.create().uv(5, 3).cuboid(-1.0f, 26.5f, -0.5f, 1.0f, 0.5f, 0.5f, new Dilation(0.0f))
			.uv(5, 3).cuboid(-1.0f, 26.5f, 1.0f, 1.0f, 0.5f, 0.5f, new Dilation(0.0f))
			.uv(5, 3).cuboid(0.0f, 26.5f, 0.0f, 0.5f, 0.5f, 1.0f, new Dilation(0.0f))
			.uv(5, 3).cuboid(-1.5f, 26.5f, 0.0f, 0.5f, 0.5f, 1.0f, new Dilation(0.0f)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

		ModelPartData tip = modelPartData.addChild("tip", ModelPartBuilder.create().uv(5, 7).cuboid(-1.0f, 27.0f, 0.0f, 1.0f, 1.2f, 1.0f, new Dilation(0.0f))
			.uv(5, 11).cuboid(-0.9f, 28.2f, 0.1f, 0.8f, 1.0f, 0.8f, new Dilation(0.0f))
			.uv(5, 14).cuboid(-0.8f, 29.2f, 0.2f, 0.6f, 1.0f, 0.6f, new Dilation(0.0f))
			.uv(5, 17).cuboid(-0.7f, 30.2f, 0.3f, 0.4f, 1.0f, 0.4f, new Dilation(0.0f))
			.uv(5, 20).cuboid(-0.6f, 31.0f, 0.4f, 0.2f, 1.0f, 0.2f, new Dilation(0.0f)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(StoneSpearEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.pole.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.support.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.tip.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}