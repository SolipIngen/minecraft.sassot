package solipingen.sassot.client.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
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
                for (int j = 0; j < ModEnchantments.ECHOING.getMaxLevel(); j++) {
                    ItemStack shieldStack = new ItemStack(itemEntry.value());
                    if (j > 0) {
                        shieldStack.addEnchantment(ModEnchantments.ECHOING, j);
                    }
                    EmiStack shield = EmiStack.of(shieldStack);
                    EmiStack echoShard = EmiStack.of(Items.ECHO_SHARD);
                    List<EmiIngredient> input = List.of(
                            echoShard, echoShard, echoShard,
                            echoShard, shield, echoShard,
                            echoShard, echoShard, echoShard
                    );
                    shieldStack.addEnchantment(ModEnchantments.ECHOING, j + 1);
                    EmiStack output = EmiStack.of(shieldStack);
                    int level = j;
                    ModClientEMIPlugin.addRecipeSafe(registry, () ->
                            new EmiCraftingRecipe(input, output,
                                    new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,
                                            Registries.ITEM.getId(itemEntry.value()).getPath() + "_" + "crafting/echoing_shield_" + level),
                                    false));

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
