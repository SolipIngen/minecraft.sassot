package solipingen.sassot.mixin.client.render.entity.model;

import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.util.Arm;
import solipingen.sassot.client.util.interfaces.mixin.render.LivingEntityRenderStateInterface;


@Mixin(IllagerEntityModel.class)
@Environment(value = EnvType.CLIENT)
public abstract class IllagerEntityModelMixin<S extends IllagerEntityRenderState> extends EntityModel<S> implements ModelWithArms, ModelWithHead {
    @Shadow @Final private ModelPart rightArm;
    @Shadow @Final private ModelPart leftArm;


    protected IllagerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/IllagerEntityRenderState;)V", at = @At("TAIL"))
    private void injectedSetAngles(S illagerEntityRenderState, CallbackInfo cbi) {
        if (illagerEntityRenderState.attacking && ((LivingEntityRenderStateInterface)illagerEntityRenderState).getIsAttackingWithSpear()) {
            if (illagerEntityRenderState.mainArm == Arm.LEFT) {
                this.leftArm.pitch = this.leftArm.pitch * 0.5f - (float)Math.PI;
                this.leftArm.yaw = 0.0f;
            }
            else {
                this.rightArm.pitch = this.rightArm.pitch * 0.5f - (float)Math.PI;
                this.rightArm.yaw = 0.0f;
            }
        }
    }    



}
