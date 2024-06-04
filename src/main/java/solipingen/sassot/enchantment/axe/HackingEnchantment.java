package solipingen.sassot.enchantment.axe;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.registry.tag.ModItemTags;


public class HackingEnchantment extends Enchantment {
    
    
    public HackingEnchantment(Enchantment.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || stack.getItem() instanceof BlazearmItem || stack.isIn(ModItemTags.HACKING_WEAPONS);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SWEEPING_EDGE && other != ModEnchantments.THRUSTING;
    }



}
