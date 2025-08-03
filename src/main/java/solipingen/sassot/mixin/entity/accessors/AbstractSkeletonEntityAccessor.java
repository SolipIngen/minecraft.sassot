package solipingen.sassot.mixin.entity.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;


@Mixin(AbstractSkeletonEntity.class)
public interface AbstractSkeletonEntityAccessor {
    
    @Accessor("meleeAttackGoal")
    public MeleeAttackGoal getMeleeAttackGoal();

}
