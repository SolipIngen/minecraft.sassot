package solipingen.sassot.advancement;

import net.minecraft.advancement.criterion.Criteria;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;

public class ModCriteria {

    public static final ThrowSpearCriterion THROW_SPEAR = Criteria.register(new ThrowSpearCriterion());
    public static final PlayerSkeweredEntityCriterion PLAYER_SKEWERED_ENTITY = Criteria.register(new PlayerSkeweredEntityCriterion());


    public static void registerModCriteria() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Advancement Criteria for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }
    
}
