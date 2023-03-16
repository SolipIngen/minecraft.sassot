package solipingen.sassot.enchantment.spear;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


public class SkeweringEnchantment extends Enchantment {

    
    public SkeweringEnchantment(Enchantment.Rarity weight, EquipmentSlot ... slotTypes) {
        super(weight, EnchantmentTarget.BREAKABLE, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return 25;
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
        return stack.getItem() instanceof SpearItem || stack.getItem() instanceof BlazearmItem || stack.getItem() instanceof TridentItem;
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != ModEnchantments.WHIRLWIND;
    }
    
    
}
