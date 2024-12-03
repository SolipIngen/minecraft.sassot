package solipingen.sassot.mixin.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Shadow @Final private Text description;


    @Inject(method = "isSupportedItem", at = @At("HEAD"), cancellable = true)
    private void injectedIsSupportedItem(ItemStack stack, CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.description.getString().matches(Text.translatable("enchantment.minecraft.piercing").getString())) {
            cbireturn.setReturnValue(stack.isIn(ModItemTags.PIERCING_ENCHANTABLE));
        }
    }

    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void injectedIsAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.description.getString().matches(Text.translatable("enchantment.minecraft.piercing").getString())) {
            cbireturn.setReturnValue(stack.isIn(ModItemTags.PIERCING_ENCHANTABLE));
        }
    }

    @Inject(method = "canBeCombined", at = @At("HEAD"), cancellable = true)
    private static void injectedCanBeCombined(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second, CallbackInfoReturnable<Boolean> cbireturn) {
        if ((first.matchesKey(Enchantments.IMPALING) && second.isIn(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                || (first.isIn(EnchantmentTags.DAMAGE_EXCLUSIVE_SET) && second.matchesKey(Enchantments.IMPALING))) {
            cbireturn.setReturnValue(!first.equals(second));
        }
    }


}
