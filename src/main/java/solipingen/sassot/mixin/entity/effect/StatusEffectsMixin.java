package solipingen.sassot.mixin.entity.effect;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Lifecycle;

import net.minecraft.entity.attribute.AttributeModifierCreator;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import solipingen.sassot.mixin.entity.accessors.StatusEffectInvoker;


@Mixin(StatusEffects.class)
public abstract class StatusEffectsMixin {
    

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void injectedRegister(String id, StatusEffect entry, CallbackInfoReturnable<StatusEffect> cbireturn) {
        Map<EntityAttribute, AttributeModifierCreator> attributeModifierMap = entry.getAttributeModifiers();
        attributeModifierMap.forEach((attribute, modifier) -> {
            if (attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
                Registry.register(Registries.STATUS_EFFECT, id, entry);
                int rawId = Registries.STATUS_EFFECT.getRawId(entry);
                boolean weaknessBl = entry.getCategory() == StatusEffectCategory.HARMFUL;
                StatusEffect statusEffect = StatusEffectInvoker.invokeStatusEffect(entry.getCategory(), entry.getColor());
                String uuid = weaknessBl ? "22653B89-116E-49DC-9B6B-9971489B5BE5" : "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9";
                statusEffect.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, uuid, weaknessBl ? -0.15 : 0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
                cbireturn.setReturnValue(((SimpleRegistry<StatusEffect>)Registries.STATUS_EFFECT).set(rawId, RegistryKey.of(Registries.STATUS_EFFECT.getKey(), new Identifier(id)), statusEffect, Lifecycle.stable()).value());
            }
        });
    }


}
