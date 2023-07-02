package solipingen.sassot.mixin.client.integration.lambdynlights;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import solipingen.sassot.entity.ModEntityTypes;


@Environment(value = EnvType.CLIENT)
public class ModLambDynamicLightsPlugin implements DynamicLightsInitializer {

    
    @Override
    public void onInitializeDynamicLights() {

        DynamicLightHandlers.registerDynamicLightHandler(ModEntityTypes.BLAZEARM_ENTITY, (entity) -> {
            if (entity.isTouchingWater()) {
                return 4;
            }
            return 14;
        });

    }

    
}
