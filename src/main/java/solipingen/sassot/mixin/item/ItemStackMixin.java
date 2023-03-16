package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements FabricItemStack {


    @Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeBaseValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private double redireectedGetAttributeBaseValue(PlayerEntity player, EntityAttribute originalEntityAttribute) {
        double addition = player.getAttributeBaseValue(originalEntityAttribute);
        if (originalEntityAttribute == EntityAttributes.GENERIC_ATTACK_SPEED) {
            if (((ItemStack)(Object)this).getItem() instanceof SwordItem) {
                int sweepLevel = EnchantmentHelper.getLevel(Enchantments.SWEEPING, ((ItemStack)(Object)this));
                addition += 0.1*sweepLevel;
            }
            else if (((ItemStack)(Object)this).getItem() instanceof SpearItem || ((ItemStack)(Object)this).getItem() instanceof TridentItem) {
                int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, ((ItemStack)(Object)this));
                addition += 0.1*thrustLevel;
            }
            else if (((ItemStack)(Object)this).getItem() instanceof AxeItem || ((ItemStack)(Object)this).getItem() instanceof BlazearmItem) {
                int hackLevel = EnchantmentHelper.getLevel(ModEnchantments.HACKING, ((ItemStack)(Object)this));
                addition += 0.1*hackLevel;
            }
        }
        return addition;
    }


}
