package solipingen.sassot.enchantment;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.enchantment.axe.HackingEnchantment;
import solipingen.sassot.enchantment.shield.CloakingEnchantment;
import solipingen.sassot.enchantment.shield.EchoingEnchantment;
import solipingen.sassot.enchantment.shield.ProjectileDeflectionEnchantment;
import solipingen.sassot.enchantment.shield.ShockReboundEnchantment;
import solipingen.sassot.enchantment.spear.GroundshakingEnchantment;
import solipingen.sassot.enchantment.spear.SkeweringEnchantment;
import solipingen.sassot.enchantment.spear.ThrustingEnchantment;
import solipingen.sassot.enchantment.spear.WhirlwindEnchantment;
import solipingen.sassot.registry.tag.ModEntityTypeTags;
import solipingen.sassot.registry.tag.ModItemTags;

import java.util.Optional;


public class ModEnchantments {
    private static final EquipmentSlot[] HAND_SLOTS = {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

    // Axes
    public static final Enchantment HACKING = ModEnchantments.registerEnchantment("hacking", 
        new HackingEnchantment(Enchantment.properties(ModItemTags.HACKING_WEAPONS, 2, 3, Enchantment.leveledCost(5, 9), Enchantment.leveledCost(20, 9), 4, EquipmentSlot.MAINHAND)));

        
    // Shields
    public static final Enchantment SHIELDING = ModEnchantments.registerEnchantment("shielding", 
        new Enchantment(Enchantment.properties(ModItemTags.SHIELD_ENCHANTABLE, 10, 4, Enchantment.leveledCost(1, 10), Enchantment.constantCost(50), 1, HAND_SLOTS)));
    
    public static final Enchantment UNYIELDING = ModEnchantments.registerEnchantment("unyielding", 
        new Enchantment(Enchantment.properties(ModItemTags.SHIELD_ENCHANTABLE, 5, 3, Enchantment.leveledCost(5, 7), Enchantment.constantCost(50), 2, HAND_SLOTS)));
    
    public static final Enchantment PROJECTILE_DEFLECTION = ModEnchantments.registerEnchantment("projectile_deflection", 
        new ProjectileDeflectionEnchantment(Enchantment.properties(ModItemTags.SHIELD_ENCHANTABLE, 2, 3, Enchantment.leveledCost(10, 20), Enchantment.constantCost(50), 4, HAND_SLOTS)));

    public static final Enchantment SHOCK_REBOUND = ModEnchantments.registerEnchantment("shock_rebound", 
        new ShockReboundEnchantment(Enchantment.properties(ModItemTags.SHIELD_ENCHANTABLE, 2, 3, Enchantment.leveledCost(10, 20), Enchantment.constantCost(50), 4, HAND_SLOTS)));

    public static final Enchantment ECHOING = ModEnchantments.registerEnchantment("echoing", 
        new EchoingEnchantment(Enchantment.properties(ModItemTags.SHIELD_ENCHANTABLE, 1, 3, Enchantment.leveledCost(25, 25), Enchantment.leveledCost(75, 25), 8, HAND_SLOTS)));
    
    public static final Enchantment CLOAKING = ModEnchantments.registerEnchantment("cloaking",
        new CloakingEnchantment(Enchantment.properties(ModItemTags.SHIELD_ENCHANTABLE, 1, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 8, HAND_SLOTS)));

    
    // Spears
    public static final Enchantment WHIRLWIND = ModEnchantments.registerEnchantment("whirlwind",
        new WhirlwindEnchantment(Enchantment.properties(ModItemTags.SPEAR_ENCHANTABLE, 2, 3, Enchantment.leveledCost(17, 7), Enchantment.constantCost(50), 4, EquipmentSlot.MAINHAND)));
    
    public static final Enchantment SKEWERING = ModEnchantments.registerEnchantment("skewering", 
        new SkeweringEnchantment(Enchantment.properties(ModItemTags.THRUSTING_WEAPONS, 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, EquipmentSlot.MAINHAND)));

    public static final Enchantment THRUSTING = ModEnchantments.registerEnchantment("thrusting", 
        new ThrustingEnchantment(Enchantment.properties(ModItemTags.THRUSTING_WEAPONS, 2, 3, Enchantment.leveledCost(5, 9), Enchantment.leveledCost(20, 9), 4, EquipmentSlot.MAINHAND)));

    public static final Enchantment GROUNDSHAKING = ModEnchantments.registerEnchantment("groundshaking", 
        new GroundshakingEnchantment(Enchantment.properties(ModItemTags.GROUNDSHAKING_ENCHANTABLE, 1, 1, Enchantment.constantCost(25), Enchantment.constantCost(50), 8, EquipmentSlot.MAINHAND)));
    
    public static final Enchantment LEANING = ModEnchantments.registerEnchantment("leaning", 
        new DamageEnchantment(Enchantment.properties(ModItemTags.BLAZEARM_ENCHANTABLE, 2, 5, Enchantment.leveledCost(1, 8), Enchantment.leveledCost(21, 8), 4, EquipmentSlot.MAINHAND), Optional.of(ModEntityTypeTags.SENSITIVE_TO_LEANING)));

    public static final Enchantment FLARE = ModEnchantments.registerEnchantment("flare", 
        new FlareEnchantment(Enchantment.properties(ModItemTags.BLAZEARM_ENCHANTABLE, 2, 3, Enchantment.leveledCost(17, 7), Enchantment.constantCost(50), 4, EquipmentSlot.MAINHAND)));


    private static Enchantment registerEnchantment(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), enchantment);
    } 

    public static void registerModEnchantments() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Enchantments for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }
    
}
