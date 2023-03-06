package solipingen.sassot.mixin.recipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.ShieldDecorationRecipe;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;


@Mixin(ShieldDecorationRecipe.class)
public abstract class ShieldDecorationRecipeMixin extends SpecialCraftingRecipe {

    public ShieldDecorationRecipeMixin(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private void injectedMatches(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> cbireturn) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;
        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStack(i);
            if (itemStack3.isEmpty()) continue;
            if (itemStack3.getItem() instanceof BannerItem) {
                if (!itemStack2.isEmpty()) {
                    cbireturn.setReturnValue(false);
                }
                itemStack2 = itemStack3;
                continue;
            }
            if (itemStack3.getItem() instanceof ShieldItem) {
                if (!itemStack.isEmpty()) {
                    cbireturn.setReturnValue(false);
                }
                if (BlockItem.getBlockEntityNbt(itemStack3) != null) {
                    cbireturn.setReturnValue(false);
                }
                itemStack = itemStack3;
                continue;
            }
            cbireturn.setReturnValue(false);
        }
        cbireturn.setReturnValue(!itemStack.isEmpty() && !itemStack2.isEmpty());
    }

    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private void injectedCraft(CraftingInventory craftingInventory, CallbackInfoReturnable<ItemStack> cbireturn) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;
        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStack(i);
            if (itemStack3.isEmpty()) continue;
            if (itemStack3.getItem() instanceof BannerItem) {
                itemStack = itemStack3;
                continue;
            }
            if (!(itemStack3.getItem() instanceof ShieldItem)) continue;
            itemStack2 = itemStack3.copy();
        }
        if (itemStack2.isEmpty()) {
            cbireturn.setReturnValue(itemStack2);
        }
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack);
        NbtCompound nbtCompound2 = nbtCompound == null ? new NbtCompound() : nbtCompound.copy();
        nbtCompound2.putInt("Base", ((BannerItem)itemStack.getItem()).getColor().getId());
        BlockItem.setBlockEntityNbt(itemStack2, BlockEntityType.BANNER, nbtCompound2);
        cbireturn.setReturnValue(itemStack2);
    }
    
}
