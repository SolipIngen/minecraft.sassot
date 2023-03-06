package solipingen.sassot.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.sound.ModBlockSoundGroup;

@SuppressWarnings("unused")
public class ModBlocks {
    
    // Echo Crystal
    public static final Block ECHO_CRYSTAL_CLUSTER = registerBlock("echo_crystal_cluster", 
        new EchoCrystalClusterBlock(7, 3, FabricBlockSettings.copy(Blocks.AMETHYST_CLUSTER).strength(2.0f).luminance(state -> 10).sounds(ModBlockSoundGroup.ECHO_CLUSTER)));
    
    public static final Block LARGE_ECHO_CRYSTAL_BUD = registerBlock("large_echo_crystal_bud", 
        new EchoCrystalClusterBlock(5, 3, FabricBlockSettings.copy(ECHO_CRYSTAL_CLUSTER).luminance(state -> 9).sounds(ModBlockSoundGroup.LARGE_ECHO_BUD)));

    public static final Block MEDIUM_ECHO_CRYSTAL_BUD = registerBlock("medium_echo_crystal_bud", 
        new EchoCrystalClusterBlock(4, 3, FabricBlockSettings.copy(ECHO_CRYSTAL_CLUSTER).luminance(state -> 8).sounds(ModBlockSoundGroup.MEDIUM_ECHO_BUD)));

    public static final Block SMALL_ECHO_CRYSTAL_BUD = registerBlock("small_echo_crystal_bud", 
        new EchoCrystalClusterBlock(3, 4, FabricBlockSettings.copy(ECHO_CRYSTAL_CLUSTER).luminance(state -> 7).sounds(ModBlockSoundGroup.SMALL_ECHO_BUD)));

    
    // Villager Fighter Marker
    public static final Block SWORDSMAN_MARKER = registerBlock("swordsman_marker", 
        new VillagerFighterMarkerBlock(FabricBlockSettings.copy(Blocks.POLISHED_GRANITE_SLAB)));
    
    public static final Block SPEARMAN_MARKER = registerBlock("spearman_marker", 
        new VillagerFighterMarkerBlock(FabricBlockSettings.copy(Blocks.POLISHED_ANDESITE_SLAB)));



    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), block);
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), 
            new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering ModBlocks for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

}
