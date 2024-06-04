package solipingen.sassot.enchantment.shield;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import solipingen.sassot.enchantment.ModEnchantments;


public class CloakingEnchantment extends Enchantment {

    
    public CloakingEnchantment(Enchantment.Properties properties) {
        super(properties);
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
