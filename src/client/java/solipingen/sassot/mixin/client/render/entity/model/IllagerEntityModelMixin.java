package solipingen.sassot.mixin.client.render.entity.model;

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
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.util.Arm;
import solipingen.sassot.item.SpearItem;


@Mixin(IllagerEntityModel.class)
@Environment(value = EnvType.CLIENT)
public abstract class IllagerEntityModelMixin<T extends IllagerEntity> {
    @Shadow @Final private ModelPart rightArm;
    @Shadow @Final private ModelPart leftArm;


    @Inject(method = "setAngles", at = @At("TAIL"))
    private void injectedSetAngles(T illagerEntity, float f, float g, float h, float i, float j, CallbackInfo cbi) {
        if (illagerEntity instanceof PillagerEntity && illagerEntity.isAttacking() && illagerEntity.getMainHandStack().getItem() instanceof SpearItem) {
            if (illagerEntity.getMainArm() == Arm.LEFT) {
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
