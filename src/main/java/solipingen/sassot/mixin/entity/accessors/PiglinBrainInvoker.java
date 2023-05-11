package solipingen.sassot.mixin.entity.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;


@Mixin(PiglinBrain.class)
public interface PiglinBrainInvoker {

    @Invoker("isPreferredAttackTarget")
    public static boolean invokeIsPreferredAttackTarget(PiglinEntity piglin, LivingEntity target) {
        throw new AssertionError();
    }
    
    @Invoker("isHoldingCrossbow")
    public static boolean invokeIsHoldingCrossbow(LivingEntity piglin) {
        throw new AssertionError();
    }

    @Invoker("getNearestZombifiedPiglin")
    public static boolean invokeGetNearestZombifiedPiglin(PiglinEntity piglin) {
        throw new AssertionError();
    }

}
