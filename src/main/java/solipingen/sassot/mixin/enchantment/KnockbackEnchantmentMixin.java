package solipingen.sassot.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.KnockbackEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

@Mixin(KnockbackEnchantment.class)
public abstract class KnockbackEnchantmentMixin extends Enchantment {

    
    protected KnockbackEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack) || stack.getItem() instanceof AxeItem;
    }
    
}
