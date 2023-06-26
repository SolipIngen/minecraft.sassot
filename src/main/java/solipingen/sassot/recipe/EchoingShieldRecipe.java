package solipingen.sassot.recipe;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import solipingen.sassot.enchantment.ModEnchantments;


public class EchoingShieldRecipe extends SpecialCraftingRecipe {
    public static final RecipeSerializer<EchoingShieldRecipe> ECHOING_SHIELD_RECIPE_SERIALIZER = new SpecialRecipeSerializer<EchoingShieldRecipe>(EchoingShieldRecipe::new);

    
    public EchoingShieldRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
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
                if (!(i == 1 && j == 1 ? !(itemStack.getItem() instanceof ShieldItem) : !itemStack.isOf(Items.ECHO_SHARD))) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(RecipeInputInventory craftingInventory, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack itemStack = craftingInventory.getStack(1 + craftingInventory.getWidth());
        if (!(itemStack.getItem() instanceof ShieldItem)) {
            return ItemStack.EMPTY;
        }
        int echoingLevel = EnchantmentHelper.getLevel(ModEnchantments.ECHOING, itemStack);
        if (echoingLevel >= 3) {
            return ItemStack.EMPTY;
        }
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack);
        if (echoingLevel <= 0) {
            enchantments.put(ModEnchantments.ECHOING, echoingLevel + 1);
        }
        else if (echoingLevel > 0) {
            enchantments.replace(ModEnchantments.ECHOING, echoingLevel + 1);
        }
        ItemStack resultItemStack = itemStack.copy();
        EnchantmentHelper.set(enchantments, resultItemStack);
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
