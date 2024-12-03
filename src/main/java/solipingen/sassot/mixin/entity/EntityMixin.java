package solipingen.sassot.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;


@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInterface {


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
