package solipingen.sassot.mixin.item;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder, FabricItemStack {


    @Inject(method = "appendAttributeModifiersTooltip", at = @At("HEAD"), cancellable = true)
    private void injectedAppendAttributeModifiersTooltip(Consumer<Text> textConsumer, TooltipDisplayComponent displayComponent, @Nullable PlayerEntity player, CallbackInfo cbi) {
        if (player != null) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = player.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            int sharpnessLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.SHARPNESS), (ItemStack)(Object)this);
            int densityLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.DENSITY), (ItemStack)(Object)this);
            if (sharpnessLevel > 0 || densityLevel > 0) {
                if (displayComponent.shouldDisplay(DataComponentTypes.ATTRIBUTE_MODIFIERS)) {
                    AttributeModifierSlot[] slots = AttributeModifierSlot.values();
                    for (AttributeModifierSlot attributeModifierSlot : slots) {
                        MutableBoolean mutableBoolean = new MutableBoolean(true);
                        ((ItemStack)(Object)this).applyAttributeModifier(attributeModifierSlot, (attribute, modifier, display) -> {
                            if (display != AttributeModifiersComponent.Display.getHidden()) {
                                if (mutableBoolean.isTrue()) {
                                    textConsumer.accept(ScreenTexts.EMPTY);
                                    textConsumer.accept(Text.translatable("item.modifiers." + attributeModifierSlot.asString()).formatted(Formatting.GRAY));
                                    mutableBoolean.setFalse();
                                }
                                if (modifier.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID)) {
                                    double value = modifier.value() + 1.0*sharpnessLevel + 1.0*densityLevel + player.getAttributeBaseValue(EntityAttributes.ATTACK_DAMAGE);
                                    display.addTooltip(textConsumer, player, attribute, new EntityAttributeModifier(modifier.id(), value, modifier.operation()));
                                }
                                else {
                                    display.addTooltip(textConsumer, player, attribute, modifier);
                                }
                            }
                        });
                    }
                }
                cbi.cancel();
            }
        }
    }

}
