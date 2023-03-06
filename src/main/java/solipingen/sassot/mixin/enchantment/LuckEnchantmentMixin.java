package solipingen.sassot.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.LuckEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(LuckEnchantment.class)
public abstract class LuckEnchantmentMixin extends Enchantment {

    
    protected LuckEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Inject(method = "canAccept", at = @At("HEAD"), cancellable = true)
    private void injectedCanAccept(Enchantment other, CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.type == EnchantmentTarget.WEAPON) {
            cbireturn.setReturnValue(super.canAccept(other));
        }
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        if (this.type == EnchantmentTarget.WEAPON) {
            return super.isAcceptableItem(stack) || (stack.getItem() instanceof SpearItem || stack.getItem() instanceof BlazearmItem || stack.getItem() instanceof TridentItem || stack.getItem() instanceof AxeItem);
        }
        else {
            return super.isAcceptableItem(stack);
        }
    }
    
}
