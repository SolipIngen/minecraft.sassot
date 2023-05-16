package solipingen.sassot.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
import solipingen.sassot.entity.ai.brain.task.SpearThrowAttackTask;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {


    @Invoker("isPreferredAttackTarget")
    public static boolean invokeIsPreferredAttackTarget(PiglinEntity piglin, LivingEntity target) {
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


    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PiglinBrain;addFightActivities(Lnet/minecraft/entity/mob/PiglinEntity;Lnet/minecraft/entity/ai/brain/Brain;)V"))
    private static void redirectedAddFightActivities(PiglinEntity piglin, Brain<PiglinEntity> brain) {
        brain.setTaskList(Activity.FIGHT, 10, ImmutableList.of(
                ForgetAttackTargetTask.create(target -> !PiglinBrainMixin.invokeIsPreferredAttackTarget(piglin, target)), 
                TaskTriggerer.runIf(PiglinBrainMixin::isHoldingCrossbowOrSpear, AttackTask.create(5, 0.75f)), 
                RangedApproachTask.create(1.0f), 
                MeleeAttackTask.create(20), 
                new CrossbowAttackTask<>(), 
                new SpearThrowAttackTask<>(40 - 5*(piglin.world.getDifficulty().getId() - 1), 10.0f), 
                HuntFinishTask.create(), 
                ForgetTask.create(PiglinBrainMixin::invokeGetNearestZombifiedPiglin, MemoryModuleType.ATTACK_TARGET)), 
            MemoryModuleType.ATTACK_TARGET);
    }

    private static boolean isHoldingCrossbowOrSpear(LivingEntity piglin) {
        ItemStack itemStack = piglin.getMainHandStack();
        boolean bl = PiglinBrainMixin.invokeIsHoldingCrossbow(piglin) || itemStack.getItem() instanceof RangedWeaponItem || itemStack.getItem() instanceof SpearItem || itemStack.getItem() instanceof BlazearmItem;
        if (!bl) {
            itemStack = piglin.getOffHandStack();
            return PiglinBrainMixin.invokeIsHoldingCrossbow(piglin) || itemStack.getItem() instanceof RangedWeaponItem || itemStack.getItem() instanceof SpearItem || itemStack.getItem() instanceof BlazearmItem;
        }
        return bl;
    }




}
