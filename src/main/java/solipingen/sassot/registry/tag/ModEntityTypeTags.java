package solipingen.sassot.registry.tag;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModEntityTypeTags {

    public static final TagKey<EntityType<?>> SPEARS = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "spears"));

    public static final TagKey<EntityType<?>> IMMUNE_TO_WHIRLWIND = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "immune_to_whirlwind"));
    public static final TagKey<EntityType<?>> SENSITIVE_TO_WHIRLWIND = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "sensitive_to_whirlwind"));
    public static final TagKey<EntityType<?>> IMMUNE_TO_FLARE = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "immune_to_flare"));
    public static final TagKey<EntityType<?>> SENSITIVE_TO_LEANING = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "sensitive_to_leaning"));

    public static final TagKey<EntityType<?>> SPEARMAN_VILLAGER_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "spearman_villager_targets"));
    public static final TagKey<EntityType<?>> SWORDSMAN_VILLAGER_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "swordsman_villager_targets"));

}
