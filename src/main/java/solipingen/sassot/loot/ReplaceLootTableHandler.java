package solipingen.sassot.loot;

import net.minecraft.loot.LootTables;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;

import java.util.List;


public class ReplaceLootTableHandler implements LootTableEvents.Replace {

    private static final List<RegistryKey<LootTable>> LOOT_TABLE_LIST = List.of(
            LootTables.VILLAGE_TOOLSMITH_CHEST, LootTables.VILLAGE_WEAPONSMITH_CHEST, LootTables.TRIAL_CHAMBERS_REWARD_CHEST
        );


    @Override
    @Nullable
    public LootTable replaceLootTable(RegistryKey<LootTable> key, LootTable original, LootTableSource source) {
//        RegistryEntryLookup<LootTable> lootTableLookup = BuiltinRegistries.createWrapperLookup().createRegistryLookup().getOrThrow(RegistryKeys.LOOT_TABLE);
//        for (RegistryKey<LootTable> lootTableKey : LOOT_TABLE_LIST) {
//            RegistryKey<LootTable> modLootTableKey = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, lootTableKey.getValue()))
//            if (modIdentifier.getPath().matches(original..getPath()) && (source.isBuiltin() || source == LootTableSource.DATA_PACK)) {
//                LootTable newTable = lootManager.getLootTable(modIdentifier);
//                return newTable;
//            }
//        }
        return null;
    }


    
}
