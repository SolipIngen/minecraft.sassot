package solipingen.sassot.mixin.entity.ai.goal;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalMixin extends Goal {
    @Shadow @Final protected PathAwareEntity mob;
    @Shadow @Final private boolean pauseWhenMobIdle;
    @Shadow private double targetX;
    @Shadow private double targetY;
    @Shadow private double targetZ;
    @Shadow private int updateCountdownTicks;
    @Shadow private int cooldown;

    @Invoker("attack")
    public abstract void invokeAttack(LivingEntity target, double squaredDistance);


    @Inject(method = "getSquaredMaxAttackDistance", at = @At("HEAD"), cancellable = true)
    private void injectedGetSquaredMaxAttackDistance(LivingEntity target, CallbackInfoReturnable<Double> cbireturn) {
        ItemStack mainHandStack = this.mob.getMainHandStack();
        if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
            cbireturn.setReturnValue((double)MathHelper.square(this.mob.getWidth()*2.0f + 0.5f) + target.getWidth());
        }
        else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
            cbireturn.setReturnValue((double)MathHelper.square(this.mob.getWidth()*2.0f + 1.0f) + target.getWidth());
        }
    }



}
