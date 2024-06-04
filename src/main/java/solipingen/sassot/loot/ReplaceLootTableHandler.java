package solipingen.sassot.loot;

import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ReplaceLootTableHandler implements LootTableEvents.Replace {
    private static final Identifier TRIAL_CHAMBERS_REWARD_CHEST_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/trial_chambers/reward");


    @Override
    @Nullable
    public LootTable replaceLootTable(RegistryKey<LootTable> key, LootTable original, LootTableSource source) {
//        for (Identifier modIdentifier : ID_ARRAY) {
//            if (modIdentifier.getPath().matches(id.getPath()) && (source.isBuiltin() || source == LootTableSource.DATA_PACK)) {
//                LootTable newTable = lootManager.getLootTable(modIdentifier);
//                return newTable;
//            }
//        }
        return null;
    }


    
}
