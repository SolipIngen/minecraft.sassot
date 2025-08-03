package solipingen.sassot.registry.tag;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModDamageTypeTags {

    public static TagKey<DamageType> IS_ALSO_BLOCKABLE_BY_SHIELD = TagKey.of(RegistryKeys.DAMAGE_TYPE,
            Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "is_also_blockable_by_shield"));
    public static TagKey<DamageType> ECHOING = TagKey.of(RegistryKeys.DAMAGE_TYPE,
            Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "echoing"));

}
