package solipingen.sassot.client.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.ModItems;


@Environment(value = EnvType.CLIENT)
public class ModModelPredicateProvider {
    
    
    public static void registerModItemModelPredicates() {

        // Spears
        ModModelPredicateProvider.registerSpear(ModItems.WOODEN_SPEAR);
        ModModelPredicateProvider.registerSpear(ModItems.BAMBOO_SPEAR);
        ModModelPredicateProvider.registerSpear(ModItems.STONE_SPEAR);
        ModModelPredicateProvider.registerSpear(ModItems.FLINT_SPEAR);
        ModModelPredicateProvider.registerSpear(ModItems.COPPER_SPEAR);
        ModModelPredicateProvider.registerSpear(ModItems.GOLDEN_SPEAR);
        ModModelPredicateProvider.registerSpear(ModItems.IRON_SPEAR);
        ModModelPredicateProvider.registerSpear(ModItems.DIAMOND_SPEAR);
        ModModelPredicateProvider.registerSpear(ModItems.NETHERITE_SPEAR);
        ModModelPredicateProvider.registerSpear(ModItems.BLAZEARM);

        // Wooden & Framed Shields
        ModModelPredicateProvider.registerShield(ModItems.WOODEN_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.COPPER_FRAMED_WOODEN_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.GOLD_FRAMED_WOODEN_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.IRON_FRAMED_WOODEN_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.DIAMOND_FRAMED_WOODEN_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.EMERALD_FRAMED_WOODEN_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.NETHERITE_FRAMED_WOODEN_SHIELD);

        // Shields
        ModModelPredicateProvider.registerShield(ModItems.COPPER_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.GOLDEN_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.IRON_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.DIAMOND_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.NETHERITE_SHIELD);

        // Fishing Rods
        ModModelPredicateProvider.registerFishingRod(ModItems.COPPER_FUSED_FISHING_ROD);
        ModModelPredicateProvider.registerFishingRod(ModItems.IRON_FUSED_FISHING_ROD);
        ModModelPredicateProvider.registerFishingRod(ModItems.GOLD_FUSED_FISHING_ROD);
        ModModelPredicateProvider.registerFishingRod(ModItems.DIAMOND_FUSED_FISHING_ROD);
        ModModelPredicateProvider.registerFishingRod(ModItems.NETHERITE_FUSED_FISHING_ROD);
        
    }

    private static void registerSpear(Item spear) {
        ModelPredicateProviderRegistry.register(spear, Identifier.of("throwing"),
            (stack, world, entity, seed) -> (entity != null && entity.isUsingItem() && !(entity instanceof MerchantEntity) && entity.getActiveItem() == stack) || (entity instanceof PiglinEntity && ((MobEntity)entity).isAttacking()) ? 1.0f : 0.0f);
    }

    private static void registerShield(Item shield) {
        ModelPredicateProviderRegistry.register(shield, Identifier.of("blocking"), 
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(shield, Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "cloaking"),
            (stack, world, entity, seed) -> {
                if (entity != null) {
                    RegistryEntryLookup<Enchantment> enchantmentLookup = entity.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
                    return entity.isUsingItem() && entity.getActiveItem() == stack && EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.CLOAKING), stack) > 0 ? 1.0f : 0.0f;
                }
                return 0.0f;
            });
    }

    private static void registerFishingRod(Item fishingRod) {
        ModelPredicateProviderRegistry.register(fishingRod, Identifier.of("cast"), 
            (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            boolean bl = entity.getMainHandStack() == stack;
            boolean bl2 = entity.getOffHandStack() == stack;
            if (entity.getMainHandStack().getItem() instanceof FishingRodItem) {
                bl2 = false;
            }
            return (bl || bl2) && entity instanceof PlayerEntity && ((PlayerEntity)entity).fishHook != null ? 1.0f : 0.0f;
        });
    }

}
