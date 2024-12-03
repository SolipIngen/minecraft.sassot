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
import solipingen.sassot.entity.projectile.spear.CopperSpearEntity;


@Environment(EnvType.CLIENT)
public class CopperSpearEntityModel extends EntityModel<CopperSpearEntity> {
	private final ModelPart pole;
	private final ModelPart base;
	private final ModelPart support;
	private final ModelPart tip;
	
	
	public CopperSpearEntityModel(ModelPart root) {
		this.pole = root.getChild("pole");
		this.base = root.getChild("base");
		this.support = root.getChild("support");
		this.tip = root.getChild("tip");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData pole = modelPartData.addChild("pole", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0f, 0.0f, 0.0f, 1.0f, 27.0f, 1.0f, new Dilation(0.0f)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

		ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(6, 19).cuboid(-1.0f, 0.0f, -0.5f, 1.0f, 3.0f, 0.5f, new Dilation(0.0f))
			.uv(6, 19).cuboid(-1.0f, 0.0f, 1.0f, 1.0f, 3.0f, 0.5f, new Dilation(0.0f))
			.uv(6, 19).cuboid(0.0f, 0.0f, 0.0f, 0.5f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(6, 19).cuboid(-1.5f, 0.0f, 0.0f, 0.5f, 3.0f, 1.0f, new Dilation(0.0f)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

		ModelPartData support = modelPartData.addChild("support", ModelPartBuilder.create().uv(5, 4).cuboid(-1.0f, 26.5f, -0.2f, 1.0f, 0.5f, 0.2f, new Dilation(0.0f))
			.uv(5, 4).cuboid(-1.0f, 26.5f, 1.0f, 1.0f, 0.5f, 0.2f, new Dilation(0.0f))
			.uv(5, 4).cuboid(0.0f, 26.5f, 0.0f, 0.2f, 0.5f, 1.0f, new Dilation(0.0f))
			.uv(5, 4).cuboid(-1.2f, 26.5f, 0.0f, 0.2f, 0.5f, 1.0f, new Dilation(0.0f)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

		ModelPartData tip = modelPartData.addChild("tip", ModelPartBuilder.create().uv(5, 7).cuboid(-1.0f, 27.0f, 0.0f, 1.0f, 1.2f, 1.0f, new Dilation(0.0f))
			.uv(10, 7).cuboid(-0.9f, 28.2f, 0.1f, 0.8f, 1.0f, 0.8f, new Dilation(0.0f))
			.uv(15, 7).cuboid(-0.8f, 29.2f, 0.2f, 0.6f, 1.0f, 0.6f, new Dilation(0.0f))
			.uv(19, 7).cuboid(-0.7f, 30.2f, 0.3f, 0.4f, 1.0f, 0.4f, new Dilation(0.0f))
			.uv(0, 0).cuboid(-0.6f, 31.0f, 0.4f, 0.2f, 1.0f, 0.2f, new Dilation(0.0f)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	@Override
	public void setAngles(CopperSpearEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		this.pole.render(matrices, vertexConsumer, light, overlay, color);
		this.base.render(matrices, vertexConsumer, light, overlay, color);
		this.support.render(matrices, vertexConsumer, light, overlay, color);
		this.tip.render(matrices, vertexConsumer, light, overlay, color);
	}
}