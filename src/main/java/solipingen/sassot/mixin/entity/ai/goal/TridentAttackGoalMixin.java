package solipingen.sassot.mixin.entity.ai.goal;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;


@Mixin(targets = "net.minecraft.entity.mob.DrownedEntity$TridentAttackGoal")
public abstract class TridentAttackGoalMixin extends ProjectileAttackGoal {
    @Shadow @Final private DrownedEntity drowned;


    public TridentAttackGoalMixin(RangedAttackMob mob, double mobSpeed, int intervalTicks, float maxShootRange) {
        super(mob, mobSpeed, intervalTicks, maxShootRange);
    }

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    private void injectedCanStart(CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.drowned.getTarget() != null) {
            LivingEntity target = this.drowned.getTarget();
            if (target instanceof PlayerEntity && (target.isSpectator() || ((PlayerEntity)target).isCreative())) {
                cbireturn.setReturnValue(false);
            }
            else {
                cbireturn.setReturnValue(super.canStart()
                        && this.drowned.getMainHandStack().isOf(Items.TRIDENT) && this.drowned.canDrownedAttackTarget(this.drowned.getTarget()) && !this.drowned.isInAttackRange(target));
            }
        }
    }

    @Inject(method = "stop", at = @At("HEAD"), cancellable = true)
    private void injectedStop(CallbackInfo cbi) {
        if (this.drowned.getTarget() != null && this.drowned.isInAttackRange(this.drowned.getTarget())) {
            LivingEntity target = this.drowned.getTarget();
            super.stop();
            this.drowned.clearActiveItem();
            this.drowned.setTarget(target);
            cbi.cancel();
        }
    }


    
}
