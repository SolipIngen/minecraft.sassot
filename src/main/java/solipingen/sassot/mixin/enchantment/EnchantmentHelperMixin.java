package solipingen.sassot.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {


    @Inject(method = "getSweepingMultiplier(I)F", at = @At("HEAD"), cancellable = true)
    private static void injectedGetSweepingMultiplier(int level, CallbackInfoReturnable<Float> cbireturn) {
        float sweepingMultiplier = 1.0f/3.0f + 0.2f*level;
        cbireturn.setReturnValue(sweepingMultiplier);
    }
    
    
}
