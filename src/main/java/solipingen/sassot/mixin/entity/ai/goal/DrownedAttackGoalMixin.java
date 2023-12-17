package solipingen.sassot.mixin.entity.ai.goal;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Items;


@Mixin(targets = "net.minecraft.entity.mob.DrownedEntity$DrownedAttackGoal")
public abstract class DrownedAttackGoalMixin extends ZombieAttackGoal {
    @Shadow @Final private DrownedEntity drowned;


    public DrownedAttackGoalMixin(ZombieEntity zombie, double speed, boolean pauseWhenMobIdle) {
        super(zombie, speed, pauseWhenMobIdle);
    }

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    private void injectedCanStartContinue(CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.drowned.getMainHandStack().isOf(Items.TRIDENT) && this.drowned.getTarget() != null) {
            LivingEntity target = this.drowned.getTarget();
            cbireturn.setReturnValue(super.canStart() && this.drowned.isInAttackRange(target));
        }
    }

    @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
    private void injectedShouldContinue(CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.drowned.getMainHandStack().isOf(Items.TRIDENT) && this.drowned.getTarget() != null) {
            LivingEntity target = this.drowned.getTarget();
            cbireturn.setReturnValue(super.canStart() && this.drowned.isInAttackRange(target));
        }
    }


}
