package solipingen.sassot.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.ModItems;


public class ModifyLootTableHandler implements LootTableEvents.Modify {
    private static final Identifier ELDER_GUARDIAN_DROP_ID = new Identifier("entities/elder_guardian");
    private static final Identifier ABANDONED_MINESHAFT_ID = new Identifier("chests/abandoned_mineshaft");
    private static final Identifier BURIED_TREASURE_ID = new Identifier("chests/buried_treasure");
    private static final Identifier OCEAN_RUIN_SMALL_ID = new Identifier("chests/underwater_ruin_small");
    private static final Identifier OCEAN_RUIN_BIG_ID = new Identifier("chests/underwater_ruin_big");
    private static final Identifier SHIPWRECK_SUPPLY_ID = new Identifier("chests/shipwreck_supply");
    private static final Identifier SHIPWRECK_TREASURE_ID = new Identifier("chests/shipwreck_treasure");
    private static final Identifier RUINED_PORTAL_ID = new Identifier("chests/ruined_portal");
    private static final Identifier NETHER_BRIDGE_ID = new Identifier("chests/nether_bridge");
    private static final Identifier BASTION_GENERIC_ID = new Identifier("chests/bastion_other");
    private static final Identifier BASTION_BRIDGE_ID = new Identifier("chests/bastion_bridge");
    private static final Identifier BASTION_TREASURE_ID = new Identifier("chests/bastion_treasure");
    private static final Identifier STRONGHOLD_ALTAR_ID = new Identifier("chests/stronghold_corridor");
    private static final Identifier END_CITY_ID = new Identifier("chests/end_city_treasure");
    private static final Identifier FISHING_GAMEPLAY_ID = new Identifier("gameplay/fishing/treasure");
    private static final Identifier TOOLSMITH_VILLAGER_CHEST_ID = new Identifier("chests/village/village_toolsmith");
    private static final Identifier WEAPONSMITH_VILLAGER_CHEST_ID = new Identifier("chests/village/village_weaponsmith");
    private static final Identifier TRIAL_CHAMBERS_REWARD_CHEST_ID = new Identifier("chests/trial_chambers/reward");

    private static final Identifier[] ID_ARRAY = new Identifier[]{ELDER_GUARDIAN_DROP_ID, 
        ABANDONED_MINESHAFT_ID, BURIED_TREASURE_ID, OCEAN_RUIN_SMALL_ID, OCEAN_RUIN_BIG_ID, SHIPWRECK_SUPPLY_ID, SHIPWRECK_TREASURE_ID, RUINED_PORTAL_ID, 
        NETHER_BRIDGE_ID, BASTION_GENERIC_ID, BASTION_BRIDGE_ID, BASTION_TREASURE_ID, STRONGHOLD_ALTAR_ID, END_CITY_ID, TOOLSMITH_VILLAGER_CHEST_ID, WEAPONSMITH_VILLAGER_CHEST_ID, TRIAL_CHAMBERS_REWARD_CHEST_ID,
        FISHING_GAMEPLAY_ID};


    @Override
    public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source) {
        for (Identifier identifier : ID_ARRAY) {
            if (identifier.getPath().matches(key.getValue().getPath())) {
                LootPool.Builder poolBuilder = LootPool.builder();
                if (identifier.getPath().endsWith("elder_guardian")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.33f, 0.12f))
                        .with(ItemEntry.builder(ModItems.ELDER_GUARDIAN_SPIKE_SHARD))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("mineshaft")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(24))
                        .with(ItemEntry.builder(ModItems.COPPER_PICKAXE).weight(7));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("buried_treasure")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(5))
                        .with(ItemEntry.builder(ModItems.IRON_SPEAR).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().startsWith("chests/underwater_ruin")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(7))
                        .with(ItemEntry.builder(ModItems.FISHING_ROD_FUSION_SMITHING_TEMPLATE).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("shipwreck_supply")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(8))
                        .with(ItemEntry.builder(Items.FISHING_ROD).weight(4))
                        .with(ItemEntry.builder(ModItems.COPPER_FUSED_FISHING_ROD).weight(2))
                        .with(ItemEntry.builder(ModItems.IRON_FUSED_FISHING_ROD).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("shipwreck_treasure")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(6))
                        .with(ItemEntry.builder(ModItems.GOLD_FUSED_FISHING_ROD).weight(1))
                        .apply(EnchantRandomlyLootFunction.builder());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("ruined_portal")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(7))
                        .with(ItemEntry.builder(ModItems.GOLDEN_SPEAR).weight(1))
                        .apply(EnchantRandomlyLootFunction.builder());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("nether_bridge")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(5))
                        .with(ItemEntry.builder(ModItems.GOLDEN_SPEAR).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("bastion_treasure")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(6))
                        .with(ItemEntry.builder(ModItems.DIAMOND_SPEAR).weight(1))
                        .with(ItemEntry.builder(ModItems.DIAMOND_SPEAR).weight(1))
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.8f, 1.0f))).apply(EnchantRandomlyLootFunction.builder());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("bastion_other") || identifier.getPath().endsWith("bastion_bridge")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(4))
                        .with(ItemEntry.builder(ModItems.GOLDEN_SPEAR).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("stronghold_corridor")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(5))
                        .with(ItemEntry.builder(ModItems.IRON_SPEAR).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("end_city_treasure")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(8))
                        .with(ItemEntry.builder(ModItems.IRON_SPEAR).weight(2))
                        .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f, 39.0f)).allowTreasureEnchantments())
                        .with(ItemEntry.builder(ModItems.DIAMOND_SPEAR).weight(1))
                        .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0f, 39.0f)).allowTreasureEnchantments());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().startsWith("chests") && identifier.getPath().endsWith("trial_chambers/corridor")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(10))
                        .with(ItemEntry.builder(ModItems.COPPER_AXE).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))).weight(1))
                        .with(ItemEntry.builder(ModItems.COPPER_PICKAXE).apply(SetDamageLootFunction.builder(ConstantLootNumberProvider.create(1.0f))).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().startsWith("gameplay/fishing")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                        .with(EmptyEntry.builder().weight(6))
                        .with(ItemEntry.builder(ModItems.FISHING_ROD_FUSION_SMITHING_TEMPLATE).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("village_toolsmith")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                            .with(EmptyEntry.builder().weight(3))
                            .with(ItemEntry.builder(ModItems.COPPER_PICKAXE).weight(1))
                            .with(ItemEntry.builder(ModItems.COPPER_SHOVEL).weight(1));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("village_weaponsmith")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                            .with(EmptyEntry.builder().weight(3))
                            .with(ItemEntry.builder(ModItems.COPPER_PICKAXE).weight(1))
                            .with(ItemEntry.builder(ModItems.COPPER_SWORD).weight(1))
                            .with(ItemEntry.builder(ModItems.SHIELD_FRAMING_SMITHING_TEMPLATE).weight(2));
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (identifier.getPath().endsWith("trial_chambers/reward")) {
                    poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                            .with(EmptyEntry.builder().weight(15))
                            .with(ItemEntry.builder(ModItems.COPPER_FRAMED_WOODEN_SHIELD).weight(1)
                                    .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))))
                            .with(ItemEntry.builder(ModItems.COPPER_SHIELD).weight(1)
                                    .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))))
                            .with(ItemEntry.builder(ModItems.IRON_FRAMED_WOODEN_SHIELD).weight(1)
                                    .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))))
                            .with(ItemEntry.builder(ModItems.IRON_SHIELD).weight(1)
                                    .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))))
                            .with(ItemEntry.builder(Items.BOOK).weight(1)
                                    .apply(EnchantRandomlyLootFunction.create()
                                            .add(ModEnchantments.SHIELDING)
                                            .add(ModEnchantments.UNYIELDING)
                                            .add(ModEnchantments.SHOCK_REBOUND)
                                            .add(ModEnchantments.PROJECTILE_DEFLECTION)
                                            .add(ModEnchantments.CLOAKING)));
                    tableBuilder.pool(poolBuilder.build());
                }
            }
        }
    }
    
}
