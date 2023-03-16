package solipingen.sassot.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

public class VillagerTrackTargetGoal extends TrackTargetGoal {
    private final VillagerEntity villager;
    @Nullable
    private LivingEntity target;
    private final TargetPredicate targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(64.0);

    public VillagerTrackTargetGoal(VillagerEntity villager) {
        super(villager, false, true);
        this.villager = villager;
        this.setControls(EnumSet.of(Goal.Control.TARGET));
    }

    @Override
    public boolean canStart() {
        Box box = this.villager.getBoundingBox().expand(10.0, 8.0, 10.0);
        List<VillagerEntity> list = this.villager.world.getTargets(VillagerEntity.class, this.targetPredicate, this.villager, box);
        List<PlayerEntity> list2 = this.villager.world.getPlayers(this.targetPredicate, this.villager, box);
        for (LivingEntity livingEntity : list) {
            VillagerEntity villagerEntity = (VillagerEntity)livingEntity;
            for (PlayerEntity playerEntity : list2) {
                int i = villagerEntity.getReputation(playerEntity);
                if (i > -100) continue;
                this.target = playerEntity;
            }
        }
        if (this.target == null) {
            return false;
        }
        return !(this.target instanceof PlayerEntity) || (!this.target.isSpectator() && !((PlayerEntity)this.target).isCreative());
    }

    @Override
    public void start() {
        this.villager.setTarget(this.target);
        super.start();
    }
}
