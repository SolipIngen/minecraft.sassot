package solipingen.sassot.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.mixin.item.accessors.HoeItemInvoker;


public class ModItems {

    // Spears
    public static final Item WOODEN_SPEAR = ModItems.registerItem("wooden_spear", 
        new SpearItem(ToolMaterials.WOOD, 4.0f, -2.3, new FabricItemSettings()));
    
    public static final Item BAMBOO_SPEAR = ModItems.registerItem("bamboo_spear", 
        new SpearItem(ToolMaterials.WOOD, 5.0f, -2.2, new FabricItemSettings()));
    
    public static final Item STONE_SPEAR = ModItems.registerItem("stone_spear", 
        new SpearItem(ToolMaterials.STONE, 6.0f, -2.8, new FabricItemSettings()));
    
    public static final Item FLINT_SPEAR = ModItems.registerItem("flint_spear", 
        new SpearItem(ToolMaterials.STONE, 6.0f, -2.5, new FabricItemSettings()));
    
    public static final Item COPPER_SPEAR = ModItems.registerItem("copper_spear", 
        new SpearItem(ModToolMaterials.COPPER, 7.0f, -2.6, new FabricItemSettings()));
    
    public static final Item GOLDEN_SPEAR = ModItems.registerItem("golden_spear", 
        new SpearItem(ToolMaterials.GOLD, 7.0f, -2.6, new FabricItemSettings()));

    public static final Item IRON_SPEAR = ModItems.registerItem("iron_spear", 
        new SpearItem(ToolMaterials.IRON, 8.0f, -2.5, new FabricItemSettings()));

    public static final Item DIAMOND_SPEAR = ModItems.registerItem("diamond_spear", 
        new SpearItem(ToolMaterials.DIAMOND, 10.0f, -2.3, new FabricItemSettings()));
    
    public static final Item NETHERITE_SPEAR = ModItems.registerItem("netherite_spear", 
        new SpearItem(ToolMaterials.NETHERITE, 11.0f, -2.2, new FabricItemSettings().fireproof()));

    public static final Item BLAZEARM = ModItems.registerItem("blazearm", 
        new BlazearmItem(new FabricItemSettings().fireproof().maxDamage(1808)));

    
    // Shields
    public static final Item WOODEN_SHIELD = ModItems.registerItem("wooden_shield", 
        new ModShieldItem(ToolMaterials.WOOD, 1.0f, false, 60, 0.05f, new FabricItemSettings()));
    
    public static final Item COPPER_FRAMED_WOODEN_SHIELD = ModItems.registerItem("copper_framed_wooden_shield", 
        new ModShieldItem(ModToolMaterials.COPPER, 1.0f, true, 60, 0.05f, new FabricItemSettings()));

    public static final Item GOLD_FRAMED_WOODEN_SHIELD = ModItems.registerItem("gold_framed_wooden_shield", 
        new ModShieldItem(ToolMaterials.GOLD, 1.0f, true, 60, 0.05f, new FabricItemSettings()));
    
    public static final Item IRON_FRAMED_WOODEN_SHIELD = ModItems.registerItem("iron_framed_wooden_shield", 
        new ModShieldItem(ToolMaterials.IRON, 1.0f, true, 60, 0.05f, new FabricItemSettings()));
    
    public static final Item COPPER_SHIELD = ModItems.registerItem("copper_shield", 
        new ModShieldItem(ModToolMaterials.COPPER, 3.0f, false, 70, 0.075f, new FabricItemSettings()));
    
    public static final Item GOLDEN_SHIELD = ModItems.registerItem("golden_shield", 
        new ModShieldItem(ToolMaterials.GOLD, 3.0f, false, 70, 0.075f, new FabricItemSettings()));
    
    public static final Item IRON_SHIELD = ModItems.registerItem("iron_shield", 
        new ModShieldItem(ToolMaterials.IRON, 4.0f, false, 80, 0.1f, new FabricItemSettings()));
    
    public static final Item DIAMOND_SHIELD = ModItems.registerItem("diamond_shield", 
        new ModShieldItem(ToolMaterials.DIAMOND, 6.0f, false, 100, 0.15f, new FabricItemSettings()));
    
    public static final Item NETHERITE_SHIELD = ModItems.registerItem("netherite_shield", 
        new ModShieldItem(ToolMaterials.DIAMOND, 7.0f, false, 120, 0.175f, new FabricItemSettings().fireproof()));

    
    // Copper Tools
    public static final Item COPPER_SWORD = ModItems.registerItem("copper_sword", 
        (Item)new SwordItem(ModToolMaterials.COPPER, 4, -2.3f, new FabricItemSettings()));
    
    public static final Item COPPER_SHOVEL = ModItems.registerItem("copper_shovel", 
        (Item)new ShovelItem(ModToolMaterials.COPPER, 1.0f, -2.5f, new FabricItemSettings()));
    
    public static final Item COPPER_PICKAXE = ModItems.registerItem("copper_pickaxe", 
        (Item)new PickaxeItem(ModToolMaterials.COPPER, 2, -2.6f, new FabricItemSettings()));
    
    public static final Item COPPER_AXE = ModItems.registerItem("copper_axe", 
        (Item)new AxeItem(ModToolMaterials.COPPER, 6.0f, -3.0f, new FabricItemSettings()));
    
    public static final Item COPPER_HOE = ModItems.registerItem("copper_hoe", 
        (Item)HoeItemInvoker.invokeHoeItem(ModToolMaterials.COPPER, 2, -2.3f, new FabricItemSettings()));

    
    // Shield Framing Template
    public static final Item SHIELD_FRAMING_SMITHING_TEMPLATE = ModItems.registerItem("shield_framing_smithing_template", 
        (Item)ShieldFramingTemplateItem.createShieldFramingTemplate());


    // Elder Guardian Spike
    public static final Item ELDER_GUARDIAN_SPIKE_SHARD = ModItems.registerItem("elder_guardian_spike_shard", 
        new ElderGuardianSpikeShardItem(1, -1.0f, new FabricItemSettings().maxDamage(104)));

    public static final Item ELDER_GUARDIAN_SPIKE_BONE_SHARD = ModItems.registerItem("elder_guardian_spike_bone_shard", 
        new Item(new FabricItemSettings()));

    
    // Replace Vanilla Items with Mod Versions
    public static void replaceVanillaItems() {
        for (Item item : Registries.ITEM) {
            String name = item.getTranslationKey().substring(item.getTranslationKey().lastIndexOf(".") + 1);
            int rawId = Registries.ITEM.getRawId(item);
            if (item instanceof SwordItem) {
                if (name.matches("wooden_sword")) {
                    Item newSwordItem = (Item)new SwordItem(ToolMaterials.WOOD, 2, -1.8f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newSwordItem);
                }
                else if (name.matches("stone_sword")) {
                    Item newSwordItem = (Item)new SwordItem(ToolMaterials.STONE, 3, -2.4f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newSwordItem);
                }
                else if (name.matches("golden_sword")) {
                    Item newSwordItem = (Item)new SwordItem(ToolMaterials.GOLD, 4, -2.3f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newSwordItem);
                }
                else if (name.matches("iron_sword")) {
                    Item newSwordItem = (Item)new SwordItem(ToolMaterials.IRON, 5, -2.2f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newSwordItem);
                }
                else if (name.matches("diamond_sword")) {
                    Item newSwordItem = (Item)new SwordItem(ToolMaterials.DIAMOND, 6, -2.0f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newSwordItem);
                }
                else if (name.matches("netherite_sword")) {
                    Item newSwordItem = (Item)new SwordItem(ToolMaterials.NETHERITE, 6, -1.9f, new Item.Settings().fireproof());
                    Registry.register(Registries.ITEM, rawId, name, newSwordItem);
                }
            }
            else if (item instanceof AxeItem) {
                if  (name.matches("wooden_axe")) {
                    Item newAxeItem = (Item)new AxeItem(ToolMaterials.WOOD, 3.0f, -2.5f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newAxeItem);
                }
                else if (name.matches("stone_axe")) {
                    Item newAxeItem = (Item)new AxeItem(ToolMaterials.STONE, 5.0f, -3.2f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newAxeItem);
                }
                else if (name.matches("golden_axe")) {
                    Item newAxeItem = (Item)new AxeItem(ToolMaterials.GOLD, 6.0f, -3.0f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newAxeItem);
                }
                else if (name.matches("iron_axe")) {
                    Item newAxeItem = (Item)new AxeItem(ToolMaterials.IRON, 7.0f, -2.9f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newAxeItem);
                }
                else if (name.matches("diamond_axe")) {
                    Item newAxeItem = (Item)new AxeItem(ToolMaterials.DIAMOND, 8.0f, -2.7f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newAxeItem);
                }
                else if (name.matches("netherite_axe")) {
                    Item newAxeItem = (Item)new AxeItem(ToolMaterials.NETHERITE, 8.0f, -2.6f, new Item.Settings().fireproof());
                    Registry.register(Registries.ITEM, rawId, name, newAxeItem);
                }
            }
            else if (item instanceof TridentItem) {
                Item newTridentItem = (Item)new TridentItem(new Item.Settings().maxDamage(1095));
                Registry.register(Registries.ITEM, rawId, name, newTridentItem);
            }
            else if (item instanceof PickaxeItem) {
                if  (name.matches("wooden_pickaxe")) {
                    Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.WOOD, 2, -2.4f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newPickaxeItem);
                }
                else if (name.matches("stone_pickaxe")) {
                    Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.STONE, 2, -2.8f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newPickaxeItem);
                }
                else if (name.matches("golden_pickaxe")) {
                    Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.GOLD, 2, -2.6f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newPickaxeItem);
                }
                else if (name.matches("iron_pickaxe")) {
                    Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.IRON, 2, -2.5f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newPickaxeItem);
                }
                else if (name.matches("diamond_pickaxe")) {
                    Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.DIAMOND, 2, -2.5f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newPickaxeItem);
                }
                else if (name.matches("netherite_pickaxe")) {
                    Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.NETHERITE, 2, -2.5f, new Item.Settings().fireproof());
                    Registry.register(Registries.ITEM, rawId, name, newPickaxeItem);
                }
            }
            else if (item instanceof ShovelItem) {
                if  (name.matches("wooden_shovel")) {
                    Item newShovelItem = (Item)new ShovelItem(ToolMaterials.WOOD, 1.0f, -2.3f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newShovelItem);
                }
                else if (name.matches("stone_shovel")) {
                    Item newShovelItem = (Item)new ShovelItem(ToolMaterials.STONE, 1.0f, -2.7f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newShovelItem);
                }
                else if (name.matches("golden_shovel")) {
                    Item newShovelItem = (Item)new ShovelItem(ToolMaterials.GOLD, 1.0f, -2.5f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newShovelItem);
                }
                else if (name.matches("iron_shovel")) {
                    Item newShovelItem = (Item)new ShovelItem(ToolMaterials.IRON, 1.0f, -2.4f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newShovelItem);
                }
                else if (name.matches("diamond_shovel")) {
                    Item newShovelItem = (Item)new ShovelItem(ToolMaterials.DIAMOND, 1.0f, -2.4f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newShovelItem);
                }
                else if (name.matches("netherite_shovel")) {
                    Item newShovelItem = (Item)new ShovelItem(ToolMaterials.NETHERITE, 1.0f, -2.4f, new Item.Settings().fireproof());
                    Registry.register(Registries.ITEM, rawId, name, newShovelItem);
                }
            }
            else if (item instanceof HoeItem) {
                if  (name.matches("wooden_hoe")) {
                    Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.WOOD, 2, -2.2f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newHoeItem);
                }
                else if (name.matches("stone_hoe")) {
                    Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.STONE, 2, -2.4f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newHoeItem);
                }
                else if (name.matches("golden_hoe")) {
                    Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.GOLD, 2, -2.3f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newHoeItem);
                }
                else if (name.matches("iron_hoe")) {
                    Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.IRON, 2, -2.3f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newHoeItem);
                }
                else if (name.matches("diamond_hoe")) {
                    Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.DIAMOND, 2, -2.3f, new Item.Settings());
                    Registry.register(Registries.ITEM, rawId, name, newHoeItem);
                }
                else if (name.matches("netherite_hoe")) {
                    Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.NETHERITE, 2, -2.3f, new Item.Settings().fireproof());
                    Registry.register(Registries.ITEM, rawId, name, newHoeItem);
                }
            }
        }
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Replacing Vanilla Items for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

    // Registering Methods
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Items for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }
    
}
