package solipingen.sassot.enchantment.axe;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.registry.tag.ModItemTags;


public class HackingEnchantment extends Enchantment {
    
    
    public HackingEnchantment(Enchantment.Rarity weight, EquipmentSlot ... slotTypes) {
        super(weight, EnchantmentTarget.BREAKABLE, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return 5 + (level - 1)*9;
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
        return stack.isIn(ModItemTags.HACKING_WEAPONS);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SWEEPING && other != ModEnchantments.THRUSTING;
    }



}
