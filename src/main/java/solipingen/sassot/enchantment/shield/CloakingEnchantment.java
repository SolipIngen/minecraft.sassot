package solipingen.sassot.enchantment.shield;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import solipingen.sassot.enchantment.ModEnchantments;


public class CloakingEnchantment extends Enchantment {

    
    public CloakingEnchantment(Rarity weight, EquipmentSlot ...slotTypes) {
        super(weight, EnchantmentTarget.BREAKABLE, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return 20;
    }

    @Override
    public int getMaxPower(int level) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && !(other == ModEnchantments.PROJECTILE_DEFLECTION || other == ModEnchantments.SHOCK_REBOUND);
    }

}
