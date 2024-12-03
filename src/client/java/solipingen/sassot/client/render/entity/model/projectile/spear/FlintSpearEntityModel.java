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
import solipingen.sassot.entity.projectile.spear.FlintSpearEntity;


@Environment(EnvType.CLIENT)
public class FlintSpearEntityModel extends EntityModel<FlintSpearEntity> {
	private final ModelPart spear;


	public FlintSpearEntityModel(ModelPart root) {
		this.spear = root.getChild("spear");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spear = modelPartData.addChild("spear", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0f, -8.0f, 0.0f, 1.0f, 27.0f, 1.0f, new Dilation(0.0f))
			.uv(5, 7).cuboid(-1.0f, 19.0f, 0.0f, 1.0f, 1.2f, 1.0f, new Dilation(0.0f))
			.uv(5, 10).cuboid(-0.9f, 20.2f, 0.1f, 0.8f, 1.0f, 0.8f, new Dilation(0.0f))
			.uv(5, 13).cuboid(-0.8f, 21.2f, 0.2f, 0.6f, 1.0f, 0.6f, new Dilation(0.0f))
			.uv(5, 16).cuboid(-0.7f, 22.2f, 0.3f, 0.4f, 1.0f, 0.4f, new Dilation(0.0f))
			.uv(5, 19).cuboid(-0.6f, 23.0f, 0.4f, 0.2f, 1.0f, 0.2f, new Dilation(0.0f))
			.uv(6, 4).cuboid(-1.0f, 18.5f, -0.5f, 1.0f, 0.5f, 0.5f, new Dilation(0.0f))
			.uv(6, 4).cuboid(-1.0f, 18.5f, 1.0f, 1.0f, 0.5f, 0.5f, new Dilation(0.0f))
			.uv(6, 4).cuboid(0.0f, 18.5f, 0.0f, 0.5f, 0.5f, 1.0f, new Dilation(0.0f))
			.uv(6, 4).cuboid(-1.5f, 18.5f, 0.0f, 0.5f, 0.5f, 1.0f, new Dilation(0.0f)), ModelTransform.pivot(0.0f, 8.0f, 0.0f));
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	@Override
	public void setAngles(FlintSpearEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		this.spear.render(matrices, vertexConsumer, light, overlay, color);
	}
}