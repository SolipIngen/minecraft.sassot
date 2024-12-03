package solipingen.sassot.resource;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModDataPacks {


    public static void registerModDataPacks() {

        FabricLoader.getInstance().getModContainer(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID).ifPresent((container) -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "bettersassotcombat"), container, ResourcePackActivationType.DEFAULT_ENABLED);
        });

    }

    
}
