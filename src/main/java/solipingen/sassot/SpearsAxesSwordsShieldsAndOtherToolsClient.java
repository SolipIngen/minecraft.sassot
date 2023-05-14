package solipingen.sassot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import solipingen.sassot.client.item.ModModelPredicateProvider;
import solipingen.sassot.client.render.entity.ModEntityRendererRegistry;
import solipingen.sassot.client.render.entity.model.ModEntityModelLayers;

@Environment(value=EnvType.CLIENT)
public class SpearsAxesSwordsShieldsAndOtherToolsClient implements ClientModInitializer {

    
    @Override
    public void onInitializeClient() {

        ModEntityModelLayers.registerModEntityModelLayers();
        ModEntityRendererRegistry.registerModEntityRenderers();
        ModModelPredicateProvider.registerModItemModelPredicates();
        
    }
    
}
