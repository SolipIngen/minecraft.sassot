package solipingen.sassot.recipe;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
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
    public boolean matches(RecipeInputInventory craftingInventory, World world) {
        if (craftingInventory.getWidth() != 3 || craftingInventory.getHeight() != 3) {
            return false;
        }
        for (int i = 0; i < craftingInventory.getWidth(); ++i) {
            for (int j = 0; j < craftingInventory.getHeight(); ++j) {
                ItemStack itemStack = craftingInventory.getStack(i + j * craftingInventory.getWidth());
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
    public ItemStack craft(RecipeInputInventory craftingInventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = craftingInventory.getStack(1 + craftingInventory.getWidth());
        ItemStack resultItemStack = itemStack.copy();
        if (!(itemStack.isIn(ModItemTags.SHIELDS))) {
            return ItemStack.EMPTY;
        }
        int echoingLevel = EnchantmentHelper.getLevel(ModEnchantments.ECHOING, itemStack);
        if (echoingLevel >= 3) {
            return ItemStack.EMPTY;
        }
        resultItemStack.addEnchantment(ModEnchantments.ECHOING, echoingLevel + 1);
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
