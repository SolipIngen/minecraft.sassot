package solipingen.sassot.item;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;

import java.util.function.Function;


public class ModItems {

    // Spears
    public static final float SWORD_BASE_ATTACK_DAMAGE = 3.0f;
    public static final float SPEAR_BASE_ATTACK_DAMAGE = 4.0f;

    public static final Item WOODEN_SPEAR = ModItems.registerItem("wooden_spear", 
            settings -> new SpearItem(ToolMaterial.WOOD, settings), new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterial.WOOD, SPEAR_BASE_ATTACK_DAMAGE, -2.3f)));
    
    public static final Item BAMBOO_SPEAR = ModItems.registerItem("bamboo_spear", 
            settings -> new SpearItem(ToolMaterial.WOOD, settings), new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterial.WOOD, SPEAR_BASE_ATTACK_DAMAGE, -2.2f)));
    
    public static final Item STONE_SPEAR = ModItems.registerItem("stone_spear",
            settings -> new SpearItem(ToolMaterial.STONE, settings), new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterial.STONE, SPEAR_BASE_ATTACK_DAMAGE, -2.8f)).repairable(Items.FLINT));
    
    public static final Item FLINT_SPEAR = ModItems.registerItem("flint_spear",
            settings -> new SpearItem(ToolMaterial.STONE, settings), new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterial.STONE, SPEAR_BASE_ATTACK_DAMAGE, -2.5f)));
    
    public static final Item COPPER_SPEAR = ModItems.registerItem("copper_spear",
            settings -> new SpearItem(ModToolMaterials.COPPER, settings), new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ModToolMaterials.COPPER, SPEAR_BASE_ATTACK_DAMAGE, -2.6f)));
    
    public static final Item GOLDEN_SPEAR = ModItems.registerItem("golden_spear",
            settings -> new SpearItem(ToolMaterial.GOLD, settings), new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterial.GOLD, SPEAR_BASE_ATTACK_DAMAGE, -2.6f)));

    public static final Item IRON_SPEAR = ModItems.registerItem("iron_spear",
            settings -> new SpearItem(ToolMaterial.IRON, settings), new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterial.IRON, SPEAR_BASE_ATTACK_DAMAGE, -2.5f)));

    public static final Item DIAMOND_SPEAR = ModItems.registerItem("diamond_spear",
            settings -> new SpearItem(ToolMaterial.DIAMOND, settings), new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterial.DIAMOND, SPEAR_BASE_ATTACK_DAMAGE, -2.3f)));
    
    public static final Item NETHERITE_SPEAR = ModItems.registerItem("netherite_spear",
            settings -> new SpearItem(ToolMaterial.NETHERITE, settings), new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterial.NETHERITE, SPEAR_BASE_ATTACK_DAMAGE, -2.2f)).fireproof());

    public static final Item BLAZEARM = ModItems.registerItem("blazearm",
            BlazearmItem::new, new Item.Settings().attributeModifiers(BlazearmItem.createAttributeModifiers())
                    .enchantable(15).repairable(Items.NETHERITE_INGOT)
                    .fireproof().maxDamage(1808).rarity(Rarity.UNCOMMON));

    
    // Shields
    public static final Item WOODEN_SHIELD = ModItems.registerItem("wooden_shield",
            settings -> new ModShieldItem(ToolMaterial.WOOD, 2.0f, false, 0.15f,0.6f, 0.05f, settings), new Item.Settings());
    
    public static final Item COPPER_FRAMED_WOODEN_SHIELD = ModItems.registerItem("copper_framed_wooden_shield",
            settings -> new ModShieldItem(ModToolMaterials.COPPER, 3.0f, true, 0.2f, 0.6f, 0.05f, settings), new Item.Settings());

    public static final Item GOLD_FRAMED_WOODEN_SHIELD = ModItems.registerItem("gold_framed_wooden_shield",
            settings -> new ModShieldItem(ToolMaterial.GOLD, 3.0f, true, 0.2f, 0.6f, 0.05f, settings), new Item.Settings());
    
    public static final Item IRON_FRAMED_WOODEN_SHIELD = ModItems.registerItem("iron_framed_wooden_shield",
            settings -> new ModShieldItem(ToolMaterial.IRON, 3.5f, true, 0.2f, 0.6f, 0.05f, settings), new Item.Settings());

    public static final Item DIAMOND_FRAMED_WOODEN_SHIELD = ModItems.registerItem("diamond_framed_wooden_shield",
            settings -> new ModShieldItem(ToolMaterial.DIAMOND, 4.0f, true, 0.2f,0.6f, 0.05f, settings), new Item.Settings());

    public static final Item EMERALD_FRAMED_WOODEN_SHIELD = ModItems.registerItem("emerald_framed_wooden_shield",
            settings -> new ModShieldItem(ModToolMaterials.EMERALD, 3.0f, true, 0.2f, 0.6f, 0.05f, settings), new Item.Settings());

    public static final Item NETHERITE_FRAMED_WOODEN_SHIELD = ModItems.registerItem("netherite_framed_wooden_shield",
            settings -> new ModShieldItem(ToolMaterial.NETHERITE, 4.0f, true, 0.2f, 0.6f, 0.05f, settings), new Item.Settings());
    
    public static final Item COPPER_SHIELD = ModItems.registerItem("copper_shield",
            settings -> new ModShieldItem(ModToolMaterials.COPPER, 4.0f, false, 0.25f, 0.8f, 0.075f, settings), new Item.Settings());
    
    public static final Item GOLDEN_SHIELD = ModItems.registerItem("golden_shield",
            settings -> new ModShieldItem(ToolMaterial.GOLD, 4.0f, false, 0.25f, 0.8f, 0.075f, settings), new Item.Settings());
    
    public static final Item IRON_SHIELD = ModItems.registerItem("iron_shield",
            settings -> new ModShieldItem(ToolMaterial.IRON, 5.0f, false, 0.3f, 1.0f, 0.1f, settings), new Item.Settings());
    
    public static final Item DIAMOND_SHIELD = ModItems.registerItem("diamond_shield",
            settings -> new ModShieldItem(ToolMaterial.DIAMOND, 5.5f, false, 0.35f, 1.2f, 0.125f, settings), new Item.Settings());
    
    public static final Item NETHERITE_SHIELD = ModItems.registerItem("netherite_shield",
            settings -> new ModShieldItem(ToolMaterial.NETHERITE, 6.0f, false, 0.4f, 1.4f, 0.15f, settings), new Item.Settings().fireproof());

    
    // Copper Tools
    public static final Item COPPER_SWORD = ModItems.registerItem("copper_sword",
            Item::new, new Item.Settings().sword(ModToolMaterials.COPPER, SWORD_BASE_ATTACK_DAMAGE, -2.3f));
    
    public static final Item COPPER_SHOVEL = ModItems.registerItem("copper_shovel",
            settings -> new ShovelItem(ModToolMaterials.COPPER, 1.0f, -2.5f, settings), new Item.Settings());
    
    public static final Item COPPER_PICKAXE = ModItems.registerItem("copper_pickaxe", 
            Item::new, new Item.Settings().pickaxe(ModToolMaterials.COPPER, 2, -2.6f));
    
    public static final Item COPPER_AXE = ModItems.registerItem("copper_axe",
            settings -> new AxeItem(ModToolMaterials.COPPER, 6.0f, -3.0f, settings), new Item.Settings());

    public static final Item COPPER_HOE = ModItems.registerItem("copper_hoe",
            settings -> new HoeItem(ModToolMaterials.COPPER,  2, -2.3f, settings), new Item.Settings());

    
    // Fishing Rods
    public static final Item COPPER_FUSED_FISHING_ROD = ModItems.registerItem("copper_fused_fishing_rod", 
            settings -> new ModFishingRodItem(settings, ModToolMaterials.COPPER), new Item.Settings());
    
    public static final Item IRON_FUSED_FISHING_ROD = ModItems.registerItem("iron_fused_fishing_rod",
            settings -> new ModFishingRodItem(settings, ToolMaterial.IRON), new Item.Settings());

    public static final Item GOLD_FUSED_FISHING_ROD = ModItems.registerItem("gold_fused_fishing_rod",
            settings -> new ModFishingRodItem(settings, ToolMaterial.GOLD), new Item.Settings());

    public static final Item DIAMOND_FUSED_FISHING_ROD = ModItems.registerItem("diamond_fused_fishing_rod",
            settings -> new ModFishingRodItem(settings, ToolMaterial.DIAMOND), new Item.Settings());

    public static final Item NETHERITE_FUSED_FISHING_ROD = ModItems.registerItem("netherite_fused_fishing_rod",
            settings -> new ModFishingRodItem(settings, ToolMaterial.NETHERITE), new Item.Settings().fireproof());


    // On A Stick Items
    public static final Item CARROT_ON_A_COPPER_FUSED_STICK = ModItems.registerItem("carrot_on_a_copper_fused_stick", 
           settings -> new ModOnAStickItem<PigEntity>(settings, EntityType.PIG, ModToolMaterials.COPPER, 7), new Item.Settings().maxDamage(25));

    public static final Item WARPED_FUNGUS_ON_A_COPPER_FUSED_STICK = ModItems.registerItem("warped_fungus_on_a_copper_fused_stick",
            settings -> new ModOnAStickItem<StriderEntity>(settings, EntityType.STRIDER, ModToolMaterials.COPPER, 1), new Item.Settings().maxDamage(100));

    public static final Item CARROT_ON_AN_IRON_FUSED_STICK = ModItems.registerItem("carrot_on_an_iron_fused_stick",
            settings -> new ModOnAStickItem<PigEntity>(settings, EntityType.PIG, ToolMaterial.IRON, 7), new Item.Settings().maxDamage(25));
        
    public static final Item WARPED_FUNGUS_ON_AN_IRON_FUSED_STICK = ModItems.registerItem("warped_fungus_on_an_iron_fused_stick",
            settings -> new ModOnAStickItem<StriderEntity>(settings, EntityType.STRIDER, ToolMaterial.IRON, 1), new Item.Settings().maxDamage(100));

    public static final Item CARROT_ON_A_GOLD_FUSED_STICK = ModItems.registerItem("carrot_on_a_gold_fused_stick",
            settings -> new ModOnAStickItem<PigEntity>(settings, EntityType.PIG, ToolMaterial.GOLD, 7), new Item.Settings().maxDamage(25));
        
    public static final Item WARPED_FUNGUS_ON_A_GOLD_FUSED_STICK = ModItems.registerItem("warped_fungus_on_a_gold_fused_stick",
            settings -> new ModOnAStickItem<StriderEntity>(settings, EntityType.STRIDER, ToolMaterial.GOLD, 1), new Item.Settings().maxDamage(100));

    public static final Item CARROT_ON_A_DIAMOND_FUSED_STICK = ModItems.registerItem("carrot_on_a_diamond_fused_stick",
            settings -> new ModOnAStickItem<PigEntity>(settings, EntityType.PIG, ToolMaterial.DIAMOND, 7), new Item.Settings().maxDamage(25));
        
    public static final Item WARPED_FUNGUS_ON_A_DIAMOND_FUSED_STICK = ModItems.registerItem("warped_fungus_on_a_diamond_fused_stick",
            settings -> new ModOnAStickItem<StriderEntity>(settings, EntityType.STRIDER, ToolMaterial.DIAMOND, 1), new Item.Settings().maxDamage(100));

    public static final Item CARROT_ON_A_NETHERITE_FUSED_STICK = ModItems.registerItem("carrot_on_a_netherite_fused_stick",
            settings -> new ModOnAStickItem<PigEntity>(settings, EntityType.PIG, ToolMaterial.NETHERITE, 7), new Item.Settings().maxDamage(25).fireproof());
        
    public static final Item WARPED_FUNGUS_ON_A_NETHERITE_FUSED_STICK = ModItems.registerItem("warped_fungus_on_a_netherite_fused_stick",
            settings -> new ModOnAStickItem<StriderEntity>(settings, EntityType.STRIDER, ToolMaterial.NETHERITE, 1), new Item.Settings().maxDamage(100).fireproof());
    
    
    // Shield Framing Template
    public static final Item SHIELD_FRAMING_SMITHING_TEMPLATE = ModItems.registerItem("shield_framing_smithing_template", 
        ShieldFramingTemplateItem::createShieldFramingTemplate, new Item.Settings());
    
    // Fishing Rod Fusion Template
    public static final Item FISHING_ROD_FUSION_SMITHING_TEMPLATE = ModItems.registerItem("fishing_rod_fusion_smithing_template", 
        FishingRodFusionTemplateItem::createFishingRodFusionTemplate, new Item.Settings());


    // Elder Guardian Spike
    public static final Item ELDER_GUARDIAN_SPIKE_SHARD = ModItems.registerItem("elder_guardian_spike_shard",
            ElderGuardianSpikeShardItem::new, new Item.Settings().attributeModifiers(ElderGuardianSpikeShardItem.createAttributeModifiers()).maxDamage(104).rarity(Rarity.UNCOMMON));

    public static final Item ELDER_GUARDIAN_SPIKE_BONE_SHARD = ModItems.registerItem("elder_guardian_spike_bone_shard", 
            Item::new, new Item.Settings().rarity(Rarity.UNCOMMON));


    // Registering Methods
    private static Item registerItem(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        return Registry.register(Registries.ITEM, itemKey, item);
    }

    public static void registerModItems() {
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            builder.add(WOODEN_SHIELD, 700);
            builder.add(WOODEN_SPEAR, 300);
            builder.add(BAMBOO_SPEAR, 300);
        });
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Items for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }
    
}
