package solipingen.sassot.recipe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModRecipes {
    
    
    public static void registerModRecipes() {

        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "crafting_special_echoingshield"), 
            EchoingShieldRecipe.ECHOING_SHIELD_RECIPE_SERIALIZER);

    }

}
