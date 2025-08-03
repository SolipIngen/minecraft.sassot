package solipingen.sassot.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModEnchantments {

    // Axes
    public static final RegistryKey<Enchantment> HACKING = ModEnchantments.enchantmentKeyOf("hacking");

    // Shields
    public static final RegistryKey<Enchantment> SHIELDING = ModEnchantments.enchantmentKeyOf("shielding");
    public static final RegistryKey<Enchantment> UNYIELDING = ModEnchantments.enchantmentKeyOf("unyielding");
    public static final RegistryKey<Enchantment> PROJECTILE_DEFLECTION = ModEnchantments.enchantmentKeyOf("projectile_deflection");
    public static final RegistryKey<Enchantment> SHOCK_REBOUND = ModEnchantments.enchantmentKeyOf("shock_rebound");
    public static final RegistryKey<Enchantment> ECHOING = ModEnchantments.enchantmentKeyOf("echoing");
    public static final RegistryKey<Enchantment> CLOAKING = ModEnchantments.enchantmentKeyOf("cloaking");
    
    // Spears
    public static final RegistryKey<Enchantment> WHIRLWIND = ModEnchantments.enchantmentKeyOf("whirlwind");
    public static final RegistryKey<Enchantment> SKEWERING = ModEnchantments.enchantmentKeyOf("skewering");
    public static final RegistryKey<Enchantment> THRUSTING = ModEnchantments.enchantmentKeyOf("thrusting");
    public static final RegistryKey<Enchantment> GROUNDSHAKING = ModEnchantments.enchantmentKeyOf("groundshaking");
    public static final RegistryKey<Enchantment> LEANING = ModEnchantments.enchantmentKeyOf("leaning");
    public static final RegistryKey<Enchantment> FLARE = ModEnchantments.enchantmentKeyOf("flare");


    private static RegistryKey<Enchantment> enchantmentKeyOf(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name));
    }

    public static void registerModEnchantments() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Enchantments for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }
    
}
