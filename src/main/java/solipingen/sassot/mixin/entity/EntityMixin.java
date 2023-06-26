package solipingen.sassot.mixin.entity;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
    

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;noneMatch(Ljava/util/function/Predicate;)Z"))
    private boolean redirectedNoneMatch(Stream<BlockState> blockStream, Predicate<? super BlockState> blockStatePredicate, MovementType movementType, Vec3d movement) {
        if (((Entity)(Object)this) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)((Entity)(Object)this);
            if (player.isUsingRiptide()) {
                return false;
            }
        }
        return blockStream.noneMatch(blockStatePredicate);
    }

    @Override
    public boolean isBeingSnowedOn() {
        World world = ((Entity)(Object)this).getWorld();
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
        Biome biome = world.getBiome(blockPos).value();
        return biome.getPrecipitation(blockPos) == Biome.Precipitation.SNOW;
    }


}
