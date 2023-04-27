package solipingen.sassot.mixin.entity.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.effect.DamageModifierStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;


@Mixin(DamageModifierStatusEffect.class)
public interface DamageModifierStatusEffectInvoker {
    
    
    @Invoker("<init>")
    public static DamageModifierStatusEffect invokeDamageModifierStatusEffect(StatusEffectCategory category, int color, double modifier) {
        throw new AssertionError();
    }


}
