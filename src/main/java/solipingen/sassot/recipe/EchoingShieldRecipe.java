package solipingen.sassot.recipe;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.registry.tag.ModItemTags;


public class EchoingShieldRecipe extends SpecialCraftingRecipe {
    public static final RecipeSerializer<EchoingShieldRecipe> ECHOING_SHIELD_RECIPE_SERIALIZER = new SpecialRecipeSerializer<EchoingShieldRecipe>(EchoingShieldRecipe::new);

    
    public EchoingShieldRecipe(CraftingRecipeCategory category) {
        super(category);
    }
    
    @Override
    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        if (craftingRecipeInput.getWidth() != 3 || craftingRecipeInput.getHeight() != 3) {
            return false;
        }
        for (int i = 0; i < craftingRecipeInput.getWidth(); ++i) {
            for (int j = 0; j < craftingRecipeInput.getHeight(); ++j) {
                ItemStack itemStack = craftingRecipeInput.getStackInSlot(i + j * craftingRecipeInput.getWidth());
                if (itemStack.isEmpty()) {
                    return false;
                }
                if (!(i == 1 && j == 1 ? !(itemStack.isIn(ModItemTags.SHIELDS)) : !itemStack.isOf(Items.ECHO_SHARD))) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = craftingRecipeInput.getStackInSlot(1 + craftingRecipeInput.getWidth());
        ItemStack resultItemStack = itemStack.copy();
        if (!(itemStack.isIn(ModItemTags.SHIELDS))) {
            return ItemStack.EMPTY;
        }
        RegistryEntryLookup<Enchantment> enchantmentLookup = lookup.createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        int echoingLevel = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.ECHOING), itemStack);
        if (echoingLevel >= 3) {
            return ItemStack.EMPTY;
        }
        resultItemStack.addEnchantment(enchantmentLookup.getOrThrow(ModEnchantments.ECHOING), echoingLevel + 1);
        return resultItemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ECHOING_SHIELD_RECIPE_SERIALIZER;
    }
    

}
