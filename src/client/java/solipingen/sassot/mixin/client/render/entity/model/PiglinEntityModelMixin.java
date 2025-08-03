package solipingen.sassot.mixin.client.render.entity.model;

import net.minecraft.client.render.entity.model.PiglinBaseEntityModel;
import net.minecraft.client.render.entity.state.PiglinEntityRenderState;
import net.minecraft.entity.mob.PiglinActivity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PiglinEntityModel;
import solipingen.sassot.client.util.interfaces.mixin.render.LivingEntityRenderStateInterface;


@Mixin(PiglinEntityModel.class)
@Environment(value = EnvType.CLIENT)
public abstract class PiglinEntityModelMixin extends PiglinBaseEntityModel<PiglinEntityRenderState> {


    public PiglinEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "rotateMainArm", at = @At("HEAD"), cancellable = true)
    private void injectedRotateMainArm(PiglinEntityRenderState state, CallbackInfo cbi) {
        if (state.activity == PiglinActivity.ATTACKING_WITH_MELEE_WEAPON && ((LivingEntityRenderStateInterface)state).getIsAttackingWithSpear() || ((LivingEntityRenderStateInterface)state).getIsAttackingWithBlazearm()) {
            if (state.mainArm == Arm.LEFT) {
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
