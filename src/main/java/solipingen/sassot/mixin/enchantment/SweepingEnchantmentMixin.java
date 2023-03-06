package solipingen.sassot.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.SweepingEnchantment;
import net.minecraft.entity.EquipmentSlot;


@Mixin(SweepingEnchantment.class)
public abstract class SweepingEnchantmentMixin extends Enchantment {


    protected SweepingEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Inject(method = "getMultiplier", at = @At("HEAD"), cancellable = true)
    private static void injectedGetMultiplier(int level, CallbackInfoReturnable<Float> cbireturn) {
        float sweepingMultiplier = 1.0f/3.0f + 0.2f*level;
        cbireturn.setReturnValue(sweepingMultiplier);
    }

    
}
