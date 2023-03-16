package solipingen.sassot.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;


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
        LivingEntity livingEntity = SpearThrowAttackTask.getAttackTarget(mobEntity);
        boolean spearBl = (((LivingEntity)mobEntity).getMainHandStack().getItem() instanceof SpearItem || (((LivingEntity)mobEntity).getOffHandStack().getItem() instanceof SpearItem || ((LivingEntity)mobEntity).getMainHandStack().getItem() instanceof BlazearmItem));
        if (spearBl) {
            this.tickState(mobEntity, livingEntity);
        }
        return (((LivingEntity)mobEntity).getMainHandStack().getItem() instanceof SpearItem || ((LivingEntity)mobEntity).getMainHandStack().isOf(ModItems.BLAZEARM)) && LookTargetUtil.isVisibleInMemory(mobEntity, livingEntity) && LookTargetUtil.isTargetWithinAttackRange(mobEntity, livingEntity, 0);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld serverWorld, E mobEntity, long l) {
        return ((LivingEntity)mobEntity).getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) && this.shouldRun(serverWorld, mobEntity);
    }

    @Override
    protected void keepRunning(ServerWorld serverWorld, E mobEntity, long l) {
        LivingEntity livingEntity = SpearThrowAttackTask.getAttackTarget(mobEntity);
        this.setLookTarget((MobEntity)mobEntity, livingEntity);
        this.tickState(mobEntity, livingEntity);
    }

    @Override
    protected void finishRunning(ServerWorld serverWorld, E mobEntity, long l) {
        if (((LivingEntity)mobEntity).isUsingItem()) {
            ((LivingEntity)mobEntity).clearActiveItem();
        }
        this.seenTargetTicks = 0;
        this.updateCountdownTicks = -1;
    }

    public void tickState(E entity, LivingEntity target) {
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
            ((SpearThrowingMob)entity).spearAttack(target, g);
            this.updateCountdownTicks = MathHelper.floor(f * (float)(this.maxIntervalTicks - this.minIntervalTicks) + (float)this.minIntervalTicks);
        } 
        else if (this.updateCountdownTicks < 0) {
            this.updateCountdownTicks = MathHelper.floor(MathHelper.lerp(Math.sqrt(d) / (double)this.maxShootRange, (double)this.minIntervalTicks, (double)this.maxIntervalTicks));
        }
    }

    private void setLookTarget(MobEntity entity, LivingEntity target) {
        entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(target, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity entity) {
        return entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).get();
    }



}
