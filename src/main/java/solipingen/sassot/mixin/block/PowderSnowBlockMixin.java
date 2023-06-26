package solipingen.sassot.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import solipingen.sassot.entity.projectile.BlazearmEntity;


@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin extends Block implements FluidDrainable {


    public PowderSnowBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    private void injectedOnEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo cbi) {
        if (!world.isClient) {
            if (entity instanceof BlazearmEntity) {
                world.breakBlock(pos, false);
            }
        }
    }


    
}
