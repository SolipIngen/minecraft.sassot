package solipingen.sassot.mixin.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractSkeletonEntityRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.client.util.interfaces.mixin.render.LivingEntityRenderStateInterface;
import solipingen.sassot.client.util.interfaces.mixin.render.SkeletonEntityRenderStateInterface;


@Mixin(AbstractSkeletonEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class AbstractSkeletonEntityRendererMixin<T extends AbstractSkeletonEntity, S extends SkeletonEntityRenderState> extends BipedEntityRenderer<T, S, SkeletonEntityModel<S>> {

    public AbstractSkeletonEntityRendererMixin(EntityRendererFactory.Context context, SkeletonEntityModel<S> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/mob/AbstractSkeletonEntity;Lnet/minecraft/client/render/entity/state/SkeletonEntityRenderState;F)V",
        at = @At("TAIL"))
    private void injectedUpdateRenderState(T abstractSkeletonEntity, S skeletonEntityRenderState, float f, CallbackInfo cbi) {
        LivingEntityRenderStateInterface iLivingEntityState = (LivingEntityRenderStateInterface)skeletonEntityRenderState;
        ((SkeletonEntityRenderStateInterface)skeletonEntityRenderState).setIsThrowing(abstractSkeletonEntity.isAttacking() && (iLivingEntityState.getIsAttackingWithSpear() || iLivingEntityState.getIsAttackingWithBlazearm()));
    }

    @Inject(method = "getArmPose(Lnet/minecraft/entity/mob/AbstractSkeletonEntity;Lnet/minecraft/util/Arm;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;",
        at = @At("HEAD"), cancellable = true)
    private void injectedGetArmPose(AbstractSkeletonEntity abstractSkeletonEntity, Arm arm, CallbackInfoReturnable<BipedEntityModel.ArmPose> cbireturn) {
        ItemStack weaponStack = abstractSkeletonEntity.getWeaponStack();
        if (abstractSkeletonEntity.isAttacking() && (weaponStack.getItem() instanceof SpearItem || weaponStack.getItem() instanceof TridentItem || weaponStack.getItem() instanceof BlazearmItem)) {
            cbireturn.setReturnValue(abstractSkeletonEntity.getMainArm() == arm ? BipedEntityModel.ArmPose.THROW_SPEAR : BipedEntityModel.ArmPose.EMPTY);
        }
    }


}
