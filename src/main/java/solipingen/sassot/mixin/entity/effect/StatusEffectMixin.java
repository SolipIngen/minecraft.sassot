package solipingen.sassot.mixin.entity.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


@Mixin(StatusEffect.class)
public abstract class StatusEffectMixin {


    @ModifyArgs(method = "addAttributeModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect$EffectAttributeModifierCreator;<init>(Lnet/minecraft/util/Identifier;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)V"))
    private void modifiedAddAttributeModifier(Args args) {
        if (args.get(0).equals(Identifier.ofVanilla("effect.strength"))) {
            args.set(1, 0.15);
            args.set(2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }
        else if (args.get(0).equals(Identifier.ofVanilla("effect.weakness"))) {
            args.set(1, -0.15);
            args.set(2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }
    }


}
