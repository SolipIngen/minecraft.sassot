package solipingen.sassot.mixin.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.PiercingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(PiercingEnchantment.class)
public abstract class PiercingEnchantmentMixin extends Enchantment {


    public PiercingEnchantmentMixin(Properties properties) {
        super(properties);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), index = 1)
    private static Enchantment.Properties modifiedInit(Enchantment.Properties properties) {
        return Enchantment.properties(ModItemTags.PIERCING_ENCHANTABLE,
                properties.weight(), properties.maxLevel(), properties.minCost(), properties.maxCost(), properties.anvilCost(), properties.slots());
    }

    @Inject(method = "canAccept", at = @At("HEAD"), cancellable = true)
    private void injectedCanAccept(Enchantment other, CallbackInfoReturnable<Boolean> cbireturn) {
        cbireturn.setReturnValue(super.canAccept(other) && other != Enchantments.MULTISHOT
                && other != Enchantments.RIPTIDE && other != ModEnchantments.WHIRLWIND && other != ModEnchantments.FLARE);
    }


}
