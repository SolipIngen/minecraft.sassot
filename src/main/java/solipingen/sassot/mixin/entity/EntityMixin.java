package solipingen.sassot.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;


@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInterface {
    

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private boolean redirectedBlockStateIsOf(BlockState blockState, Block originalBlock, MovementType movementType, Vec3d movement) {
        if (((Entity)(Object)this) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)((Entity)(Object)this);
            if (player.isUsingRiptide()) {
                return false;
            }
        }
        return blockState.isOf(originalBlock);
    }


    @Override
    public boolean isBeingSnowedOn() {
        World world = ((Entity)(Object)this).world;
        BlockPos blockPos = ((Entity)(Object)this).getBlockPos();
        if (!world.isRaining()) {
            return false;
        }
        if (!world.isSkyVisible(blockPos)) {
            return false;
        }
        if (world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos).getY() > blockPos.getY()) {
            return false;
        }
        Biome biome = ((Entity)(Object)this).world.getBiome(blockPos).value();
        return biome.getPrecipitation() == Biome.Precipitation.SNOW;
    }


}
