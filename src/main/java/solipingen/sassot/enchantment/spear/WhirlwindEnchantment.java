package solipingen.sassot.enchantment.spear;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import solipingen.sassot.enchantment.ModEnchantments;


public class WhirlwindEnchantment extends Enchantment {
    
    
    public WhirlwindEnchantment(Enchantment.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.LOYALTY && other != ModEnchantments.SKEWERING;
    }

}
