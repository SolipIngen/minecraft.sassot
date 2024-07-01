package solipingen.sassot.item;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;
import net.minecraft.util.Unit;


public class ModifyItemComponentHandler implements DefaultItemComponentEvents.ModifyCallback {
    private static final Item[] FIREPROOF_ITEMS = {Items.BLAZE_ROD, Items.BLAZE_POWDER, Items.END_ROD};


    @Override
    public void modify(DefaultItemComponentEvents.ModifyContext context) {
        for (Item item : FIREPROOF_ITEMS) {
            context.modify(item, builder -> builder.add(DataComponentTypes.FIRE_RESISTANT, Unit.INSTANCE));
        }
        context.modify(Items.BRUSH, builder -> builder.add(DataComponentTypes.MAX_DAMAGE, 96));
        context.modify(Items.FLINT_AND_STEEL,  builder -> builder.add(DataComponentTypes.MAX_DAMAGE, 108));
        context.modify(Items.TRIDENT, builder -> builder.add(DataComponentTypes.RARITY, Rarity.UNCOMMON));
        context.modify(Items.MACE, builder -> builder.add(DataComponentTypes.RARITY, Rarity.UNCOMMON));
        context.modify(Items.HEAVY_CORE, builder -> builder.add(DataComponentTypes.RARITY, Rarity.UNCOMMON));
    }


}
