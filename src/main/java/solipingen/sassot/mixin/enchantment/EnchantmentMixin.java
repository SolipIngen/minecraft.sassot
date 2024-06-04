package solipingen.sassot.mixin.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Shadow
    public abstract String getTranslationKey();


    @Inject(method = "getApplicableItems", at = @At("HEAD"), cancellable = true)
    private void injectedGetApplicableItems(CallbackInfoReturnable<TagKey<Item>> cbireturn) {
        String name = this.getTranslationKey();
        if (name.contains("knockback")) {
            cbireturn.setReturnValue(ModItemTags.KNOCKBACK_ENCHANTABLE);
        }
        else if (name.contains("loyalty")) {
            cbireturn.setReturnValue(ModItemTags.LOYALTY_ENCHANTABLE);
        }
    }


}
