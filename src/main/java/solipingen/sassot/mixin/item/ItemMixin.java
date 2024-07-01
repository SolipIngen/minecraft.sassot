package solipingen.sassot.mixin.item;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow
    public abstract float getMiningSpeed(ItemStack stack, BlockState state);


    @Inject(method = "postMine", at = @At("HEAD"), cancellable = true)
    private void injectedPostMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cbireturn) {
        ToolComponent toolComponent = stack.get(DataComponentTypes.TOOL);
        if (toolComponent == null) {
            cbireturn.setReturnValue(false);
        }
        else {
            if (!world.isClient() && state.getHardness(world, pos) != 0.0f && toolComponent.damagePerBlock() > 0 && this.getMiningSpeed(stack, state) > 1.0f) {
                stack.damage(toolComponent.damagePerBlock(), miner, EquipmentSlot.MAINHAND);
            }
            cbireturn.setReturnValue(true);
        }
    }


}
