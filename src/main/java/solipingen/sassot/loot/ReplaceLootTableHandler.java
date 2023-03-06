package solipingen.sassot.loot;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ReplaceLootTableHandler implements LootTableEvents.Replace {
    private static final Identifier ELDER_GUARDIAN_DROP_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "entities/elder_guardian");
    private static final Identifier TOOLSMITH_VILLAGER_CHEST_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/village/village_toolsmith");
    private static final Identifier WEAPONSMITH_VILLAGER_CHEST_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/village/village_weaponsmith");
    private static final Identifier[] ID_ARRAY = new Identifier[]{ 
        ELDER_GUARDIAN_DROP_ID, 
        TOOLSMITH_VILLAGER_CHEST_ID, WEAPONSMITH_VILLAGER_CHEST_ID
    };


    @Override
    @Nullable
    public LootTable replaceLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable original, LootTableSource source) {
        for (int i = 0; i < ID_ARRAY.length; i++) {
            if (id.getPath().equals(ID_ARRAY[i].getPath()) && source.isBuiltin()) {
                LootTable newTable = lootManager.getTable(ID_ARRAY[i]);
                return newTable;
            }
        }
        return original;
    }


    
}
