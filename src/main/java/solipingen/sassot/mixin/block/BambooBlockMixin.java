package solipingen.sassot.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BambooBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import solipingen.sassot.item.BlazearmItem;


@Mixin(BambooBlock.class)
public abstract class BambooBlockMixin extends Block {


    public BambooBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "calcBlockBreakingDelta", at = @At("TAIL"), cancellable = true)
    private void injectedCalcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cbireturn) {
        if (player.getMainHandStack().getItem() instanceof BlazearmItem) {
            cbireturn.setReturnValue(1.0f);
        }
    }


    
}
