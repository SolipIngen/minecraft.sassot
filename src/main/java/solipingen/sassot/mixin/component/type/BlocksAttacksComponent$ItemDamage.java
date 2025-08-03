package solipingen.sassot.mixin.component.type;

import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BlocksAttacksComponent.ItemDamage.class)
public abstract class BlocksAttacksComponent$ItemDamage {


    @Inject(method = "calculate", at = @At("HEAD"), cancellable = true)
    private void injectedCalculate(float itemDamage, CallbackInfoReturnable<Integer> cbireturn) {
        BlocksAttacksComponent.ItemDamage itemDamageComponent = (BlocksAttacksComponent.ItemDamage)(Object)this;
        cbireturn.setReturnValue(Math.max(MathHelper.floor(itemDamageComponent.base() + itemDamageComponent.factor()*itemDamage - itemDamageComponent.threshold()), 0));
    }

}
