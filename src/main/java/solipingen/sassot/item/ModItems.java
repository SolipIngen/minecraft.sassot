package solipingen.sassot.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModItems {

    // Spears
    public static final float SPEAR_BASE_ATTACK_DAMAGE = 4.0f;

    public static final Item WOODEN_SPEAR = ModItems.registerItem("wooden_spear", 
        new SpearItem(ToolMaterials.WOOD, new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterials.WOOD, SPEAR_BASE_ATTACK_DAMAGE, -2.3f))));
    
    public static final Item BAMBOO_SPEAR = ModItems.registerItem("bamboo_spear", 
        new SpearItem(ToolMaterials.WOOD, new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterials.WOOD, SPEAR_BASE_ATTACK_DAMAGE, -2.2f))));
    
    public static final Item STONE_SPEAR = ModItems.registerItem("stone_spear", 
        new SpearItem(ToolMaterials.STONE, new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterials.STONE, SPEAR_BASE_ATTACK_DAMAGE, -2.8f))));
    
    public static final Item FLINT_SPEAR = ModItems.registerItem("flint_spear", 
        new SpearItem(ToolMaterials.STONE, new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterials.STONE, SPEAR_BASE_ATTACK_DAMAGE, -2.5f))));
    
    public static final Item COPPER_SPEAR = ModItems.registerItem("copper_spear", 
        new SpearItem(ModToolMaterials.COPPER, new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ModToolMaterials.COPPER, SPEAR_BASE_ATTACK_DAMAGE, -2.6f))));
    
    public static final Item GOLDEN_SPEAR = ModItems.registerItem("golden_spear", 
        new SpearItem(ToolMaterials.GOLD, new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterials.GOLD, SPEAR_BASE_ATTACK_DAMAGE, -2.6f))));

    public static final Item IRON_SPEAR = ModItems.registerItem("iron_spear", 
        new SpearItem(ToolMaterials.IRON, new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterials.IRON, SPEAR_BASE_ATTACK_DAMAGE, -2.5f))));

    public static final Item DIAMOND_SPEAR = ModItems.registerItem("diamond_spear", 
        new SpearItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterials.DIAMOND, SPEAR_BASE_ATTACK_DAMAGE, -2.3f))));
    
    public static final Item NETHERITE_SPEAR = ModItems.registerItem("netherite_spear", 
        new SpearItem(ToolMaterials.NETHERITE, new Item.Settings().attributeModifiers(SpearItem.createAttributeModifiers(ToolMaterials.NETHERITE, SPEAR_BASE_ATTACK_DAMAGE, -2.2f)).fireproof()));

    public static final Item BLAZEARM = ModItems.registerItem("blazearm", 
        new BlazearmItem(new Item.Settings().attributeModifiers(BlazearmItem.createAttributeModifiers()).fireproof().maxDamage(1808).rarity(Rarity.UNCOMMON)));

    
    // Shields
    public static final Item WOODEN_SHIELD = ModItems.registerItem("wooden_shield", 
        new ModShieldItem(ToolMaterials.WOOD, 2.0f, false, 60, 0.05f, new Item.Settings()));
    
    public static final Item COPPER_FRAMED_WOODEN_SHIELD = ModItems.registerItem("copper_framed_wooden_shield", 
        new ModShieldItem(ModToolMaterials.COPPER, 3.0f, true, 60, 0.05f, new Item.Settings()));

    public static final Item GOLD_FRAMED_WOODEN_SHIELD = ModItems.registerItem("gold_framed_wooden_shield", 
        new ModShieldItem(ToolMaterials.GOLD, 3.0f, true, 60, 0.05f, new Item.Settings()));
    
    public static final Item IRON_FRAMED_WOODEN_SHIELD = ModItems.registerItem("iron_framed_wooden_shield", 
        new ModShieldItem(ToolMaterials.IRON, 3.5f, true, 60, 0.05f, new Item.Settings()));

    public static final Item DIAMOND_FRAMED_WOODEN_SHIELD = ModItems.registerItem("diamond_framed_wooden_shield", 
        new ModShieldItem(ToolMaterials.DIAMOND, 4.0f, true, 60, 0.05f, new Item.Settings()));

    public static final Item EMERALD_FRAMED_WOODEN_SHIELD = ModItems.registerItem("emerald_framed_wooden_shield", 
        new ModShieldItem(ModToolMaterials.EMERALD, 3.0f, true, 60, 0.05f, new Item.Settings()));

    public static final Item NETHERITE_FRAMED_WOODEN_SHIELD = ModItems.registerItem("netherite_framed_wooden_shield", 
        new ModShieldItem(ToolMaterials.NETHERITE, 4.0f, true, 60, 0.05f, new Item.Settings()));
    
    public static final Item COPPER_SHIELD = ModItems.registerItem("copper_shield", 
        new ModShieldItem(ModToolMaterials.COPPER, 4.0f, false, 80, 0.075f, new Item.Settings()));
    
    public static final Item GOLDEN_SHIELD = ModItems.registerItem("golden_shield", 
        new ModShieldItem(ToolMaterials.GOLD, 4.0f, false, 80, 0.075f, new Item.Settings()));
    
    public static final Item IRON_SHIELD = ModItems.registerItem("iron_shield", 
        new ModShieldItem(ToolMaterials.IRON, 5.0f, false, 100, 0.1f, new Item.Settings()));
    
    public static final Item DIAMOND_SHIELD = ModItems.registerItem("diamond_shield", 
        new ModShieldItem(ToolMaterials.DIAMOND, 5.5f, false, 120, 0.125f, new Item.Settings()));
    
    public static final Item NETHERITE_SHIELD = ModItems.registerItem("netherite_shield", 
        new ModShieldItem(ToolMaterials.NETHERITE, 6.0f, false, 140, 0.15f, new Item.Settings().fireproof()));

    
    // Copper Tools
    public static final Item COPPER_SWORD = ModItems.registerItem("copper_sword", 
        new SwordItem(ModToolMaterials.COPPER, new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.COPPER, 4, -2.3f))));
    
    public static final Item COPPER_SHOVEL = ModItems.registerItem("copper_shovel", 
        new ShovelItem(ModToolMaterials.COPPER, new Item.Settings().attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.COPPER, 1.0f, -2.5f))));
    
    public static final Item COPPER_PICKAXE = ModItems.registerItem("copper_pickaxe", 
        new PickaxeItem(ModToolMaterials.COPPER, new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.COPPER, 2, -2.6f))));
    
    public static final Item COPPER_AXE = ModItems.registerItem("copper_axe", 
        new AxeItem(ModToolMaterials.COPPER, new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.COPPER, 6.0f, -3.0f))));

    public static final Item COPPER_HOE = ModItems.registerItem("copper_hoe", 
        new HoeItem(ModToolMaterials.COPPER,  new Item.Settings().attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.COPPER, 2, -2.3f))));

    
    // Fishing Rods
    public static final Item COPPER_FUSED_FISHING_ROD = ModItems.registerItem("copper_fused_fishing_rod", 
        new ModFishingRodItem(new Item.Settings(), ModToolMaterials.COPPER));
    
    public static final Item IRON_FUSED_FISHING_ROD = ModItems.registerItem("iron_fused_fishing_rod", 
        new ModFishingRodItem(new Item.Settings(), ToolMaterials.IRON));

    public static final Item GOLD_FUSED_FISHING_ROD = ModItems.registerItem("gold_fused_fishing_rod", 
        new ModFishingRodItem(new Item.Settings(), ToolMaterials.GOLD));

    public static final Item DIAMOND_FUSED_FISHING_ROD = ModItems.registerItem("diamond_fused_fishing_rod", 
        new ModFishingRodItem(new Item.Settings(), ToolMaterials.DIAMOND));

    public static final Item NETHERITE_FUSED_FISHING_ROD = ModItems.registerItem("netherite_fused_fishing_rod", 
        new ModFishingRodItem(new Item.Settings().fireproof(), ToolMaterials.NETHERITE));


    // On A Stick Items
    public static final Item CARROT_ON_A_COPPER_FUSED_STICK = ModItems.registerItem("carrot_on_a_copper_fused_stick", 
        new ModOnAStickItem<PigEntity>(new Item.Settings().maxDamage(25), EntityType.PIG, ModToolMaterials.COPPER, 7));

    public static final Item WARPED_FUNGUS_ON_A_COPPER_FUSED_STICK = ModItems.registerItem("warped_fungus_on_a_copper_fused_stick", 
        new ModOnAStickItem<StriderEntity>(new Item.Settings().maxDamage(100), EntityType.STRIDER, ModToolMaterials.COPPER, 1));

    public static final Item CARROT_ON_AN_IRON_FUSED_STICK = ModItems.registerItem("carrot_on_an_iron_fused_stick", 
        new ModOnAStickItem<PigEntity>(new Item.Settings().maxDamage(25), EntityType.PIG, ToolMaterials.IRON, 7));
        
    public static final Item WARPED_FUNGUS_ON_AN_IRON_FUSED_STICK = ModItems.registerItem("warped_fungus_on_an_iron_fused_stick", 
        new ModOnAStickItem<StriderEntity>(new Item.Settings().maxDamage(100), EntityType.STRIDER, ToolMaterials.IRON, 1));

    public static final Item CARROT_ON_A_GOLD_FUSED_STICK = ModItems.registerItem("carrot_on_a_gold_fused_stick", 
        new ModOnAStickItem<PigEntity>(new Item.Settings().maxDamage(25), EntityType.PIG, ToolMaterials.GOLD, 7));
        
    public static final Item WARPED_FUNGUS_ON_A_GOLD_FUSED_STICK = ModItems.registerItem("warped_fungus_on_a_gold_fused_stick", 
        new ModOnAStickItem<StriderEntity>(new Item.Settings().maxDamage(100), EntityType.STRIDER, ToolMaterials.GOLD, 1));

    public static final Item CARROT_ON_A_DIAMOND_FUSED_STICK = ModItems.registerItem("carrot_on_a_diamond_fused_stick", 
        new ModOnAStickItem<PigEntity>(new Item.Settings().maxDamage(25), EntityType.PIG, ToolMaterials.DIAMOND, 7));
        
    public static final Item WARPED_FUNGUS_ON_A_DIAMOND_FUSED_STICK = ModItems.registerItem("warped_fungus_on_a_diamond_fused_stick", 
        new ModOnAStickItem<StriderEntity>(new Item.Settings().maxDamage(100), EntityType.STRIDER, ToolMaterials.DIAMOND, 1));

    public static final Item CARROT_ON_A_NETHERITE_FUSED_STICK = ModItems.registerItem("carrot_on_a_netherite_fused_stick", 
        new ModOnAStickItem<PigEntity>(new Item.Settings().maxDamage(25).fireproof(), EntityType.PIG, ToolMaterials.NETHERITE, 7));
        
    public static final Item WARPED_FUNGUS_ON_A_NETHERITE_FUSED_STICK = ModItems.registerItem("warped_fungus_on_a_netherite_fused_stick", 
        new ModOnAStickItem<StriderEntity>(new Item.Settings().maxDamage(100).fireproof(), EntityType.STRIDER, ToolMaterials.NETHERITE, 1));
    
    
    // Shield Framing Template
    public static final Item SHIELD_FRAMING_SMITHING_TEMPLATE = ModItems.registerItem("shield_framing_smithing_template", 
        ShieldFramingTemplateItem.createShieldFramingTemplate());
    
    // Fishing Rod Fusion Template
    public static final Item FISHING_ROD_FUSION_SMITHING_TEMPLATE = ModItems.registerItem("fishing_rod_fusion_smithing_template", 
        FishingRodFusionTemplateItem.createFishingRodFusionTemplate());


    // Elder Guardian Spike
    public static final Item ELDER_GUARDIAN_SPIKE_SHARD = ModItems.registerItem("elder_guardian_spike_shard", 
        new ElderGuardianSpikeShardItem(1, -1.0f, new Item.Settings().attributeModifiers(ElderGuardianSpikeShardItem.createAttributeModifiers()).maxDamage(104).rarity(Rarity.UNCOMMON)));

    public static final Item ELDER_GUARDIAN_SPIKE_BONE_SHARD = ModItems.registerItem("elder_guardian_spike_bone_shard", 
        new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));


    // Registering Methods
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), item);
    }

    public static void registerModItems() {
        FuelRegistry.INSTANCE.add(WOODEN_SHIELD, 700);
        FuelRegistry.INSTANCE.add(WOODEN_SPEAR, 300);
        FuelRegistry.INSTANCE.add(BAMBOO_SPEAR, 300);

        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Items for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }
    
}
