package solipingen.sassot.mixin.entity.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;


@Mixin(StatusEffect.class)
public interface StatusEffectInvoker {
    
    
    @Invoker("<init>")
    public static StatusEffect invokeStatusEffect(StatusEffectCategory category, int color) {
        throw new AssertionError();
    }


}
