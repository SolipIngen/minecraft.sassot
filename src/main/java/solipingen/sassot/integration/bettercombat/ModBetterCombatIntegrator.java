package solipingen.sassot.integration.bettercombat;

import net.bettercombat.api.client.AttackRangeExtensions;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import solipingen.sassot.enchantment.ModEnchantments;


public class ModBetterCombatIntegrator {


    public static void registerAttackRangeModifiers() {
        AttackRangeExtensions.register(context -> {
            PlayerEntity player = context.player();
            RegistryEntryLookup<Enchantment> enchantmentLookup = player.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int thrustingLevel = EnchantmentHelper.getEquipmentLevel(enchantmentLookup.getOrThrow(ModEnchantments.THRUSTING), player);
            return new AttackRangeExtensions.Modifier(0.1*thrustingLevel, AttackRangeExtensions.Operation.ADD);
        });
    }

}
