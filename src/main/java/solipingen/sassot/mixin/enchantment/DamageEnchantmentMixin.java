package solipingen.sassot.mixin.enchantment;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;

@Mixin(DamageEnchantment.class)
public abstract class DamageEnchantmentMixin extends Enchantment {
    @Shadow @Final public int typeIndex;


    protected DamageEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
    private void injectedGetAttackDamage(int level, EntityGroup group, CallbackInfoReturnable<Float> cbireturn) {
        if (this.typeIndex == 0) {
            cbireturn.setReturnValue(level*1.0f);
        }
    }

    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void injectedIsAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cbireturn) {
        if (stack.getItem() instanceof SpearItem || stack.getItem() instanceof BlazearmItem || stack.getItem() instanceof TridentItem) {
            cbireturn.setReturnValue(true);
        }
    }
    
}
