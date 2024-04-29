package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import solipingen.sassot.util.interfaces.mixin.entity.player.PlayerEntityInterface;


@Mixin(FishingRodItem.class)
public abstract class FishingRodItemMixin extends Item {


    public FishingRodItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void injectedUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cbireturn) {
        ((PlayerEntityInterface)user).setFishingRodStack(user.getStackInHand(hand));
    }

    @ModifyConstant(method = "getEnchantability", constant = @Constant(intValue = 1))
    private int modifiedEnchantability(int originalInt) {
        return 15;
    }

    
}
