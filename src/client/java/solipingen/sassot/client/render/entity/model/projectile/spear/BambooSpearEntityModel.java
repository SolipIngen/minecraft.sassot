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
import solipingen.sassot.entity.projectile.spear.BambooSpearEntity;


@Environment(EnvType.CLIENT)
public class BambooSpearEntityModel extends EntityModel<BambooSpearEntity> {
	private final ModelPart pole;
	private final ModelPart tip;


	public BambooSpearEntityModel(ModelPart root) {
		this.pole = root.getChild("pole");
		this.tip = root.getChild("tip");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData pole = modelPartData.addChild("pole", ModelPartBuilder.create().uv(0, 0).cuboid(-9.0f, 0.0f, 8.0f, 1.0f, 27.0f, 1.0f, new Dilation(0.0f)), ModelTransform.pivot(8.0f, 0.0f, -8.0f));

		ModelPartData tip = modelPartData.addChild("tip", ModelPartBuilder.create().uv(6, 5).cuboid(-1.0f, 27.0f, 0.0f, 1.0f, 1.2f, 1.0f, new Dilation(0.0f))
			.uv(6, 10).cuboid(-1.0f, 28.2f, 0.0f, 1.0f, 1.0f, 0.8f, new Dilation(0.0f))
			.uv(6, 14).cuboid(-1.0f, 29.2f, 0.0f, 1.0f, 1.0f, 0.6f, new Dilation(0.0f))
			.uv(6, 18).cuboid(-1.0f, 30.2f, 0.0f, 1.0f, 1.0f, 0.4f, new Dilation(0.0f))
			.uv(6, 21).cuboid(-1.0f, 31.0f, 0.0f, 1.0f, 1.0f, 0.2f, new Dilation(0.0f)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(BambooSpearEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.pole.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.tip.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}