package solipingen.sassot.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.sound.ModBlockSoundGroup;


public class ModBlocks {
    
    // Echo Crystal
    public static final Block ECHO_CRYSTAL_BLOCK = ModBlocks.registerBlock("echo_crystal_block", 
        new EchoCrystalBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).strength(2.0f).luminance(state -> 12).sounds(ModBlockSoundGroup.ECHO_CRYSTAL_BLOCK)), true);

    public static final Block ECHO_CRYSTAL_CLUSTER = ModBlocks.registerBlock("echo_crystal_cluster", 
        new EchoCrystalClusterBlock(7, 3, AbstractBlock.Settings.copy(Blocks.AMETHYST_CLUSTER).strength(2.0f).luminance(state -> 10).sounds(ModBlockSoundGroup.ECHO_CLUSTER)), true);
    
    public static final Block LARGE_ECHO_CRYSTAL_BUD = ModBlocks.registerBlock("large_echo_crystal_bud", 
        new EchoCrystalClusterBlock(5, 3, AbstractBlock.Settings.copy(ECHO_CRYSTAL_CLUSTER).luminance(state -> 8).sounds(ModBlockSoundGroup.LARGE_ECHO_BUD)), true);

    public static final Block MEDIUM_ECHO_CRYSTAL_BUD = ModBlocks.registerBlock("medium_echo_crystal_bud", 
        new EchoCrystalClusterBlock(4, 3, AbstractBlock.Settings.copy(ECHO_CRYSTAL_CLUSTER).luminance(state -> 6).sounds(ModBlockSoundGroup.MEDIUM_ECHO_BUD)), true);

    public static final Block SMALL_ECHO_CRYSTAL_BUD = ModBlocks.registerBlock("small_echo_crystal_bud", 
        new EchoCrystalClusterBlock(3, 4, AbstractBlock.Settings.copy(ECHO_CRYSTAL_CLUSTER).luminance(state -> 4).sounds(ModBlockSoundGroup.SMALL_ECHO_BUD)), true);

    
    // Villager Fighter Marker
    public static final Block SWORDSMAN_MARKER = ModBlocks.registerBlock("swordsman_marker", 
        new VillagerFighterMarkerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE_SLAB)), true);
    
    public static final Block SPEARMAN_MARKER = ModBlocks.registerBlock("spearman_marker", 
        new VillagerFighterMarkerBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE_SLAB)), true);



    private static Block registerBlock(String name, Block block, boolean withBlockItem) {
        if (withBlockItem) {
            ModBlocks.registerBlockItem(name, block);
        }
        return Registry.register(Registries.BLOCK, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering ModBlocks for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

}
