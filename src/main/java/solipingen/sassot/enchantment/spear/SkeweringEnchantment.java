package solipingen.sassot.enchantment.spear;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import solipingen.sassot.enchantment.ModEnchantments;


public class SkeweringEnchantment extends Enchantment {

    
    public SkeweringEnchantment(Enchantment.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != ModEnchantments.WHIRLWIND && other != Enchantments.RIPTIDE && other != ModEnchantments.FLARE;
    }
    
    
}
