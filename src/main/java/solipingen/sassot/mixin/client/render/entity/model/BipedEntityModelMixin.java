package solipingen.sassot.mixin.client.render.entity.model;

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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(BipedEntityModel.class)
@Environment(value = EnvType.CLIENT)
public abstract class BipedEntityModelMixin<T extends LivingEntity> {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart hat;
    @Shadow @Final public ModelPart body;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightLeg;
    @Shadow @Final public ModelPart leftLeg;
    
    @Invoker("getArm")
    public abstract ModelPart invokeGetArm(Arm arm);

    @Invoker("getPreferredArm")
    public abstract Arm invokeGetPreferredArm(T entity);


    @Inject(method = "animateArms", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;getPreferredArm(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/util/Arm;"), cancellable = true)
    private void injectedAnimateArms(T entity, float animationProgress, CallbackInfo cbi) {
        if (entity instanceof PlayerEntity && (entity.getMainHandStack().getItem() instanceof SpearItem || entity.getMainHandStack().getItem() instanceof TridentItem || entity.getMainHandStack().getItem() instanceof BlazearmItem)) {
            if (((BipedEntityModel<?>)(Object)this).handSwingProgress > 0.0f) {
                Arm arm = this.invokeGetPreferredArm(entity);
                ModelPart modelPart = this.invokeGetArm(arm);
                float f = ((BipedEntityModel<?>)(Object)this).handSwingProgress < 0.33f ? 0.33f : ((BipedEntityModel<?>)(Object)this).handSwingProgress;
                this.body.yaw = MathHelper.sin(MathHelper.sqrt(f) * ((float)Math.PI * 2)) * 0.1f;
                if (arm == Arm.LEFT) {
                    this.body.yaw *= -1.0f;
                }
                this.rightArm.pivotZ = MathHelper.sin(this.body.yaw) * 5.0f;
                this.rightArm.pivotX = -MathHelper.cos(this.body.yaw) * 5.0f;
                this.leftArm.pivotZ = -MathHelper.sin(this.body.yaw) * 5.0f;
                this.leftArm.pivotX = MathHelper.cos(this.body.yaw) * 5.0f;
                this.rightArm.yaw += this.body.yaw;
                this.leftArm.yaw += this.body.yaw;
                this.leftArm.pitch += this.body.yaw;
                f = 1.0f - f;
                f *= f;
                f *= f;
                f = 1.0f - f;
                float g = MathHelper.sin(f * (float)Math.PI);
                float h = MathHelper.sin(((BipedEntityModel<?>)(Object)this).handSwingProgress * (float)Math.PI) * -(this.head.pitch - 0.7f) * 0.75f;
                modelPart.pitch -= g * 1.2f + h;
                modelPart.yaw -= this.body.yaw * 0.25f;
                modelPart.roll += MathHelper.sin(((BipedEntityModel<?>)(Object)this).handSwingProgress * (float)Math.PI) * -0.4f;
                cbi.cancel();
            }
        }
    }


}
