package solipingen.sassot.mixin.entity.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.UUID;


@Mixin(StatusEffect.class)
public abstract class StatusEffectMixin {


    @ModifyArgs(method = "addAttributeModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect$EffectAttributeModifierCreator;<init>(Ljava/util/UUID;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)V"))
    private void modifiedAddAttributeModifier(Args args) {
        if (args.get(0) == UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9")) {
            args.set(1, 0.15);
            args.set(2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }
        else if (args.get(0) == UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5")) {
            args.set(1, -0.15);
            args.set(2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }
    }

//    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
//    private static void injectedRegister(String id, StatusEffect entry, CallbackInfoReturnable<StatusEffect> cbireturn) {
//        Map<EntityAttribute, AttributeModifierCreator> attributeModifierMap = entry.getAttributeModifiers();
//        attributeModifierMap.forEach((attribute, modifier) -> {
//            if (attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
//                Registry.register(Registries.STATUS_EFFECT, id, entry);
//                int rawId = Registries.STATUS_EFFECT.getRawId(entry);
//                boolean weaknessBl = entry.getCategory() == StatusEffectCategory.HARMFUL;
//                StatusEffect statusEffect = StatusEffectInvoker.invokeStatusEffect(entry.getCategory(), entry.getColor());
//                String uuid = weaknessBl ? "22653B89-116E-49DC-9B6B-9971489B5BE5" : "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9";
//                statusEffect.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, uuid, weaknessBl ? -0.15 : 0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
//                cbireturn.setReturnValue(((SimpleRegistry<StatusEffect>)Registries.STATUS_EFFECT).set(rawId, RegistryKey.of(Registries.STATUS_EFFECT.getKey(), new Identifier(id)), statusEffect, Lifecycle.stable()).value());
//            }
//        });
//    }


}
