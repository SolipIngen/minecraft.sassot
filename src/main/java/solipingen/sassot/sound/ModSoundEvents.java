package solipingen.sassot.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModSoundEvents {
    
    public static final RegistryEntry<SoundEvent> SPEAR_THROW = ModSoundEvents.registerReference("spear_throw");
    public static final SoundEvent SPEAR_HIT_BLOCK = ModSoundEvents.registerSoundEvent("spear_hit_block");
    public static final SoundEvent SPEAR_HIT_ENTITY = ModSoundEvents.registerSoundEvent("spear_hit_entity");
    public static final SoundEvent SPEAR_RETURN = ModSoundEvents.registerSoundEvent("spear_return");
    public static final RegistryEntry<SoundEvent> SPEAR_WHIRLWIND_1 = ModSoundEvents.registerReference("spear_whirlwind_1");
    public static final RegistryEntry<SoundEvent> SPEAR_WHIRLWIND_2 = ModSoundEvents.registerReference("spear_whirlwind_2");
    public static final RegistryEntry<SoundEvent> SPEAR_WHIRLWIND_3 = ModSoundEvents.registerReference("spear_whirlwind_3");
    public static final SoundEvent SPEAR_SKEWERING = ModSoundEvents.registerSoundEvent("spear_skewering");

    public static final RegistryEntry<SoundEvent> BLAZEARM_THROW = ModSoundEvents.registerReference("blazearm_throw");
    public static final SoundEvent BLAZEARM_HIT_BLOCK = ModSoundEvents.registerSoundEvent("blazearm_hit_block");
    public static final SoundEvent BLAZEARM_HIT_ENTITY = ModSoundEvents.registerSoundEvent("blazearm_hit_entity");
    public static final SoundEvent BLAZEARM_RETURN = ModSoundEvents.registerSoundEvent("blazearm_return");
    public static final RegistryEntry<SoundEvent> BLAZEARM_FLARE_1 = ModSoundEvents.registerReference("blazearm_flare_1");
    public static final RegistryEntry<SoundEvent> BLAZEARM_FLARE_2 = ModSoundEvents.registerReference("blazearm_flare_2");
    public static final RegistryEntry<SoundEvent> BLAZEARM_FLARE_3 = ModSoundEvents.registerReference("blazearm_flare_3");

    public static final SoundEvent ECHO_CRYSTAL_BLOCK_BREAK = ModSoundEvents.registerSoundEvent("echo_crystal_block_break");
    public static final SoundEvent ECHO_CRYSTAL_CLUSTER_BREAK = ModSoundEvents.registerSoundEvent("echo_crystal_cluster_break");
    public static final SoundEvent LARGE_ECHO_CRYSTAL_BUD_BREAK = ModSoundEvents.registerSoundEvent("large_echo_crystal_bud_break");
    public static final SoundEvent MEDIUM_ECHO_CRYSTAL_BUD_BREAK = ModSoundEvents.registerSoundEvent("medium_echo_crystal_bud_break");
    public static final SoundEvent SMALL_ECHO_CRYSTAL_BUD_BREAK = ModSoundEvents.registerSoundEvent("small_echo_crystal_bud_break");

    public static final SoundEvent SHIELD_ECHO = ModSoundEvents.registerSoundEvent("shield_echo");

    public static final SoundEvent COPPER_SHIELD_BLOCK = ModSoundEvents.registerSoundEvent("copper_shield_block");
    public static final SoundEvent GOLDEN_SHIELD_BLOCK = ModSoundEvents.registerSoundEvent("golden_shield_block");
    public static final SoundEvent IRON_SHIELD_BLOCK = ModSoundEvents.registerSoundEvent("iron_shield_block");
    public static final SoundEvent DIAMOND_SHIELD_BLOCK = ModSoundEvents.registerSoundEvent("diamond_shield_block");
    public static final SoundEvent NETHERITE_SHIELD_BLOCK = ModSoundEvents.registerSoundEvent("netherite_shield_block");

    public static final SoundEvent GROUNDSHAKING = ModSoundEvents.registerSoundEvent("groundshaking");

    public static final SoundEvent PILLAGER_SPEAR_THROW = ModSoundEvents.registerSoundEvent("pillager_spear_throw");
    public static final SoundEvent WITHER_SKELETON_SPEAR_THROW = ModSoundEvents.registerSoundEvent("wither_skeleton_spear_throw");
    public static final SoundEvent WITHER_SKELETON_BLAZEARM_THROW = ModSoundEvents.registerSoundEvent("wither_skeleton_blazearm_throw");
    public static final SoundEvent PIGLIN_SPEAR_THROW = ModSoundEvents.registerSoundEvent("piglin_spear_throw");
    public static final SoundEvent VILLAGER_SPEAR_THROW = ModSoundEvents.registerSoundEvent("villager_spear_throw");

    public static final SoundEvent VILLAGER_ATTACK = ModSoundEvents.registerSoundEvent("villager_attack");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    private static RegistryEntry<SoundEvent> registerReference(String name) {
        Identifier id = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name);
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerModSoundEvents() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Sounds for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

}
