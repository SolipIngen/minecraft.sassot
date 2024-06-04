package solipingen.sassot.mixin.client.render.entity.model;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PiglinEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.mob.MobEntity;
import solipingen.sassot.item.SpearItem;


@Mixin(PiglinEntityModel.class)
@Environment(value = EnvType.CLIENT)
public abstract class PiglinEntityModelMixin<T extends MobEntity> extends PlayerEntityModel<T> {


    public PiglinEntityModelMixin(ModelPart root, boolean thinArms) {
        super(root, thinArms);
    }

    @Inject(method = "rotateMainArm", at = @At("HEAD"), cancellable = true)
    private void injectedRotateMainArm(T entity, CallbackInfo cbi) {
        if (entity.getMainHandStack().getItem() instanceof SpearItem) {
            if (((MobEntity)entity).isLeftHanded()) {
                this.leftArm.pitch = this.leftArm.pitch * 0.5f - (float)Math.PI;
                this.leftArm.yaw = 0.0f;
            } 
            else {
                this.rightArm.pitch = this.rightArm.pitch * 0.5f - (float)Math.PI;
                this.rightArm.yaw = 0.0f;
            }
            cbi.cancel();
        }
    }
    
    
}
