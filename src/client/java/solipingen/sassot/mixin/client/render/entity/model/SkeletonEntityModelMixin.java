package solipingen.sassot.mixin.client.render.entity.model;

import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import solipingen.sassot.client.util.interfaces.mixin.render.SkeletonEntityRenderStateInterface;


@Mixin(SkeletonEntityModel.class)
@Environment(value = EnvType.CLIENT)
public abstract class SkeletonEntityModelMixin<S extends SkeletonEntityRenderState> extends BipedEntityModel<S> {


    public SkeletonEntityModelMixin(ModelPart root) {
        super(root);
    }

//    @Inject(method = "animateModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
//    private void injectedAnimateModel(T mobEntity, float f, float g, float h, CallbackInfo cbi) {
//        ItemStack itemStack = ((LivingEntity)mobEntity).getMainHandStack();
//        if ((itemStack.getItem() instanceof SpearItem || itemStack.getItem() instanceof BlazearmItem) && ((MobEntity)mobEntity).isAttacking()) {
//            if (((MobEntity)mobEntity).getMainArm() == Arm.RIGHT) {
//                this.rightArmPose = BipedEntityModel.ArmPose.THROW_SPEAR;
//            }
//            else {
//                this.leftArmPose = BipedEntityModel.ArmPose.THROW_SPEAR;
//            }
//        }
//    }

    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/SkeletonEntityRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V",
                shift = At.Shift.AFTER), cancellable = true)
    private void injectedSetAngles(S skeletonEntityRenderState, CallbackInfo cbi) {
        SkeletonEntityRenderStateInterface iSkeletonState = (SkeletonEntityRenderStateInterface)skeletonEntityRenderState;
        if (iSkeletonState.getIsThrowing()) {
            if (skeletonEntityRenderState.leftArmPose == BipedEntityModel.ArmPose.THROW_SPEAR) {
                this.leftArm.pitch = this.leftArm.pitch*0.5f - (float)Math.PI;
                this.leftArm.yaw = 0.0f;
            }
            if (skeletonEntityRenderState.rightArmPose == BipedEntityModel.ArmPose.THROW_SPEAR) {
                this.rightArm.pitch = this.rightArm.pitch*0.5f - (float)Math.PI;
                this.rightArm.yaw = 0.0f;
            }
            cbi.cancel();
        }
    }


    
}
