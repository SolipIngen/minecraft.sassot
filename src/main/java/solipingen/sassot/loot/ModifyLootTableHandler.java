package solipingen.sassot.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.item.Items;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.item.ModItems;


public class ModifyLootTableHandler implements LootTableEvents.Modify {
    private static final Identifier ELDER_GUARDIAN_DROP_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "entities/elder_guardian");
    private static final Identifier ABANDONED_MINESHAFT_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/abandoned_mineshaft");
    private static final Identifier OCEAN_RUIN_SMALL_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/underwater_ruin_small");
    private static final Identifier OCEAN_RUIN_BIG_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/underwater_ruin_big");
    private static final Identifier SHIPWRECK_SUPPLY_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/shipwreck_supply");
    private static final Identifier SHIPWRECK_TREASURE_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "chests/shipwreck_treasure");
    private static final Identifier FISHING_GAMEPLAY_ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "gameplay/fishing/treasure");
    private static final Identifier[] ID_ARRAY = new Identifier[]{ELDER_GUARDIAN_DROP_ID, 
        ABANDONED_MINESHAFT_ID, OCEAN_RUIN_SMALL_ID, OCEAN_RUIN_BIG_ID, SHIPWRECK_SUPPLY_ID, SHIPWRECK_TREASURE_ID, FISHING_GAMEPLAY_ID};


    @Override
    public void modifyLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable.Builder tableBuilder, LootTableSource source) {
        for (Identifier identifier : ID_ARRAY) {
            if (id.getPath().equals(identifier.getPath())) {
                LootPool.Builder poolBuilder = LootPool.builder();
                if (id.getPath().endsWith("elder_guardian")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.33f, 0.12f))
                        .with(ItemEntry.builder(ModItems.ELDER_GUARDIAN_SPIKE_SHARD))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (id.getPath().endsWith("mineshaft")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(24))
                        .with(ItemEntry.builder(ModItems.COPPER_PICKAXE).weight(7));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (id.getPath().startsWith("chests/underwater_ruin")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(7))
                        .with(ItemEntry.builder(ModItems.FISHING_ROD_FUSION_SMITHING_TEMPLATE).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (id.getPath().endsWith("shipwreck_supply")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(8))
                        .with(ItemEntry.builder(Items.FISHING_ROD).weight(4))
                        .with(ItemEntry.builder(ModItems.COPPER_FUSED_FISHING_ROD).weight(2))
                        .with(ItemEntry.builder(ModItems.IRON_FUSED_FISHING_ROD).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (id.getPath().endsWith("shipwreck_treasure")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(6))
                        .with(ItemEntry.builder(ModItems.GOLD_FUSED_FISHING_ROD).weight(1))
                        .apply(EnchantRandomlyLootFunction.builder());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (id.getPath().startsWith("gameplay/fishing")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(6))
                        .with(ItemEntry.builder(ModItems.FISHING_ROD_FUSION_SMITHING_TEMPLATE).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
            }
        }
    }
    
}
