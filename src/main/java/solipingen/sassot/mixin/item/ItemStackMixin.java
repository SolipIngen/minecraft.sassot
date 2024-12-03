package solipingen.sassot.mixin.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements FabricItemStack {


    @ModifyVariable(method = "appendAttributeModifierTooltip", at = @At("STORE"), ordinal = 0)
    private double modifiedAttributeValue(double d, Consumer<Text> textConsumer, @Nullable PlayerEntity player, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
        if (player != null && modifier.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID)) {
            int sharpnessLevel = EnchantmentHelper.getLevel(player.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SHARPNESS), ((ItemStack)(Object)this));
            if (sharpnessLevel <= 0) {
                sharpnessLevel = EnchantmentHelper.getLevel(player.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.DENSITY), ((ItemStack)(Object)this));
            }
            d = modifier.value() + player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) + 1.0*sharpnessLevel;
        }
        return d;
    }



}
