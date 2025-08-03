package solipingen.sassot.mixin.client.render.entity.model;

import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.client.util.interfaces.mixin.render.LivingEntityRenderStateInterface;


@Mixin(BipedEntityModel.class)
@Environment(value = EnvType.CLIENT)
public abstract class BipedEntityModelMixin<T extends BipedEntityRenderState> extends EntityModel<T> implements ModelWithArms, ModelWithHead {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart hat;
    @Shadow @Final public ModelPart body;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightLeg;
    @Shadow @Final public ModelPart leftLeg;


    protected BipedEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Invoker("getArm")
    public abstract ModelPart invokeGetArm(Arm arm);


    @Inject(method = "animateArms", at = @At("HEAD"), cancellable = true)
    private void injectedAnimateArms(T state, float animationProgress, CallbackInfo cbi) {
        LivingEntityRenderStateInterface iState = (LivingEntityRenderStateInterface)state;
        if (iState.isPlayer() && (iState.getIsAttackingWithSpear() || iState.getIsAttackingWithBlazearm())) {
            if (state.handSwingProgress > 0.0f) {
                Arm arm = state.preferredArm;
                ModelPart modelPart = this.invokeGetArm(arm);
                float f = Math.max(state.handSwingProgress, 0.33f);
                this.body.yaw = MathHelper.sin(MathHelper.sqrt(f) * ((float)Math.PI * 2)) * 0.1f;
                if (arm == Arm.LEFT) {
                    this.body.yaw *= -1.0f;
                }
                this.rightArm.originZ = MathHelper.sin(this.body.yaw) * 5.0f;
                this.rightArm.originX = -MathHelper.cos(this.body.yaw) * 5.0f;
                this.leftArm.originZ = -MathHelper.sin(this.body.yaw) * 5.0f;
                this.leftArm.originX = MathHelper.cos(this.body.yaw) * 5.0f;
                this.rightArm.yaw += this.body.yaw;
                this.leftArm.yaw += this.body.yaw;
                this.leftArm.pitch += this.body.yaw;
                f = 1.0f - f;
                f *= f;
                f *= f;
                f = 1.0f - f;
                float g = MathHelper.sin(f * (float)Math.PI);
                float h = MathHelper.sin(state.handSwingProgress * (float)Math.PI) * -(this.head.pitch - 0.7f) * 0.75f;
                modelPart.pitch -= g * 1.2f + h;
                modelPart.yaw -= this.body.yaw * 0.25f;
                modelPart.roll += MathHelper.sin(state.handSwingProgress * (float)Math.PI) * -0.4f;
                cbi.cancel();
            }
        }
    }


}
