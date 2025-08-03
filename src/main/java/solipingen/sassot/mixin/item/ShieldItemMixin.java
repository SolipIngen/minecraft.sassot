package solipingen.sassot.mixin.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
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
        RegistryEntryLookup<Enchantment> enchantmentLookup = user.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
        int cloakingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.CLOAKING), stack);
        user.setInvisible(cloakingLevel > 0);
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!user.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            user.setInvisible(false);
        }
        return true;
    }

    
}
