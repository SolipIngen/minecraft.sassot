package solipingen.sassot.mixin.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.WindBurstEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.enchantment.ModEnchantments;


@Mixin(WindBurstEnchantment.class)
public abstract class WindBurstEnchantmentMixin extends Enchantment {


    public WindBurstEnchantmentMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "isAvailableForRandomSelection", at = @At("HEAD"), cancellable = true)
    private void injectedIsAvailableForRandomSelection(CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(true);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != ModEnchantments.GROUNDSHAKING;
    }


}
