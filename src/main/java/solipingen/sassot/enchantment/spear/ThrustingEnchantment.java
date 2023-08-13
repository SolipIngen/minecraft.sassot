package solipingen.sassot.enchantment.spear;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.registry.tag.ModItemTags;


public class ThrustingEnchantment extends Enchantment {

    
    public ThrustingEnchantment(Enchantment.Rarity weight, EquipmentSlot ... slotTypes) {
        super(weight, EnchantmentTarget.BREAKABLE, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return 5 + (level - 1) * 9;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SpearItem || stack.getItem() instanceof TridentItem || stack.isIn(ModItemTags.THRUSTING_WEAPONS);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SWEEPING && other != ModEnchantments.HACKING;
    }

}
