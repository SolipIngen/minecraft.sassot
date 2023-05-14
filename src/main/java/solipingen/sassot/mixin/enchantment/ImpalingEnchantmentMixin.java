package solipingen.sassot.mixin.enchantment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ImpalingEnchantment;
import net.minecraft.entity.EquipmentSlot;


@Mixin(ImpalingEnchantment.class)
public class ImpalingEnchantmentMixin extends Enchantment {

    
    protected ImpalingEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @ModifyConstant(method = "getAttackDamage", constant = @Constant(floatValue = 2.5f))
    private float injectedGetAttackDamage(float damageAmount) {
        return 0.0f;
    }
    
}
