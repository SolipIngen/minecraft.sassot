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
import solipingen.sassot.entity.projectile.spear.NetheriteSpearEntity;


@Environment(EnvType.CLIENT)
public class NetheriteSpearEntityModel extends EntityModel<NetheriteSpearEntity> {
	private final ModelPart pole;
	private final ModelPart base;
	private final ModelPart support;
	private final ModelPart tip;

	public NetheriteSpearEntityModel(ModelPart root) {
		this.pole = root.getChild("pole");
		this.base = root.getChild("base");
		this.support = root.getChild("support");
		this.tip = root.getChild("tip");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData pole = modelPartData.addChild("pole", ModelPartBuilder.create().uv(0, 0).cuboid(7.0F, -8.0F, -8.0F, 1.0F, 27.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.0F, 8.0F, 8.0F));

		ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(6, 19).cuboid(-1.0F, 0.0F, -0.5F, 1.0F, 3.0F, 0.5F, new Dilation(0.0F))
			.uv(6, 19).cuboid(-1.0F, 0.0F, 1.0F, 1.0F, 3.0F, 0.5F, new Dilation(0.0F))
			.uv(6, 19).cuboid(0.0F, 0.0F, 0.0F, 0.5F, 3.0F, 1.0F, new Dilation(0.0F))
			.uv(6, 19).cuboid(-1.5F, 0.0F, 0.0F, 0.5F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData support = modelPartData.addChild("support", ModelPartBuilder.create().uv(7, 4).cuboid(-1.0F, 26.5F, -0.2F, 1.0F, 0.5F, 0.2F, new Dilation(0.0F))
			.uv(7, 4).cuboid(-1.0F, 26.5F, 1.0F, 1.0F, 0.5F, 0.2F, new Dilation(0.0F))
			.uv(7, 4).cuboid(0.0F, 26.5F, 0.0F, 0.2F, 0.5F, 1.0F, new Dilation(0.0F))
			.uv(7, 4).cuboid(-1.2F, 26.5F, 0.0F, 0.2F, 0.5F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData tip = modelPartData.addChild("tip", ModelPartBuilder.create().uv(5, 7).cuboid(-1.25F, 27.0F, -0.25F, 1.5F, 0.5F, 1.5F, new Dilation(0.0F))
			.uv(12, 7).cuboid(-1.125F, 27.5F, -0.125F, 1.25F, 1.0F, 1.25F, new Dilation(0.0F))
			.uv(18, 7).cuboid(-1.0F, 28.5F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
			.uv(23, 7).cuboid(-0.8F, 29.5F, 0.2F, 0.6F, 1.0F, 0.6F, new Dilation(0.0F))
			.uv(27, 7).cuboid(-0.7F, 30.5F, 0.3F, 0.4F, 1.0F, 0.4F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(NetheriteSpearEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.pole.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.support.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.tip.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}