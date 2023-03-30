package solipingen.sassot.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.block.ModBlocks;

public class ModItemGroups {
    
    public static void registerModItemsToVanillaGroups() {

        // Copper Tool Items
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_SWORD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_AXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(ModItems.COPPER_SHOVEL));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(ModItems.COPPER_PICKAXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(ModItems.COPPER_AXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(ModItems.COPPER_HOE));

        // Shield Items
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.WOODEN_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_FRAMED_WOODEN_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_FRAMED_WOODEN_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLD_FRAMED_WOODEN_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_FRAMED_WOODEN_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.EMERALD_FRAMED_WOODEN_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.NETHERITE_FRAMED_WOODEN_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLDEN_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_SHIELD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.NETHERITE_SHIELD));

        // Spear Items
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.WOODEN_SPEAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.BAMBOO_SPEAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.STONE_SPEAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.FLINT_SPEAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.COPPER_SPEAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.IRON_SPEAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.GOLDEN_SPEAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.DIAMOND_SPEAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.NETHERITE_SPEAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.BLAZEARM));

        // Echo Crystal Items
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(ModBlocks.ECHO_CRYSTAL_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> entries.add(ModBlocks.ECHO_CRYSTAL_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(ModBlocks.ECHO_CRYSTAL_CLUSTER));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(ModBlocks.LARGE_ECHO_CRYSTAL_BUD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(ModBlocks.MEDIUM_ECHO_CRYSTAL_BUD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(ModBlocks.SMALL_ECHO_CRYSTAL_BUD));

        // Villager Fighter Marker Items
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(ModBlocks.SWORDSMAN_MARKER));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(ModBlocks.SPEARMAN_MARKER));
    
        // Shield Framing Template
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.SHIELD_FRAMING_SMITHING_TEMPLATE));

        // Elder Guardian Items
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ModItems.ELDER_GUARDIAN_SPIKE_SHARD));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(ModItems.ELDER_GUARDIAN_SPIKE_BONE_SHARD));


        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Items to Vanilla Groups for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);

    }

}
