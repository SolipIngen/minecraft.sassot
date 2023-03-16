package solipingen.sassot.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.item.ModItems;


public class ModifyLootTableHandler implements LootTableEvents.Modify {
    private static final Identifier ELDER_GUARDIAN_DROP_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "entities/elder_guardian");
    private static final Identifier WEAPONSMITH_VILLAGER_CHEST_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/village/village_weaponsmith");
    private static final Identifier[] ID_ARRAY = new Identifier[]{ 
        ELDER_GUARDIAN_DROP_ID, WEAPONSMITH_VILLAGER_CHEST_ID};


    @Override
    public void modifyLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable.Builder tableBuilder, LootTableSource source) {
        for (Identifier identifier : ID_ARRAY) {
            if (id.getPath().equals(identifier.getPath())) {
                float probability = id.getPath().startsWith("entities") ? 0.33f : 0.5f;
                LootPool.Builder poolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(RandomChanceWithLootingLootCondition.builder(probability, id.getPath().startsWith("entities") ? 0.11f : 0.0f))
                    .with(id.getPath().startsWith("entities") ? ItemEntry.builder(ModItems.ELDER_GUARDIAN_SPIKE_SHARD) : ItemEntry.builder(ModItems.SHIELD_FRAMING_SMITHING_TEMPLATE))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
        }
    }
    
}
