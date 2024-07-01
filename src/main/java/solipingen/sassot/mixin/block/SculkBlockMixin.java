package solipingen.sassot.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SculkBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(SculkBlock.class)
public abstract class SculkBlockMixin {

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static AbstractBlock.Settings modifiedInit(AbstractBlock.Settings settings) {
        return settings.luminance(state -> 2);
    }

}
