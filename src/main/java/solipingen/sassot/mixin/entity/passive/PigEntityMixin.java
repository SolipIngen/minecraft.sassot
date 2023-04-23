package solipingen.sassot.mixin.entity.passive;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;


@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends AnimalEntity {


    protected PigEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Ingredient;ofItems([Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/recipe/Ingredient;"))
    private Ingredient redirectedAttractingIngredient(ItemConvertible... itemConvertibles) {
        ArrayList<ItemConvertible> itemList = new ArrayList<ItemConvertible>();
        for (ItemConvertible itemConvertible : itemConvertibles) {
            itemList.add(itemConvertible);
        }
        itemList.add(ModItems.CARROT_ON_A_COPPER_FUSED_STICK);
        itemList.add(ModItems.CARROT_ON_AN_IRON_FUSED_STICK);
        itemList.add(ModItems.CARROT_ON_A_GOLD_FUSED_STICK);
        itemList.add(ModItems.CARROT_ON_A_DIAMOND_FUSED_STICK);
        itemList.add(ModItems.CARROT_ON_A_NETHERITE_FUSED_STICK);
        ItemConvertible[] newItemConvertibles = itemList.toArray(new ItemConvertible[itemList.size()]);
        return Ingredient.ofItems(newItemConvertibles);
    }

    @Redirect(method = "getControllingPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedControllingPassengerHoldingItem(ItemStack itemStack, Item originalItem) {
        return itemStack.isOf(originalItem) || itemStack.isOf(ModItems.CARROT_ON_A_COPPER_FUSED_STICK) || itemStack.isOf(ModItems.CARROT_ON_AN_IRON_FUSED_STICK) 
            || itemStack.isOf(ModItems.CARROT_ON_A_GOLD_FUSED_STICK) || itemStack.isOf(ModItems.CARROT_ON_A_DIAMOND_FUSED_STICK) || itemStack.isOf(ModItems.CARROT_ON_A_NETHERITE_FUSED_STICK);
    }

    
}
