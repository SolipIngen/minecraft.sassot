package solipingen.sassot.item;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.component.type.DamageResistantComponent;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.component.type.RepairableComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Rarity;
import net.minecraft.util.Unit;

import java.util.List;
import java.util.Optional;


public class ModifyItemComponentHandler implements DefaultItemComponentEvents.ModifyCallback {
    private static final Item[] FIREPROOF_ITEMS = {Items.BLAZE_ROD, Items.BLAZE_POWDER, Items.END_ROD, Items.WITHER_SKELETON_SKULL};


    @Override
    public void modify(DefaultItemComponentEvents.ModifyContext context) {
        for (Item item : FIREPROOF_ITEMS) {
            context.modify(item, builder -> builder.add(DataComponentTypes.DAMAGE_RESISTANT,
                    new DamageResistantComponent(DamageTypeTags.IS_FIRE)));
        }
        context.modify(Items.SHIELD, builder -> builder.add(DataComponentTypes.ENCHANTABLE,
                        new EnchantableComponent(15))
                .add(DataComponentTypes.BLOCKS_ATTACKS,
                        new BlocksAttacksComponent(0.25f, 0.6f,
                                List.of(new BlocksAttacksComponent.DamageReduction(90.0f, Optional.empty(), 0.0f, 1.0f)),
                                new BlocksAttacksComponent.ItemDamage(3.0f, 0.0f, 1.0f),
                                Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                                Optional.of(SoundEvents.ITEM_SHIELD_BLOCK), Optional.of(SoundEvents.ITEM_SHIELD_BREAK))));
        context.modify(Items.FISHING_ROD, builder -> builder.add(DataComponentTypes.ENCHANTABLE,
                new EnchantableComponent(15)));
        context.modify(Items.BRUSH, builder -> builder.add(DataComponentTypes.MAX_DAMAGE, 96));
        context.modify(Items.FLINT_AND_STEEL,  builder -> builder.add(DataComponentTypes.MAX_DAMAGE, 108));
        context.modify(Items.TRIDENT, builder -> builder.add(DataComponentTypes.ENCHANTABLE, new EnchantableComponent(10))
                .add(DataComponentTypes.REPAIRABLE, new RepairableComponent(RegistryEntryList.of(Items.PRISMARINE_SHARD.getRegistryEntry())))
                .add(DataComponentTypes.RARITY, Rarity.UNCOMMON));
        context.modify(Items.MACE, builder -> builder.add(DataComponentTypes.RARITY, Rarity.UNCOMMON));
        context.modify(Items.HEAVY_CORE, builder -> builder.add(DataComponentTypes.RARITY, Rarity.UNCOMMON));
    }


}
