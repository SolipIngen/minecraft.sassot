package solipingen.sassot.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;


public class FlareEnchantment extends Enchantment {
    
    
    public FlareEnchantment(Enchantment.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.LOYALTY && other != ModEnchantments.SKEWERING;
    }

}
