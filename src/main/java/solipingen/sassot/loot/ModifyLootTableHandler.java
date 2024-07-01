package solipingen.sassot.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.*;
import solipingen.sassot.item.ModItems;


public class ModifyLootTableHandler implements LootTableEvents.Modify {


    @Override
    public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source) {
        LootPool.Builder poolBuilder = LootPool.builder();
        if (key.getValue().getPath().endsWith("elder_guardian")) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(KilledByPlayerLootCondition.builder())
                    .with(ItemEntry.builder(ModItems.ELDER_GUARDIAN_SPIKE_SHARD))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)).build());
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.ABANDONED_MINESHAFT_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(24))
                    .with(ItemEntry.builder(ModItems.COPPER_PICKAXE).weight(7));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.BURIED_TREASURE_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(5))
                    .with(ItemEntry.builder(ModItems.IRON_SPEAR).weight(1));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.UNDERWATER_RUIN_BIG_CHEST) || key.equals(LootTables.UNDERWATER_RUIN_SMALL_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(7))
                    .with(ItemEntry.builder(ModItems.FISHING_ROD_FUSION_SMITHING_TEMPLATE).weight(1));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.SHIPWRECK_SUPPLY_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(8))
                    .with(ItemEntry.builder(Items.FISHING_ROD).weight(4))
                    .with(ItemEntry.builder(ModItems.COPPER_FUSED_FISHING_ROD).weight(2))
                    .with(ItemEntry.builder(ModItems.IRON_FUSED_FISHING_ROD).weight(1));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.SHIPWRECK_TREASURE_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(6))
                    .with(ItemEntry.builder(ModItems.GOLD_FUSED_FISHING_ROD).weight(1));
//                        .apply(EnchantRandomlyLootFunction.builder());
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.RUINED_PORTAL_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(7))
                    .with(ItemEntry.builder(ModItems.GOLDEN_SPEAR).weight(1));
//                        .apply(EnchantRandomlyLootFunction.builder());
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.NETHER_BRIDGE_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(5))
                    .with(ItemEntry.builder(ModItems.GOLDEN_SPEAR).weight(1));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.BASTION_TREASURE_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(6))
                    .with(ItemEntry.builder(ModItems.DIAMOND_SPEAR).weight(1))
                    .with(ItemEntry.builder(ModItems.DIAMOND_SPEAR).weight(1))
                    .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.8f, 1.0f)));
//                            .apply(EnchantRandomlyLootFunction.builder());
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.BASTION_BRIDGE_CHEST) || key.equals(LootTables.BASTION_OTHER_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(4))
                    .with(ItemEntry.builder(ModItems.GOLDEN_SPEAR).weight(1));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.STRONGHOLD_CORRIDOR_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(5))
                    .with(ItemEntry.builder(ModItems.IRON_SPEAR).weight(1));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.END_CITY_TREASURE_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(8))
                    .with(ItemEntry.builder(ModItems.IRON_SPEAR).weight(2))
                    .apply(new EnchantWithLevelsLootFunction.Builder(UniformLootNumberProvider.create(20.0f, 39.0f)))
                    .with(ItemEntry.builder(ModItems.DIAMOND_SPEAR).weight(1))
                    .apply(new EnchantWithLevelsLootFunction.Builder(UniformLootNumberProvider.create(20.0f, 39.0f)));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.TRIAL_CHAMBERS_CORRIDOR_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(10))
                    .with(ItemEntry.builder(ModItems.COPPER_AXE).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))).weight(1))
                    .with(ItemEntry.builder(ModItems.COPPER_PICKAXE).apply(SetDamageLootFunction.builder(ConstantLootNumberProvider.create(1.0f))).weight(1));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.FISHING_TREASURE_GAMEPLAY)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(6))
                    .with(ItemEntry.builder(ModItems.FISHING_ROD_FUSION_SMITHING_TEMPLATE).weight(1));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.VILLAGE_TOOLSMITH_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(3))
                    .with(ItemEntry.builder(ModItems.COPPER_PICKAXE).weight(1))
                    .with(ItemEntry.builder(ModItems.COPPER_SHOVEL).weight(1));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.VILLAGE_WEAPONSMITH_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(3))
                    .with(ItemEntry.builder(ModItems.COPPER_PICKAXE).weight(1))
                    .with(ItemEntry.builder(ModItems.COPPER_SWORD).weight(1))
                    .with(ItemEntry.builder(ModItems.SHIELD_FRAMING_SMITHING_TEMPLATE).weight(2));
            tableBuilder.pool(poolBuilder.build());
        }
        else if (key.equals(LootTables.TRIAL_CHAMBERS_REWARD_CHEST)) {
            poolBuilder.rolls(ConstantLootNumberProvider.create(1))
                    .with(EmptyEntry.builder().weight(15))
                    .with(ItemEntry.builder(ModItems.COPPER_FRAMED_WOODEN_SHIELD).weight(1)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))))
                    .with(ItemEntry.builder(ModItems.COPPER_SHIELD).weight(1)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))))
                    .with(ItemEntry.builder(ModItems.IRON_FRAMED_WOODEN_SHIELD).weight(1)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))))
                    .with(ItemEntry.builder(ModItems.IRON_SHIELD).weight(1)
                            .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15f, 0.8f))));
//                            .with(ItemEntry.builder(Items.BOOK).weight(1)
//                                    .apply(EnchantRandomlyLootFunction.create()
//                                            .option(ModEnchantments.SHIELDING)
//                                            .add(ModEnchantments.UNYIELDING)
//                                            .add(ModEnchantments.SHOCK_REBOUND)
//                                            .add(ModEnchantments.PROJECTILE_DEFLECTION)
//                                            .add(ModEnchantments.CLOAKING)));
            tableBuilder.pool(poolBuilder.build());
        }
    }
    
}
