package solipingen.sassot.entity.ai.goal;

import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.entity.ai.SpearThrowingMob;


public class SpearThrowAttackGoal extends ProjectileAttackGoal {
    private final MobEntity mob;
    private final SpearThrowingMob owner;
    @Nullable protected LivingEntity target;
    private int updateCountdownTicks = -1;
    private final double mobSpeed;
    private int seenTargetTicks;
    private final int minIntervalTicks;
    private final int maxIntervalTicks;
    private final float maxShootRange;
    private final float squaredMaxShootRange;
    

    public SpearThrowAttackGoal(SpearThrowingMob mob, double mobSpeed, int intervalTicks, float maxShootRange) {
        this(mob, mobSpeed, intervalTicks, intervalTicks, maxShootRange);
    }
    
    public SpearThrowAttackGoal(SpearThrowingMob mob, double mobSpeed, int minIntervalTicks, int maxIntervalTicks, float maxShootRange) {
        super(mob, mobSpeed, minIntervalTicks, maxIntervalTicks, maxShootRange);
        if (!(mob instanceof LivingEntity)) {
            throw new IllegalArgumentException("SpearThrowAttackGoal requires a living Mob");
        }
        this.owner = mob;
        this.mob = (MobEntity)((Object)mob);
        this.mobSpeed = mobSpeed;
        this.minIntervalTicks = minIntervalTicks;
        this.maxIntervalTicks = maxIntervalTicks;
        this.maxShootRange = maxShootRange;
        this.squaredMaxShootRange = maxShootRange * maxShootRange;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }
    
    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null || !livingEntity.isAlive()) {
            return false;
        }
        this.target = livingEntity;
        return true;
    }
    
    @Override
    public boolean shouldContinue() {
        return this.canStart() || this.target.isAlive() && !this.mob.getNavigation().isIdle();
    }
    
    @Override
    public void stop() {
        this.target = null;
        this.seenTargetTicks = 0;
        this.updateCountdownTicks = -1;
    }
    
    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
    
    @Override
    public void tick() {
        double d = this.mob.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean bl = this.mob.getVisibilityCache().canSee(this.target);
        this.seenTargetTicks = bl ? ++this.seenTargetTicks : 0;
        if (d > (double)this.squaredMaxShootRange || this.seenTargetTicks < 5) {
            this.mob.getNavigation().startMovingTo(this.target, this.mobSpeed);
        } 
        else {
            this.mob.getNavigation().stop();
        }
        this.mob.getLookControl().lookAt(this.target, 30.0f, 30.0f);
        if (--this.updateCountdownTicks == 0) {
            if (!bl) {
                return;
            }
            float f = (float)Math.sqrt(d) / this.maxShootRange;
            float g = MathHelper.clamp(f, 0.1f, 1.0f);
            if (d > this.mob.squaredAttackRange(target)) {
                this.owner.spearAttack(this.target, g);
            }
            else {
                this.mob.swingHand(Hand.MAIN_HAND);
                this.mob.tryAttack(target);
            }
            this.updateCountdownTicks = MathHelper.floor(f * (float)(this.maxIntervalTicks - this.minIntervalTicks) + (float)this.minIntervalTicks);
        } 
        else if (this.updateCountdownTicks < 0) {
            this.updateCountdownTicks = MathHelper.floor(MathHelper.lerp(Math.sqrt(d) / (double)this.maxShootRange, (double)this.minIntervalTicks, (double)this.maxIntervalTicks));
        }
    }
}
