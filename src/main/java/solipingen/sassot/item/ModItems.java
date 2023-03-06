package solipingen.sassot.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.mixin.item.accessors.HoeItemInvoker;


public class ModItems {

    // Spears
    public static final Item WOODEN_SPEAR = registerItem("wooden_spear", 
        new SpearItem(ToolMaterials.WOOD, 4.0f, -2.3, new FabricItemSettings()));
    
    public static final Item BAMBOO_SPEAR = registerItem("bamboo_spear", 
        new SpearItem(ToolMaterials.WOOD, 5.0f, -2.2, new FabricItemSettings()));
    
    public static final Item STONE_SPEAR = registerItem("stone_spear", 
        new SpearItem(ToolMaterials.STONE, 6.0f, -2.8, new FabricItemSettings()));
    
    public static final Item FLINT_SPEAR = registerItem("flint_spear", 
        new SpearItem(ToolMaterials.STONE, 6.0f, -2.5, new FabricItemSettings()));
    
    public static final Item COPPER_SPEAR = registerItem("copper_spear", 
        new SpearItem(ModToolMaterials.COPPER, 7.0f, -2.6, new FabricItemSettings()));
    
    public static final Item GOLDEN_SPEAR = registerItem("golden_spear", 
        new SpearItem(ToolMaterials.GOLD, 7.0f, -2.6, new FabricItemSettings()));

    public static final Item IRON_SPEAR = registerItem("iron_spear", 
        new SpearItem(ToolMaterials.IRON, 8.0f, -2.5, new FabricItemSettings()));

    public static final Item DIAMOND_SPEAR = registerItem("diamond_spear", 
        new SpearItem(ToolMaterials.DIAMOND, 10.0f, -2.3, new FabricItemSettings()));
    
    public static final Item NETHERITE_SPEAR = registerItem("netherite_spear", 
        new SpearItem(ToolMaterials.NETHERITE, 11.0f, -2.2, new FabricItemSettings().fireproof()));

    public static final Item BLAZEARM = registerItem("blazearm", 
        new BlazearmItem(new FabricItemSettings().fireproof().maxDamage(1808)));

    
    // Shields
    public static final Item WOODEN_SHIELD = registerItem("wooden_shield", 
        new ModShieldItem(ToolMaterials.WOOD, 1.0f, false, 60, 0.05f, new FabricItemSettings()));
    
    public static final Item COPPER_FRAMED_WOODEN_SHIELD = registerItem("copper_framed_wooden_shield", 
        new ModShieldItem(ModToolMaterials.COPPER, 1.0f, true, 60, 0.05f, new FabricItemSettings()));

    public static final Item GOLD_FRAMED_WOODEN_SHIELD = registerItem("gold_framed_wooden_shield", 
        new ModShieldItem(ToolMaterials.GOLD, 1.0f, true, 60, 0.05f, new FabricItemSettings()));
    
    public static final Item IRON_FRAMED_WOODEN_SHIELD = registerItem("iron_framed_wooden_shield", 
        new ModShieldItem(ToolMaterials.IRON, 1.0f, true, 60, 0.05f, new FabricItemSettings()));
    
    public static final Item COPPER_SHIELD = registerItem("copper_shield", 
        new ModShieldItem(ModToolMaterials.COPPER, 3.0f, false, 70, 0.075f, new FabricItemSettings()));
    
    public static final Item GOLDEN_SHIELD = registerItem("golden_shield", 
        new ModShieldItem(ToolMaterials.GOLD, 3.0f, false, 70, 0.075f, new FabricItemSettings()));
    
    public static final Item IRON_SHIELD = registerItem("iron_shield", 
        new ModShieldItem(ToolMaterials.IRON, 4.0f, false, 80, 0.1f, new FabricItemSettings()));
    
    public static final Item DIAMOND_SHIELD = registerItem("diamond_shield", 
        new ModShieldItem(ToolMaterials.DIAMOND, 6.0f, false, 100, 0.15f, new FabricItemSettings()));
    
    public static final Item NETHERITE_SHIELD = registerItem("netherite_shield", 
        new ModShieldItem(ToolMaterials.DIAMOND, 7.0f, false, 120, 0.175f, new FabricItemSettings().fireproof()));

    
    // Copper Tools
    public static final Item COPPER_SWORD = registerItem("copper_sword", 
        (Item)new SwordItem(ModToolMaterials.COPPER, 4, -2.3f, new FabricItemSettings()));
    
    public static final Item COPPER_SHOVEL = registerItem("copper_shovel", 
        (Item)new ShovelItem(ModToolMaterials.COPPER, 1.5f, -3.0f, new FabricItemSettings()));
    
    public static final Item COPPER_PICKAXE = registerItem("copper_pickaxe", 
        (Item)new PickaxeItem(ModToolMaterials.COPPER, 1, -2.8f, new FabricItemSettings()));
    
    public static final Item COPPER_AXE = registerItem("copper_axe", 
        (Item)new AxeItem(ModToolMaterials.COPPER, 6.0f, -3.0f, new FabricItemSettings()));
    
    public static final Item COPPER_HOE = registerItem("copper_hoe", 
        (Item)HoeItemInvoker.invokeHoeItem(ModToolMaterials.COPPER, 0, -3.0f, new FabricItemSettings()));


    // Elder Guardian Spike
    public static final Item ELDER_GUARDIAN_SPIKE_SHARD = registerItem("elder_guardian_spike_shard", 
        new ElderGuardianSpikeShardItem(1, -1.0f, new FabricItemSettings().maxDamage(89)));

    public static final Item ELDER_GUARDIAN_SPIKE_BONE_SHARD = registerItem("elder_guardian_spike_bone_shard", 
        new Item(new FabricItemSettings()));


    
    // Registering Methods
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Items for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }
    
}
