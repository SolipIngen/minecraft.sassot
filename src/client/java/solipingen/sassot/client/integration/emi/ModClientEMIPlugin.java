package solipingen.sassot.client.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.registry.tag.ModItemTags;

import java.util.List;
import java.util.function.Supplier;


@Environment(EnvType.CLIENT)
public class ModClientEMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        Registries.ITEM.streamEntries().forEach(itemEntry -> {
            if (itemEntry.value().getDefaultStack().isIn(ModItemTags.SHIELD_ENCHANTABLE)) {
                if (MinecraftClient.getInstance().world != null) {
                    RegistryEntryLookup<Enchantment> enchantmentLookup = MinecraftClient.getInstance().world.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
                    RegistryEntry<Enchantment> echoingEnchantment = enchantmentLookup.getOrThrow(ModEnchantments.ECHOING);
                    for (int j = 0; j < echoingEnchantment.value().getMaxLevel(); j++) {
                        ItemStack shieldStack = new ItemStack(itemEntry.value());
                        if (j > 0) {
                            shieldStack.addEnchantment(echoingEnchantment, j);
                        }
                        EmiStack shield = EmiStack.of(shieldStack);
                        EmiStack echoShard = EmiStack.of(Items.ECHO_SHARD);
                        List<EmiIngredient> input = List.of(
                                echoShard, echoShard, echoShard,
                                echoShard, shield, echoShard,
                                echoShard, echoShard, echoShard
                        );
                        shieldStack.addEnchantment(echoingEnchantment, j + 1);
                        EmiStack output = EmiStack.of(shieldStack);
                        int level = j;
                        ModClientEMIPlugin.addRecipeSafe(registry, () ->
                                new EmiCraftingRecipe(input, output,
                                        Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,
                                                Registries.ITEM.getId(itemEntry.value()).getPath() + "_" + "crafting/echoing_shield_" + level),
                                        false));

                    }
                }
            }
        });
    }

    private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier) {
        try {
            registry.addRecipe(supplier.get());
        }
        catch (Throwable e) {
            SpearsAxesSwordsShieldsAndOtherTools.LOGGER.warn("Exception thrown when parsing EMI recipe.");
            SpearsAxesSwordsShieldsAndOtherTools.LOGGER.error(String.valueOf(e));
        }
    }



}
