package solipingen.sassot.registry.tag;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModItemTags {

    public static final TagKey<Item> SWEEPING_WEAPONS = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/sweeping_weapons"));
    public static final TagKey<Item> THRUSTING_WEAPONS = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/thrusting_weapons"));
    public static final TagKey<Item> HACKING_WEAPONS = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/hacking_weapons"));
    public static final TagKey<Item> SHIELD_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/shield"));
    public static final TagKey<Item> SPEAR_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/spear"));
    public static final TagKey<Item> BLAZEARM_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/blazearm"));
    public static final TagKey<Item> KNOCKBACK_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/knockback"));
    public static final TagKey<Item> LOYALTY_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/loyalty"));
    public static final TagKey<Item> GROUNDSHAKING_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/groundshaking"));
    public static final TagKey<Item> PIERCING_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "enchantable/piercing"));

    public static final TagKey<Item> SHIELDS = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "shields"));
    public static final TagKey<Item> SPEARS = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "spears"));
    public static final TagKey<Item> SHIELD_FRAMING_SMITHING_TEMPLATE_INGOTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "shield_framing_smithing_template_ingots"));

    public static final TagKey<Item> PIG_TEMPT_ITEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "pig_tempt_items"));
    
}
