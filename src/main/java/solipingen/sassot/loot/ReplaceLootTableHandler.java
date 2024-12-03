package solipingen.sassot.loot;

import net.minecraft.loot.LootTables;
import net.minecraft.registry.*;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;

import java.util.List;


public class ReplaceLootTableHandler implements LootTableEvents.Replace {

    private static final List<RegistryKey<LootTable>> LOOT_TABLE_LIST = List.of(
            LootTables.VILLAGE_TOOLSMITH_CHEST, LootTables.VILLAGE_WEAPONSMITH_CHEST, LootTables.TRIAL_CHAMBERS_REWARD_CHEST
        );


    @Override
    public @Nullable LootTable replaceLootTable(RegistryKey<LootTable> key, LootTable original, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
        // Fill in if necessary.
        return null;
    }

}
