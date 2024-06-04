package solipingen.sassot.mixin.enchantment;

import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.LuckEnchantment;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LuckEnchantment.class)
public abstract class LuckEnchantmentMixin extends Enchantment {

    
    protected LuckEnchantmentMixin(Enchantment.Properties properties) {
        super(properties);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static Enchantment.Properties modifiedInit(Enchantment.Properties properties) {
        if (properties.supportedItems() == ItemTags.SWORD_ENCHANTABLE) {
            return Enchantment.properties(ItemTags.WEAPON_ENCHANTABLE, properties.weight(), properties.maxLevel(), properties.minCost(), properties.maxCost(), properties.anvilCost(), properties.slots());
        }
        return properties;
    }

    @Inject(method = "canAccept", at = @At("HEAD"), cancellable = true)
    private void injectedCanAccept(Enchantment other, CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.getApplicableItems() == ItemTags.WEAPON_ENCHANTABLE || other.getApplicableItems() == ItemTags.WEAPON_ENCHANTABLE) {
            cbireturn.setReturnValue(true);
        }
    }

    
}
