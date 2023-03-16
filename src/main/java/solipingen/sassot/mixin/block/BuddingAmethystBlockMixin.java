package solipingen.sassot.mixin.block;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.AmethystBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.block.SculkCatalystBlock;
import net.minecraft.block.SculkVeinBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import solipingen.sassot.block.EchoCrystalClusterBlock;
import solipingen.sassot.block.ModBlocks;


@Mixin(BuddingAmethystBlock.class)
public abstract class BuddingAmethystBlockMixin extends AmethystBlock {
    @Shadow @Final public static int GROW_CHANCE;
    @Shadow @Final private static Direction[] DIRECTIONS;


    public BuddingAmethystBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "randomTick", at = @At("TAIL"))
    private void injectedRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo cbi) {
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = null;
        BlockState aboveBlockState = world.getBlockState(pos.up());
        if (aboveBlockState.getBlock() instanceof SculkCatalystBlock) {
            if (aboveBlockState.get(SculkCatalystBlock.BLOOM) && (BuddingAmethystBlock.canGrowIn(blockState) || blockState.getBlock() instanceof SculkVeinBlock)) {
                block = ModBlocks.SMALL_ECHO_CRYSTAL_BUD;
            }
        }

        if (blockState.isOf(ModBlocks.SMALL_ECHO_CRYSTAL_BUD) && blockState.get(EchoCrystalClusterBlock.FACING) == direction) {
            block = ModBlocks.MEDIUM_ECHO_CRYSTAL_BUD;
        } 
        else if (blockState.isOf(ModBlocks.MEDIUM_ECHO_CRYSTAL_BUD) && blockState.get(EchoCrystalClusterBlock.FACING) == direction) {
            block = ModBlocks.LARGE_ECHO_CRYSTAL_BUD;
        } 
        else if (blockState.isOf(ModBlocks.LARGE_ECHO_CRYSTAL_BUD) && blockState.get(EchoCrystalClusterBlock.FACING) == direction) {
            block = ModBlocks.ECHO_CRYSTAL_CLUSTER;
        }

        if (block != null) {
            BlockState blockState2 = block.getDefaultState().with(EchoCrystalClusterBlock.FACING, direction).with(EchoCrystalClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
            world.setBlockState(blockPos, blockState2);
        }
    }

}
