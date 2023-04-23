package solipingen.sassot.mixin.entity.ai.goal;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


@Mixin(targets = "net.minecraft.entity.mob.DrownedEntity$TridentAttackGoal")
public abstract class TridentAttackGoalMixin extends ProjectileAttackGoal {
    @Shadow @Final private DrownedEntity drowned;


    public TridentAttackGoalMixin(RangedAttackMob mob, double mobSpeed, int intervalTicks, float maxShootRange) {
        super(mob, mobSpeed, intervalTicks, maxShootRange);
    }

    @Redirect(method = "canStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedTridentHoldingBl(ItemStack itemStack, Item item) {
        LivingEntity target = this.drowned.getTarget();
        return itemStack.isOf(item) && this.drowned.squaredDistanceTo(target) > this.drowned.squaredAttackRange(target);
    }


    
}
