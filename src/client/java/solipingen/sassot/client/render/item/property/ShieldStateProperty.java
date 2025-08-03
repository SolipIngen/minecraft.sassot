package solipingen.sassot.client.render.item.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.select.ChargeTypeProperty;
import net.minecraft.client.render.item.property.select.SelectProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.ModShieldItem;


public record ShieldStateProperty() implements SelectProperty<ModShieldItem.State> {
    public static final Codec<ModShieldItem.State> VALUE_CODEC = ModShieldItem.State.CODEC;
    public static final SelectProperty.Type<ShieldStateProperty, ModShieldItem.State> TYPE = Type.create(MapCodec.unit(new ShieldStateProperty()), VALUE_CODEC);


    public ShieldStateProperty() {
    }

    @Override
    public ModShieldItem.State getValue(ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        if (livingEntity != null && livingEntity.isUsingItem() && livingEntity.getBlockingItem() == itemStack) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = livingEntity.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            if (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.CLOAKING), itemStack) > 0) {
                return ModShieldItem.State.CLOAKING;
            }
            else {
                return ModShieldItem.State.BLOCKING;
            }
        }
        return ModShieldItem.State.DEFAULT;
    }

    @Override
    public SelectProperty.Type<ShieldStateProperty, ModShieldItem.State> getType() {
        return TYPE;
    }

    @Override
    public Codec<ModShieldItem.State> valueCodec() {
        return VALUE_CODEC;
    }


}
