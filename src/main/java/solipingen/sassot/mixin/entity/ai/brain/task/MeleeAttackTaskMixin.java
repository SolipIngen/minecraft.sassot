package solipingen.sassot.mixin.entity.ai.brain.task;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.ai.brain.task.MeleeAttackTask;
import net.minecraft.entity.mob.MobEntity;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;


@Mixin(MeleeAttackTask.class)
public abstract class MeleeAttackTaskMixin {
    

    @Inject(method = "isHoldingUsableRangedWeapon", at = @At("HEAD"), cancellable = true)
    private static void injectedIsHoldingUsableRangedWeapon(MobEntity mob, CallbackInfoReturnable<Boolean> cbireturn) {
        boolean spearBl = mob.getMainHandStack().getItem() instanceof SpearItem || mob.isHolding(ModItems.BLAZEARM);
        if (spearBl) {
            cbireturn.setReturnValue(spearBl);
        }
    }


}
