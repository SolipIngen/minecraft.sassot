package solipingen.sassot.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ModSoundEvents {
    
    public static final SoundEvent SPEAR_THROW = ModSoundEvents.registerSoundEvent("spear_throw");
    public static final SoundEvent SPEAR_HIT_BLOCK = ModSoundEvents.registerSoundEvent("spear_hit_block");
    public static final SoundEvent SPEAR_HIT_ENTITY = ModSoundEvents.registerSoundEvent("spear_hit_entity");
    public static final SoundEvent SPEAR_RETURN = ModSoundEvents.registerSoundEvent("spear_return");
    public static final SoundEvent SPEAR_WHIRLWIND_1 = ModSoundEvents.registerSoundEvent("spear_whirlwind_1");
    public static final SoundEvent SPEAR_WHIRLWIND_2 = ModSoundEvents.registerSoundEvent("spear_whirlwind_2");
    public static final SoundEvent SPEAR_WHIRLWIND_3 = ModSoundEvents.registerSoundEvent("spear_whirlwind_3");
    public static final SoundEvent SPEAR_SKEWERING = ModSoundEvents.registerSoundEvent("spear_skewering");

    public static final SoundEvent BLAZEARM_THROW = ModSoundEvents.registerSoundEvent("blazearm_throw");
    public static final SoundEvent BLAZEARM_HIT_BLOCK = ModSoundEvents.registerSoundEvent("blazearm_hit_block");
    public static final SoundEvent BLAZEARM_HIT_ENTITY = ModSoundEvents.registerSoundEvent("blazearm_hit_entity");
    public static final SoundEvent BLAZEARM_RETURN = ModSoundEvents.registerSoundEvent("blazearm_return");
    public static final SoundEvent BLAZEARM_FLARE_1 = ModSoundEvents.registerSoundEvent("blazearm_flare_1");
    public static final SoundEvent BLAZEARM_FLARE_2 = ModSoundEvents.registerSoundEvent("blazearm_flare_2");
    public static final SoundEvent BLAZEARM_FLARE_3 = ModSoundEvents.registerSoundEvent("blazearm_flare_3");

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

    public static final SoundEvent GROUNDBREAKING = ModSoundEvents.registerSoundEvent("groundbreaking");

    public static final SoundEvent PILLAGER_SPEAR_THROW = ModSoundEvents.registerSoundEvent("pillager_spear_throw");
    public static final SoundEvent WITHER_SKELETON_SPEAR_THROW = ModSoundEvents.registerSoundEvent("wither_skeleton_spear_throw");
    public static final SoundEvent WITHER_SKELETON_BLAZEARM_THROW = ModSoundEvents.registerSoundEvent("wither_skeleton_blazearm_throw");
    public static final SoundEvent PIGLIN_SPEAR_THROW = ModSoundEvents.registerSoundEvent("piglin_spear_throw");
    public static final SoundEvent VILLAGER_SPEAR_THROW = ModSoundEvents.registerSoundEvent("villager_spear_throw");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerModSoundEvents() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Sounds for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

}
