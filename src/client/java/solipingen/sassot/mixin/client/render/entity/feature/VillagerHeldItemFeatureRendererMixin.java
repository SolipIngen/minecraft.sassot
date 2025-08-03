package solipingen.sassot.mixin.client.render.entity.feature;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.entity.state.VillagerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.client.resource.ModClientResourcePacks;
import solipingen.sassot.client.util.interfaces.mixin.render.LivingEntityRenderStateInterface;
import solipingen.sassot.village.ModVillagerProfessions;


@Mixin(VillagerHeldItemFeatureRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class VillagerHeldItemFeatureRendererMixin<S extends ItemHolderEntityRenderState, M extends EntityModel<S> & ModelWithHat> extends FeatureRenderer<S, M> {


    public VillagerHeldItemFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Inject(method = "applyTransforms", at = @At("TAIL"))
    private void injecedtApplyTransforms(S state, MatrixStack matrices, CallbackInfo cbi) {
        if (state instanceof VillagerEntityRenderState villagerRenderState && villagerRenderState.getVillagerData() != null) {
            VillagerProfession profession = villagerRenderState.getVillagerData().profession().value();
            if (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(47.0f));
                matrices.translate(0.0, profession == ModVillagerProfessions.SPEARMAN ? 0.5 : 0.0, -0.2);
                if (ModClientResourcePacks.isFreshAnimationsEnabled()) {
                    EntityModel<S> entityModel = this.getContextModel();
                    if (entityModel.getParts().stream().anyMatch(modelPart -> modelPart.hasChild("arms"))) {
                        ModelPart freshArmsModel = entityModel.getParts().stream().findAny().orElseThrow().getChild("arms");
                        matrices.translate(0.0, -0.05*freshArmsModel.originY
//                                + (!livingEntity.isOnGround() && livingEntity.isTouchingWater() ? 0.2 : 0.1)
                                        + 0.1,
//                                !livingEntity.isOnGround() && livingEntity.isTouchingWater() ? 0.55 : 0.05
                                0.05);
                        matrices.multiply(RotationAxis.POSITIVE_X.rotation(freshArmsModel.pitch));
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(freshArmsModel.yaw));
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(freshArmsModel.roll));
                    }
                }
            }
        }
    }

    
}
