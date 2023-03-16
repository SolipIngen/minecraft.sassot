package solipingen.sassot.mixin.client.render.entity.model;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(SkeletonEntityModel.class)
@Environment(value=EnvType.CLIENT)
public abstract class SkeletonEntityModelMixin<T extends MobEntity> extends BipedEntityModel<T> {


    public SkeletonEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "animateModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private void injectedAnimateModel(T mobEntity, float f, float g, float h, CallbackInfo cbi) {
        ItemStack itemStack = ((LivingEntity)mobEntity).getMainHandStack();
        if ((itemStack.getItem() instanceof SpearItem || itemStack.getItem() instanceof BlazearmItem) && ((MobEntity)mobEntity).isAttacking()) {
            if (((MobEntity)mobEntity).getMainArm() == Arm.RIGHT) {
                this.rightArmPose = BipedEntityModel.ArmPose.THROW_SPEAR;
            } 
            else {
                this.leftArmPose = BipedEntityModel.ArmPose.THROW_SPEAR;
            }
        }
    }

    @Inject(method = "setAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private void injectedSetAngles(T mobEntity, float f, float g, float h, float i, float j, CallbackInfo cbi) {
        if (this.leftArmPose == BipedEntityModel.ArmPose.THROW_SPEAR) {
            this.leftArm.pitch = this.leftArm.pitch * 0.5f - (float)Math.PI;
            this.leftArm.yaw = 0.0f;
        }
        if (this.rightArmPose == BipedEntityModel.ArmPose.THROW_SPEAR) {
            this.rightArm.pitch = this.rightArm.pitch * 0.5f - (float)Math.PI;
            this.rightArm.yaw = 0.0f;
        }
    }

    @Redirect(method = "setAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedIsOf(ItemStack mainHandStack, Item originalItem) {
        return mainHandStack.getItem() instanceof BowItem || mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof BlazearmItem;
    }


    
}
