package solipingen.sassot.mixin.entity.effect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.DamageModifierStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import solipingen.sassot.mixin.entity.accessors.DamageModifierStatusEffectInvoker;


@Mixin(StatusEffects.class)
public abstract class StatusEffectsMixin {
    

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void injectedRegister(int rawId, String id, StatusEffect entry, CallbackInfoReturnable<StatusEffect> cbireturn) {
        if (entry instanceof DamageModifierStatusEffect) {
            boolean weaknessBl = entry.getCategory() == StatusEffectCategory.HARMFUL;
            DamageModifierStatusEffect statusEffect = DamageModifierStatusEffectInvoker.invokeDamageModifierStatusEffect(entry.getCategory(), entry.getColor(), 0.0);
            String uuid = weaknessBl ? "22653B89-116E-49DC-9B6B-9971489B5BE5" : "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9";
            statusEffect.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, uuid, weaknessBl ? -0.15 : 0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
            cbireturn.setReturnValue(Registry.register(Registries.STATUS_EFFECT, rawId, id, statusEffect));
        }
    }


}
