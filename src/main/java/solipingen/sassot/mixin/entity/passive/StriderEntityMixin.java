package solipingen.sassot.mixin.entity.passive;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;


@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntity {


    protected StriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "initGoals", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/StriderEntity;ATTRACTING_INGREDIENT:Lnet/minecraft/recipe/Ingredient;", opcode = Opcodes.GETSTATIC))
    private Ingredient redirectedAttractingIngredient() {
        return Ingredient.ofItems(Items.WARPED_FUNGUS, Items.WARPED_FUNGUS_ON_A_STICK, ModItems.WARPED_FUNGUS_ON_A_COPPER_FUSED_STICK, ModItems.WARPED_FUNGUS_ON_AN_IRON_FUSED_STICK, ModItems.WARPED_FUNGUS_ON_A_GOLD_FUSED_STICK, ModItems.WARPED_FUNGUS_ON_A_DIAMOND_FUSED_STICK, ModItems.WARPED_FUNGUS_ON_A_NETHERITE_FUSED_STICK);
    }

    @Redirect(method = "initialize", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;WARPED_FUNGUS_ON_A_STICK:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC))
    private Item redirectedZombifiedPiglinStickStack() {
        float randomf = this.random.nextFloat();
        if (randomf < 0.004f) {
            return ModItems.WARPED_FUNGUS_ON_A_NETHERITE_FUSED_STICK;
        }
        else if (randomf >= 0.004f && randomf < 0.4f) {
            return ModItems.WARPED_FUNGUS_ON_A_GOLD_FUSED_STICK;
        }
        return Items.WARPED_FUNGUS_ON_A_STICK;
    }

    @Redirect(method = "getControllingPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedControllingPassengerHoldingItem(ItemStack itemStack, Item originalItem) {
        return itemStack.isOf(originalItem) || itemStack.isOf(ModItems.WARPED_FUNGUS_ON_A_COPPER_FUSED_STICK) || itemStack.isOf(ModItems.WARPED_FUNGUS_ON_AN_IRON_FUSED_STICK) 
            || itemStack.isOf(ModItems.WARPED_FUNGUS_ON_A_GOLD_FUSED_STICK) || itemStack.isOf(ModItems.WARPED_FUNGUS_ON_A_DIAMOND_FUSED_STICK) || itemStack.isOf(ModItems.WARPED_FUNGUS_ON_A_NETHERITE_FUSED_STICK);
    }
    

    



}
