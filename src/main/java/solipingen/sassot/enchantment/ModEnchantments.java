package solipingen.sassot.enchantment;

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
import solipingen.sassot.enchantment.shield.ShieldingEnchantment;
import solipingen.sassot.enchantment.shield.ShockReboundEnchantment;
import solipingen.sassot.enchantment.shield.UnyieldingEnchantment;
import solipingen.sassot.enchantment.spear.GroundbreakingEnchantment;
import solipingen.sassot.enchantment.spear.SkeweringEnchantment;
import solipingen.sassot.enchantment.spear.ThrustingEnchantment;
import solipingen.sassot.enchantment.spear.WhirlwindEnchantment;


public class ModEnchantments {
    private static final EquipmentSlot[] equipmentSlots = {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

    // Axes
    public static final Enchantment HACKING = ModEnchantments.registerEnchantment("hacking", 
        new HackingEnchantment(Enchantment.Rarity.RARE, equipmentSlots));

        
    // Shields
    public static final Enchantment SHIELDING = ModEnchantments.registerEnchantment("shielding", 
        new ShieldingEnchantment(Enchantment.Rarity.COMMON, equipmentSlots));
    
    public static final Enchantment UNYIELDING = ModEnchantments.registerEnchantment("unyielding", 
        new UnyieldingEnchantment(Enchantment.Rarity.UNCOMMON, equipmentSlots));
    
    public static final Enchantment PROJECTILE_DEFLECTION = ModEnchantments.registerEnchantment("projectile_deflection", 
        new ProjectileDeflectionEnchantment(Enchantment.Rarity.RARE, equipmentSlots));

    public static final Enchantment SHOCK_REBOUND = ModEnchantments.registerEnchantment("shock_rebound", 
        new ShockReboundEnchantment(Enchantment.Rarity.RARE, equipmentSlots));

    public static final Enchantment ECHOING = ModEnchantments.registerEnchantment("echoing", 
        new EchoingEnchantment(Enchantment.Rarity.VERY_RARE, equipmentSlots));
    
    public static final Enchantment CLOAKING = ModEnchantments.registerEnchantment("cloaking",
        new CloakingEnchantment(Enchantment.Rarity.VERY_RARE, equipmentSlots));

    
    // Spears
    public static final Enchantment WHIRLWIND = ModEnchantments.registerEnchantment("whirlwind",
        new WhirlwindEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    
    public static final Enchantment SKEWERING = ModEnchantments.registerEnchantment("skewering", 
        new SkeweringEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

    public static final Enchantment THRUSTING = ModEnchantments.registerEnchantment("thrusting", 
        new ThrustingEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));

    public static final Enchantment GROUNDBREAKING = ModEnchantments.registerEnchantment("groundbreaking", 
        new GroundbreakingEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    
    public static final Enchantment LEANING = ModEnchantments.registerEnchantment("leaning", 
        new LeaningEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    public static final Enchantment FLARE = ModEnchantments.registerEnchantment("flare", 
        new FlareEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));


    private static Enchantment registerEnchantment(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name), enchantment);
    } 

    public static void registerModEnchantments() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Enchantments for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }
    
}
