package solipingen.sassot.mixin.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.client.util.interfaces.mixin.render.LivingEntityRenderStateInterface;
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


@Mixin(LivingEntityRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements FeatureRendererContext<S, M>  {


    protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void injectedUpdateRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo cbi) {
        LivingEntityRenderStateInterface iLivingEntityRenderState = (LivingEntityRenderStateInterface)livingEntityRenderState;
        iLivingEntityRenderState.setIsPlayer(livingEntity instanceof PlayerEntity);
        iLivingEntityRenderState.setIsAttackingWithSpear(livingEntity.getWeaponStack().getItem() instanceof SpearItem || livingEntity.getWeaponStack().getItem() instanceof TridentItem);
        iLivingEntityRenderState.setIsAttackingWithBlazearm(livingEntity.getWeaponStack().getItem() instanceof BlazearmItem);
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isUsingRiptide()Z", shift = At.Shift.AFTER))
    private void injectedUpdateRenderStateRiptide(T livingEntity, S livingEntityRenderState, float f, CallbackInfo cbi) {
        LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
        LivingEntityRenderStateInterface iLivingEntityRenderState = (LivingEntityRenderStateInterface)livingEntityRenderState;
        iLivingEntityRenderState.setIsUsingWhirlwind(iLivingEntity.getIsUsingWhirlwind());
        iLivingEntityRenderState.setIsUsingFlare(iLivingEntity.getIsUsingFlare());
        iLivingEntityRenderState.setIsUsingTridentRiptide(iLivingEntity.getIsUsingTridentRiptide());
        iLivingEntityRenderState.setIsBeingSnowedOn(((EntityInterface)livingEntity).isBeingSnowedOn());
        iLivingEntityRenderState.setInPowderSnow(livingEntity.inPowderSnow);
    }


}
