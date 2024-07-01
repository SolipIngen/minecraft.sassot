package solipingen.sassot.entity.ai.goal;

import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.util.Hand;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.item.SpearItem;


public class PillagerSpearThrowAttackGoal extends SpearThrowAttackGoal {
    private final PillagerEntity pillager;

    public PillagerSpearThrowAttackGoal(SpearThrowingMob spearThrowingMob, double d, int i, float f) {
        super(spearThrowingMob, d, i, f);
        this.pillager = (PillagerEntity)spearThrowingMob;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && this.pillager.isHolding((stack) -> stack.getItem() instanceof SpearItem);
    }

    @Override
    public void start() {
        super.start();
        this.pillager.setAttacking(true);
        this.pillager.setCurrentHand(Hand.MAIN_HAND);
    }

    @Override
    public void stop() {
        super.stop();
        this.pillager.clearActiveItem();
        this.pillager.setAttacking(false);
    }
}
