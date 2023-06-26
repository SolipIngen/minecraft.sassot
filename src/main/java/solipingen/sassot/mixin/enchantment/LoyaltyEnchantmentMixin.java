package solipingen.sassot.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.LoyaltyEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(LoyaltyEnchantment.class)
public abstract class LoyaltyEnchantmentMixin extends Enchantment {

    
    protected LoyaltyEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SpearItem || stack.getItem() instanceof BlazearmItem || stack.getItem() instanceof TridentItem;
    }
    
}
