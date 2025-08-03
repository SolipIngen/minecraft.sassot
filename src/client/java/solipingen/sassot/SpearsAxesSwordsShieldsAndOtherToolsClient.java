package solipingen.sassot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import solipingen.sassot.client.render.entity.ModEntityRendererRegistry;
import solipingen.sassot.client.render.entity.model.ModEntityModelLayers;
import solipingen.sassot.client.render.item.property.ModItemRenderProperties;
import solipingen.sassot.client.resource.ModClientResourcePacks;


@Environment(value = EnvType.CLIENT)
public class SpearsAxesSwordsShieldsAndOtherToolsClient implements ClientModInitializer {

    
    @Override
    public void onInitializeClient() {

        ModItemRenderProperties.registerModsItemRenderProperties();
        ModEntityModelLayers.registerModEntityModelLayers();
        ModEntityRendererRegistry.registerModEntityRenderers();

        ModClientResourcePacks.registerModBuiltInResourcePacks();
        
    }
    
}
