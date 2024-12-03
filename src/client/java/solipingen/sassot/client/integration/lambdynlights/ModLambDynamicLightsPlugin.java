package solipingen.sassot.client.integration.lambdynlights;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import solipingen.sassot.entity.ModEntityTypes;


@Environment(value = EnvType.CLIENT)
public class ModLambDynamicLightsPlugin implements DynamicLightsInitializer {

    
    @Override
    public void onInitializeDynamicLights(ItemLightSourceManager itemLightSourceManager) {

        DynamicLightHandlers.registerDynamicLightHandler(ModEntityTypes.BLAZEARM_ENTITY, (entity) -> {
            if (entity.isTouchingWater()) {
                return 4;
            }
            return 14;
        });

    }

    
}
