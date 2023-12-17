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
    private static final Identifier TOOLSMITH_VILLAGER_CHEST_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/village/village_toolsmith");
    private static final Identifier WEAPONSMITH_VILLAGER_CHEST_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/village/village_weaponsmith");
    private static final Identifier[] ID_ARRAY = new Identifier[]{
        TOOLSMITH_VILLAGER_CHEST_ID, WEAPONSMITH_VILLAGER_CHEST_ID};


    @Override
    @Nullable
    public LootTable replaceLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable original, LootTableSource source) {
        for (Identifier modIdentifier : ID_ARRAY) {
            if (modIdentifier.getPath().matches(id.getPath()) && (source.isBuiltin() || source == LootTableSource.DATA_PACK)) {
                LootTable newTable = lootManager.getLootTable(modIdentifier);
                return newTable;
            }
        }
        return null;
    }


    
}
