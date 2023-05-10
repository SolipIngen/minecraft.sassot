package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements FabricItemStack {


    @Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeBaseValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private double redirectedGetAttributeBaseValue(PlayerEntity player, EntityAttribute originalEntityAttribute) {
        double addition = player.getAttributeBaseValue(originalEntityAttribute);
        if (originalEntityAttribute == EntityAttributes.GENERIC_ATTACK_SPEED) {
            if (((ItemStack)(Object)this).isIn(ModItemTags.SWEEPING_WEAPONS)) {
                int sweepLevel = EnchantmentHelper.getLevel(Enchantments.SWEEPING, ((ItemStack)(Object)this));
                addition += 0.1*sweepLevel;
            }
            else if (((ItemStack)(Object)this).isIn(ModItemTags.THRUSTING_WEAPONS)) {
                int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, ((ItemStack)(Object)this));
                addition += 0.1*thrustLevel;
            }
            else if (((ItemStack)(Object)this).isIn(ModItemTags.HACKING_WEAPONS)) {
                int hackLevel = EnchantmentHelper.getLevel(ModEnchantments.HACKING, ((ItemStack)(Object)this));
                addition += 0.1*hackLevel;
            }
        }
        return addition;
    }


}
