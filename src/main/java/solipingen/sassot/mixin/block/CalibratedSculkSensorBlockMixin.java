package solipingen.sassot.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.CalibratedSculkSensorBlock;
import net.minecraft.block.SculkSensorBlock;
import net.minecraft.block.enums.SculkSensorPhase;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(CalibratedSculkSensorBlock.class)
public abstract class CalibratedSculkSensorBlockMixin {


    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static AbstractBlock.Settings modifiedInit(AbstractBlock.Settings settings) {
        return settings.luminance(state -> SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE ? 5 + MathHelper.floor(state.get(SculkSensorBlock.POWER)/2) : 2);
    }

}
