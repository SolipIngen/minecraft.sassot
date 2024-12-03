package solipingen.sassot.mixin.entity.passive;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends AnimalEntity {


    protected PigEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/TemptGoal;<init>(Lnet/minecraft/entity/mob/PathAwareEntity;DLjava/util/function/Predicate;Z)V"), index = 2)
    private Predicate<ItemStack> modifiedTemptGoal(Predicate<ItemStack> foodPredicate) {
        return (stack) -> stack.isIn(ModItemTags.PIG_TEMPT_ITEMS);
    }

    @Redirect(method = "getControllingPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isHolding(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedControllingPassengerHoldingItem(PlayerEntity player, Item originalItem) {
        return player.isHolding(originalItem) || player.isHolding(ModItems.CARROT_ON_A_COPPER_FUSED_STICK) || player.isHolding(ModItems.CARROT_ON_AN_IRON_FUSED_STICK) 
            || player.isHolding(ModItems.CARROT_ON_A_GOLD_FUSED_STICK) || player.isHolding(ModItems.CARROT_ON_A_DIAMOND_FUSED_STICK) || player.isHolding(ModItems.CARROT_ON_A_NETHERITE_FUSED_STICK);
    }

    
}
