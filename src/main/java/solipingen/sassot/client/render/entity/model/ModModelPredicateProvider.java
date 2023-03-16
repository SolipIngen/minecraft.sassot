package solipingen.sassot.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.ModItems;


@Environment(value=EnvType.CLIENT)
public class ModModelPredicateProvider {
    
    
    public static void registerModModelPredicates() {

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

        // Shields
        ModModelPredicateProvider.registerShield(ModItems.COPPER_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.GOLDEN_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.IRON_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.DIAMOND_SHIELD);
        ModModelPredicateProvider.registerShield(ModItems.NETHERITE_SHIELD);
        
    }

    private static void registerSpear(Item spear) {
        ModelPredicateProviderRegistry.register(spear, new Identifier("throwing"),
            (stack, world, entity, seed) -> (entity != null && entity.isUsingItem() && !(entity instanceof MerchantEntity) && entity.getActiveItem() == stack) || (entity instanceof PiglinEntity && ((MobEntity)entity).isAttacking() && entity.getMainHandStack() == stack) ? 1.0f : 0.0f);
    }

    private static void registerShield(Item shield) {
        ModelPredicateProviderRegistry.register(shield, new Identifier("blocking"), 
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(shield, new Identifier("cloaking"),
            (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && EnchantmentHelper.getLevel(ModEnchantments.CLOAKING, stack) > 0 ? 1.0f : 0.0f);
    }

}
