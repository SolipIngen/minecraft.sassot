package solipingen.sassot.entity.ai.goal;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PillagerEntity;


public class PillagerMeleeAttackGoal extends MeleeAttackGoal {
    public PillagerMeleeAttackGoal(PillagerEntity pillager) {
        super(pillager, 1.0, false);
    }
}
