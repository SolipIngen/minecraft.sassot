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
import net.minecraft.block.Blocks;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.block.SculkCatalystBlock;
import net.minecraft.block.SculkShriekerBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import solipingen.sassot.block.EchoCrystalClusterBlock;
import solipingen.sassot.block.ModBlocks;


@Mixin(BuddingAmethystBlock.class)
public abstract class BuddingAmethystBlockMixin extends AmethystBlock {
    @Shadow @Final private static Direction[] DIRECTIONS;


    public BuddingAmethystBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockState aboveBlockState = world.getBlockState(pos.up());
        BlockState aboveAboveBlockState = world.getBlockState(pos.up(2));
        if (aboveBlockState.isOf(Blocks.SCULK_CATALYST) && aboveAboveBlockState.isOf(Blocks.SCULK_SHRIEKER)) {
            Direction budDirection = Direction.random(world.getRandom());
            budDirection = budDirection == direction ? budDirection.getOpposite() : budDirection;
            BlockPos blockPos = pos.offset(budDirection);
            BlockState blockState = world.getBlockState(blockPos);
            if (aboveAboveBlockState.get(SculkShriekerBlock.SHRIEKING) && aboveBlockState.get(SculkCatalystBlock.BLOOM) && (BuddingAmethystBlock.canGrowIn(blockState) || blockState.isOf(Blocks.SCULK_VEIN)) && world.getRandom().nextInt(4) == 0) {
                Block block = ModBlocks.SMALL_ECHO_CRYSTAL_BUD;
                BlockState blockState2 = block.getDefaultState().with(EchoCrystalClusterBlock.FACING, budDirection).with(EchoCrystalClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
                world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL);
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Inject(method = "randomTick", at = @At("TAIL"), cancellable = true)
    private void injectedRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo cbi) {
        if (world.random.nextInt(4) != 0) {
            cbi.cancel();
        }
        for (Direction direction : DIRECTIONS) {
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            Block block = Blocks.AIR;
            if (blockState.isOf(ModBlocks.SMALL_ECHO_CRYSTAL_BUD) && blockState.get(EchoCrystalClusterBlock.FACING) == direction) {
                block = ModBlocks.MEDIUM_ECHO_CRYSTAL_BUD;
            } 
            else if (blockState.isOf(ModBlocks.MEDIUM_ECHO_CRYSTAL_BUD) && blockState.get(EchoCrystalClusterBlock.FACING) == direction) {
                block = ModBlocks.LARGE_ECHO_CRYSTAL_BUD;
            } 
            else if (blockState.isOf(ModBlocks.LARGE_ECHO_CRYSTAL_BUD) && blockState.get(EchoCrystalClusterBlock.FACING) == direction) {
                block = ModBlocks.ECHO_CRYSTAL_CLUSTER;
            }
            if (block instanceof EchoCrystalClusterBlock) {
                BlockState blockState2 = block.getDefaultState().with(EchoCrystalClusterBlock.FACING, direction).with(EchoCrystalClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
                world.setBlockState(blockPos, blockState2);
            }
        }
    }

}
