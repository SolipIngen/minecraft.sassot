package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.world.World;
import solipingen.sassot.enchantment.ModEnchantments;


@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item {

    
    public ShieldItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int cloakingLevel = EnchantmentHelper.getLevel(ModEnchantments.CLOAKING, stack);
        if (cloakingLevel > 0) {
            user.setInvisible(true);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int cloakingLevel = EnchantmentHelper.getLevel(ModEnchantments.CLOAKING, stack);
        if (cloakingLevel > 0 && !user.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            user.setInvisible(false);
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return !stack.hasEnchantments();
    }
    
}
