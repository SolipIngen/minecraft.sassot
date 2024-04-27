package solipingen.sassot.client.render.entity.model.projectile;

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
import solipingen.sassot.entity.projectile.BlazearmEntity;


@Environment(value = EnvType.CLIENT)
public class BlazearmEntityModel extends EntityModel<BlazearmEntity> {
	private final ModelPart blazearm;


	public BlazearmEntityModel(ModelPart root) {
		this.blazearm = root.getChild("blazearm");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("blazearm", ModelPartBuilder.create().uv(0, 0).cuboid(0.0f, 0.0f, 0.0f, 1.0f, 24.0f, 1.0f, new Dilation(0.0f))
			.uv(5, 11).cuboid(-0.25f, 24.0f, -0.25f, 1.5f, 0.5f, 1.5f, new Dilation(0.0f))
			.uv(9, 8).cuboid(0.25f, 24.5f, -0.25f, 0.5f, 1.0f, 1.25f, new Dilation(0.0f))
			.uv(13, 8).cuboid(0.25f, 25.5f, -0.5f, 0.5f, 1.0f, 1.35f, new Dilation(0.0f))
			.uv(18, 8).cuboid(0.25f, 26.5f, -0.75f, 0.5f, 1.0f, 1.5f, new Dilation(0.0f))
			.uv(5, 0).cuboid(0.375f, 30.25f, 0.75f, 0.25f, 0.5f, 0.4f, new Dilation(0.0f))
			.uv(10, 0).cuboid(0.25f, 27.5f, -0.5f, 0.5f, 1.0f, 1.35f, new Dilation(0.0f))
			.uv(15, 0).cuboid(0.3f, 28.5f, -0.25f, 0.4f, 1.0f, 1.25f, new Dilation(0.0f))
			.uv(20, 0).cuboid(0.35f, 29.5f, 0.125f, 0.3f, 0.75f, 1.0f, new Dilation(0.0f))
			.uv(5, 15).cuboid(0.0f, 23.5f, -0.2f, 1.0f, 0.5f, 0.2f, new Dilation(0.0f))
			.uv(8, 15).cuboid(0.0f, 23.5f, 1.0f, 1.0f, 0.5f, 0.2f, new Dilation(0.0f))
			.uv(11, 15).cuboid(-0.2f, 23.5f, 0.0f, 0.2f, 0.5f, 1.0f, new Dilation(0.0f))
			.uv(14, 15).cuboid(1.0f, 23.5f, 0.0f, 0.2f, 0.5f, 1.0f, new Dilation(0.0f))
			.uv(5, 18).cuboid(0.0f, 0.0f, -0.5f, 1.0f, 3.0f, 0.5f, new Dilation(0.0f))
			.uv(10, 18).cuboid(0.0f,0.0f, 1.0f, 1.0f, 3.0f, 0.5f, new Dilation(0.0f))
			.uv(17, 19).cuboid(-0.5f, 0.0f, 0.0f, 0.5f, 3.0f, 1.0f, new Dilation(0.0f))
			.uv(17, 18).cuboid(1.0f, 0.0f, 0.0f, 0.5f, 3.0f, 1.0f, new Dilation(0.0f)), ModelTransform.pivot(0.0f, 0.0f, 8.0f));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(BlazearmEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.blazearm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
	
}