// Made with Blockbench 4.10.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package solipingen.sassot.client.render.entity.model.projectile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import solipingen.sassot.entity.projectile.BlazearmEntity;


@Environment(EnvType.CLIENT)
public class BlazearmEntityModel extends EntityModel<BlazearmEntity> {
	private final ModelPart spear;
	
	
	public BlazearmEntityModel(ModelPart root) {
		this.spear = root.getChild("spear");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spear = modelPartData.addChild("spear", ModelPartBuilder.create().uv(0, 0).cuboid(7.0f, -8.0f, -8.0f, 1.0f, 24.0f, 1.0f, new Dilation(0.0f))
			.uv(5, 25).cuboid(6.75f, 16.0f, -8.25f, 1.5f, 0.5f, 1.5f, new Dilation(0.0f))
			.uv(9, 9).cuboid(7.25f, 16.5f, -8.25f, 0.5f, 1.0f, 1.25f, new Dilation(0.0f))
			.uv(13, 11).cuboid(7.25f, 17.5f, -8.5f, 0.5f, 1.0f, 1.35f, new Dilation(0.0f))
			.uv(18, 8).cuboid(7.25f, 18.5f, -8.75f, 0.5f, 1.0f, 1.5f, new Dilation(0.0f))
			.uv(6, 1).cuboid(7.375f, 22.25f, -7.25f, 0.25f, 0.5f, 0.4f, new Dilation(0.0f))
			.uv(9, 2).cuboid(7.25f, 19.5f, -8.5f, 0.5f, 1.0f, 1.35f, new Dilation(0.0f))
			.uv(14, 1).cuboid(7.3f, 20.5f, -8.25f, 0.4f, 1.0f, 1.25f, new Dilation(0.0f))
			.uv(19, 3).cuboid(7.35f, 21.5f, -7.875f, 0.3f, 0.75f, 1.0f, new Dilation(0.0f))
			.uv(6, 16).cuboid(7.0f, 15.5f, -8.2f, 1.0f, 0.5f, 0.2f, new Dilation(0.0f))
			.uv(6, 16).cuboid(7.0f, 15.5f, -7.0f, 1.0f, 0.5f, 0.2f, new Dilation(0.0f))
			.uv(6, 16).cuboid(8.0f, 15.5f, -8.0f, 0.2f, 0.5f, 1.0f, new Dilation(0.0f))
			.uv(6, 16).cuboid(6.8f, 15.5f, -8.0f, 0.2f, 0.5f, 1.0f, new Dilation(0.0f))
			.uv(6, 20).cuboid(7.0f, -8.0f, -8.5f, 1.0f, 3.0f, 0.5f, new Dilation(0.0f))
			.uv(6, 20).cuboid(7.0f, -8.0f, -7.0f, 1.0f, 3.0f, 0.5f, new Dilation(0.0f))
			.uv(6, 20).cuboid(8.0f, -8.0f, -8.0f, 0.5f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(6, 20).cuboid(6.5f, -8.0f, -8.0f, 0.5f, 3.0f, 1.0f, new Dilation(0.0f)), ModelTransform.pivot(-8.0f, 8.0f, 8.0f));
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	@Override
	public void setAngles(BlazearmEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.spear.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
	
}