package solipingen.sassot.client.render.item.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.render.item.property.select.SelectProperties;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


@Environment(EnvType.CLIENT)
public class ModItemRenderProperties {


    public static void registerModsItemRenderProperties() {

        BooleanProperties.ID_MAPPER.put(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "using_spear"), UsingSpearProperty.CODEC);
        SelectProperties.ID_MAPPER.put(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "shield_state"), ShieldStateProperty.TYPE);

        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Item Render Properties for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

}
