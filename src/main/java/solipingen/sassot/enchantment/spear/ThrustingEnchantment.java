package solipingen.sassot.enchantment.spear;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import solipingen.sassot.enchantment.ModEnchantments;


public class ThrustingEnchantment extends Enchantment {

    
    public ThrustingEnchantment(Enchantment.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SWEEPING_EDGE && other != ModEnchantments.HACKING;
    }

}
