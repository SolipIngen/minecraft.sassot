package solipingen.sassot.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EchoCrystalBlock extends Block {
    
    
    public EchoCrystalBlock(AbstractBlock.Settings settings) {
        super(settings);
    }
    
    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            BlockPos blockPos = hit.getBlockPos();
            world.playSound(null, blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.BLOCKS, 1.0f, 0.2f + 0.5f*world.random.nextFloat());
            world.playSound(null, blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0f, 0.2f + 0.5f*world.random.nextFloat());
        }
    }
}
