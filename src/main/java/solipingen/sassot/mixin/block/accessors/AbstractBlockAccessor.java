package solipingen.sassot.mixin.block.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.AbstractBlock;


@Mixin(AbstractBlock.class)
public interface AbstractBlockAccessor {
    
    @Accessor("collidable")
    public boolean getCollidable();

}
