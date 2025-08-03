package solipingen.sassot.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.CobwebBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(CobwebBlock.class)
public abstract class CobwebBlockMixin {


    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static AbstractBlock.Settings modifiedInit(AbstractBlock.Settings settings) {
        return settings.hardness(1.0f).resistance(0.1f);
    }

}
