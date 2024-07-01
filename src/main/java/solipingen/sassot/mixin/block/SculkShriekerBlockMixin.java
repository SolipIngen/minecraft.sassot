package solipingen.sassot.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SculkShriekerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(SculkShriekerBlock.class)
public abstract class SculkShriekerBlockMixin {


    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static AbstractBlock.Settings modifiedInit(AbstractBlock.Settings settings) {
        return settings.luminance(state -> state.get(SculkShriekerBlock.SHRIEKING) ? 4 : 2);
    }

}
