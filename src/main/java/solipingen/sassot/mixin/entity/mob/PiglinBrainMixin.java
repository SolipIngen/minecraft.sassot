package solipingen.sassot.mixin.entity.mob;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.AttackTask;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.ai.brain.task.ForgetAttackTargetTask;
import net.minecraft.entity.ai.brain.task.ForgetTask;
import net.minecraft.entity.ai.brain.task.HuntFinishTask;
import net.minecraft.entity.ai.brain.task.MeleeAttackTask;
import net.minecraft.entity.ai.brain.task.RangedApproachTask;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.entity.ai.brain.task.SpearThrowAttackTask;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {

    @Invoker("isPreferredAttackTarget")
    public static boolean invokeIsPreferredAttackTarget(ServerWorld world, PiglinEntity piglin, LivingEntity target) {
        throw new AssertionError();
    }

    @Invoker("getNearestZombifiedPiglin")
    public static boolean invokeGetNearestZombifiedPiglin(PiglinEntity piglin) {
        throw new AssertionError();
    }

    @Invoker("isHoldingCrossbow")
    public static boolean invokeIsHoldingCrossbow(LivingEntity piglin) {
        throw new AssertionError();
    }


    @Inject(method = "addFightActivities", at = @At("HEAD"), cancellable = true)
    private static void injectedAddFightActivities(PiglinEntity piglin, Brain<PiglinEntity> brain, CallbackInfo cbi) {
        brain.setTaskList(Activity.FIGHT, 10, ImmutableList.of(
                ForgetAttackTargetTask.create((world, target) -> !PiglinBrainMixin.invokeIsPreferredAttackTarget(world, piglin, target)),
                TaskTriggerer.runIf(PiglinBrainMixin::isHoldingCrossbowOrSpear, AttackTask.create(5, 0.75f)),
                RangedApproachTask.create(1.0f),
                MeleeAttackTask.create(20), 
                new CrossbowAttackTask<>(), 
                new SpearThrowAttackTask<>(40 - 5*(piglin.getWorld().getDifficulty().getId() - 1), 10.0f),
                HuntFinishTask.create(), 
                ForgetTask.create(PiglinBrainMixin::invokeGetNearestZombifiedPiglin, MemoryModuleType.ATTACK_TARGET)), 
            MemoryModuleType.ATTACK_TARGET);
        cbi.cancel();
    }

    @Unique
    private static boolean isHoldingCrossbowOrSpear(LivingEntity piglin) {
        ItemStack itemStack = piglin.getWeaponStack();
        boolean bl = PiglinBrainMixin.invokeIsHoldingCrossbow(piglin) || itemStack.getItem() instanceof RangedWeaponItem || itemStack.isIn(ModItemTags.PIGLIN_PREFERRED_THROWING_WEAPONS);
        if (!bl) {
            itemStack = piglin.getOffHandStack();
            return PiglinBrainMixin.invokeIsHoldingCrossbow(piglin) || itemStack.getItem() instanceof RangedWeaponItem || itemStack.isIn(ModItemTags.PIGLIN_PREFERRED_THROWING_WEAPONS);
        }
        return bl;
    }




}
