package solipingen.sassot.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.sound.ModBlockSoundGroup;

import java.util.function.Function;


public class ModBlocks {
    
    // Echo Crystal
    public static final Block ECHO_CRYSTAL_BLOCK = ModBlocks.registerBlock("echo_crystal_block", 
        EchoCrystalBlock::new, AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK).strength(2.0f).luminance(state -> 12).sounds(ModBlockSoundGroup.ECHO_CRYSTAL_BLOCK), true);

    public static final Block ECHO_CRYSTAL_CLUSTER = ModBlocks.registerBlock("echo_crystal_cluster", 
        settings -> new EchoCrystalClusterBlock(14, 4, settings), AbstractBlock.Settings.copy(Blocks.AMETHYST_CLUSTER).strength(2.0f).luminance(state -> 10).sounds(ModBlockSoundGroup.ECHO_CLUSTER), true);
    
    public static final Block LARGE_ECHO_CRYSTAL_BUD = ModBlocks.registerBlock("large_echo_crystal_bud",
        settings -> new EchoCrystalClusterBlock(10, 4, settings), AbstractBlock.Settings.copy(ECHO_CRYSTAL_CLUSTER).luminance(state -> 8).sounds(ModBlockSoundGroup.LARGE_ECHO_BUD), true);

    public static final Block MEDIUM_ECHO_CRYSTAL_BUD = ModBlocks.registerBlock("medium_echo_crystal_bud", 
        settings -> new EchoCrystalClusterBlock(7, 4, settings), AbstractBlock.Settings.copy(ECHO_CRYSTAL_CLUSTER).luminance(state -> 6).sounds(ModBlockSoundGroup.MEDIUM_ECHO_BUD), true);

    public static final Block SMALL_ECHO_CRYSTAL_BUD = ModBlocks.registerBlock("small_echo_crystal_bud", 
        settings -> new EchoCrystalClusterBlock(3, 4, settings), AbstractBlock.Settings.copy(ECHO_CRYSTAL_CLUSTER).luminance(state -> 4).sounds(ModBlockSoundGroup.SMALL_ECHO_BUD), true);

    
    // Villager Fighter Marker
    public static final Block SWORDSMAN_MARKER = ModBlocks.registerBlock("swordsman_marker", 
        VillagerFighterMarkerBlock::new, (AbstractBlock.Settings.copy(Blocks.POLISHED_GRANITE_SLAB)), true);
    
    public static final Block SPEARMAN_MARKER = ModBlocks.registerBlock("spearman_marker", 
        VillagerFighterMarkerBlock::new, (AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE_SLAB)), true);


    // Registering Methods
    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean withBlockItem) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name));
        Block block = blockFactory.apply(settings.registryKey(blockKey));
        if (withBlockItem) {
            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name));
            Registry.register(Registries.ITEM, itemKey, new BlockItem(block, new Item.Settings().registryKey(itemKey)));
        }
        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    public static void registerModBlocks() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Blocks for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

}
