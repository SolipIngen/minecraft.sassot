package solipingen.sassot.registry.tag;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModItemTags {

    public static final TagKey<Item> SWEEPING_WEAPONS = TagKey.of(RegistryKeys.ITEM, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "sweeping_weapons"));
    public static final TagKey<Item> THRUSTING_WEAPONS = TagKey.of(RegistryKeys.ITEM, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "thrusting_weapons"));
    public static final TagKey<Item> HACKING_WEAPONS = TagKey.of(RegistryKeys.ITEM, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "hacking_weapons"));
    public static final TagKey<Item> SHIELD_FRAMING_SMITHING_TEMPLATE_INGOTS = TagKey.of(RegistryKeys.ITEM, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "shield_framing_smithing_template_ingots"));
    
}
