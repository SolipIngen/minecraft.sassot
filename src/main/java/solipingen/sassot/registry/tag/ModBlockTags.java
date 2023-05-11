package solipingen.sassot.registry.tag;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModBlockTags {

    public static final TagKey<Block> GLASSLIKE = TagKey.of(RegistryKeys.BLOCK, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "glasslike"));
    public static final TagKey<Block> NEEDS_STONE_TOOL = TagKey.of(RegistryKeys.BLOCK, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "needs_stone_tool"));
    public static final TagKey<Block> NEEDS_COPPER_TOOL = TagKey.of(RegistryKeys.BLOCK, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "needs_copper_tool"));
    public static final TagKey<Block> NEEDS_IRON_TOOL = TagKey.of(RegistryKeys.BLOCK, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "needs_iron_tool"));
    public static final TagKey<Block> NEEDS_DIAMOND_TOOL = TagKey.of(RegistryKeys.BLOCK, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "needs_diamond_tool"));
    public static final TagKey<Block> NEEDS_NETHERITE_TOOL = TagKey.of(RegistryKeys.BLOCK, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "needs_netherite_tool"));

}
