package solipingen.sassot.enchantment.spear;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import solipingen.sassot.enchantment.ModEnchantments;


public class GroundshakingEnchantment extends Enchantment {

    
    public GroundshakingEnchantment(Enchantment.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != ModEnchantments.WHIRLWIND && other != Enchantments.WIND_BURST;
    }
    
    
}
