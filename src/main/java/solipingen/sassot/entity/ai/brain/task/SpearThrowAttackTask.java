package solipingen.sassot.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.TargetUtil;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.registry.tag.ModItemTags;

import java.util.Optional;


public class SpearThrowAttackTask<E extends MobEntity, T extends LivingEntity> extends MultiTickTask<E> {
    private final int minIntervalTicks;
    private final int maxIntervalTicks;
    private final float maxShootRange;
    private final float squaredMaxShootRange;
    private int updateCountdownTicks = -1;
    private int seenTargetTicks;


    public SpearThrowAttackTask(int intervalTicks, float maxShootRange) {
        this(intervalTicks, intervalTicks, maxShootRange);
    }

    public SpearThrowAttackTask(int minIntervalTicks, int maxIntervalTicks, float maxShootRange) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT), 1200);
        this.minIntervalTicks = minIntervalTicks;
        this.maxIntervalTicks = maxIntervalTicks;
        this.maxShootRange = maxShootRange;
        this.squaredMaxShootRange = maxShootRange * maxShootRange;
    }

    @Override
    protected boolean shouldRun(ServerWorld serverWorld, E mobEntity) {
        Optional<LivingEntity> targetOptional = SpearThrowAttackTask.getAttackTarget(mobEntity);
        if (targetOptional.isEmpty()) {
            return false;
        }
        LivingEntity targetEntity = targetOptional.get();
        boolean spearBl = mobEntity.isHolding(stack -> stack.isIn(ModItemTags.PIGLIN_PREFERRED_THROWING_WEAPONS));
        return spearBl && TargetUtil.isVisibleInMemory(mobEntity, targetEntity) && mobEntity.squaredDistanceTo(targetEntity) <= this.squaredMaxShootRange;
    }

    @Override
    protected void run(ServerWorld serverWorld, E entity, long time) {
        SpearThrowAttackTask.getAttackTarget(entity).ifPresent(targetEntity -> {
            this.setLookTarget(entity, targetEntity);
            this.tickState(serverWorld, entity, targetEntity);
        });
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld serverWorld, E mobEntity, long time) {
        Optional<LivingEntity> targetOptional = SpearThrowAttackTask.getAttackTarget(mobEntity);
        if (targetOptional.isEmpty()) {
            return false;
        }
        return mobEntity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) && this.shouldRun(serverWorld, mobEntity);
    }

    @Override
    protected void keepRunning(ServerWorld serverWorld, E mobEntity, long time) {
        this.run(serverWorld, mobEntity, time);
    }

    @Override
    protected void finishRunning(ServerWorld serverWorld, E mobEntity, long time) {
        if (mobEntity.isUsingItem()) {
            mobEntity.clearActiveItem();
        }
        this.seenTargetTicks = 0;
        this.updateCountdownTicks = -1;
    }

    private void tickState(ServerWorld serverWorld, E entity, LivingEntity target) {
        double d = entity.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
        boolean bl = entity.getVisibilityCache().canSee(target);
        this.seenTargetTicks = bl ? ++this.seenTargetTicks : 0;
        if (d > (double)this.squaredMaxShootRange || this.seenTargetTicks < 5) {
            entity.getNavigation().startMovingTo(target, entity.getMovementSpeed());
        }
        else {
            entity.getNavigation().stop();
        }
        entity.getLookControl().lookAt(target, 30.0f, 30.0f);
        if (--this.updateCountdownTicks == 0) {
            if (!bl) {
                return;
            }
            float f = (float)Math.sqrt(d) / this.maxShootRange;
            float g = MathHelper.clamp(f, 0.1f, 1.0f);
            if (d <= (double)this.squaredMaxShootRange && !entity.isInAttackRange(target)) {
                ((SpearThrowingMob)entity).spearAttack(target, g);
                this.updateCountdownTicks = MathHelper.floor(f * (float)(this.maxIntervalTicks - this.minIntervalTicks) + (float)this.minIntervalTicks);
            }
            else if (entity.isInAttackRange(target)) {
                entity.swingHand(Hand.MAIN_HAND);
                entity.tryAttack(serverWorld, target);
            }
        }
        else if (this.updateCountdownTicks < 0) {
            this.updateCountdownTicks = entity.isInAttackRange(target) ? 20 : MathHelper.floor(MathHelper.lerp(Math.sqrt(d) / (double)this.maxShootRange, (double)this.minIntervalTicks, (double)this.maxIntervalTicks));
        }
    }

    private void setLookTarget(MobEntity entity, LivingEntity target) {
        entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(target, true));
    }

    private static Optional<LivingEntity> getAttackTarget(LivingEntity entity) {
        return entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET);
    }



}
