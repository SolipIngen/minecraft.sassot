package solipingen.sassot.entity.ai.goal;

import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.util.Hand;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;


public class WitherSkeletonSpearThrowAttackGoal extends SpearThrowAttackGoal {
    private final WitherSkeletonEntity witherSkeleton;


    public WitherSkeletonSpearThrowAttackGoal(SpearThrowingMob spearThrowingMob, double d, int i, float f) {
        super(spearThrowingMob, d, i, f);
        this.witherSkeleton = (WitherSkeletonEntity)spearThrowingMob;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && (this.witherSkeleton.isHolding((stack) -> stack.getItem() instanceof SpearItem) || this.witherSkeleton.isHolding((stack) -> stack.isOf(ModItems.BLAZEARM)));
    }

    @Override
    public void start() {
        super.start();
        this.witherSkeleton.setAttacking(true);
        this.witherSkeleton.setCurrentHand(Hand.MAIN_HAND);
    }

    @Override
    public void stop() {
        super.stop();
        this.witherSkeleton.clearActiveItem();
        this.witherSkeleton.setAttacking(false);
    }
}
