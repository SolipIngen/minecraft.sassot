package solipingen.sassot.enchantment.shield;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import solipingen.sassot.enchantment.ModEnchantments;


public class ProjectileDeflectionEnchantment extends Enchantment {

    
    public ProjectileDeflectionEnchantment(Rarity weight, EquipmentSlot ...slotTypes) {
        super(weight, EnchantmentTarget.BREAKABLE, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return 10 + 20*(level - 1);
    }

    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && !(other == ModEnchantments.SHOCK_REBOUND || other == ModEnchantments.CLOAKING);
    }

}
