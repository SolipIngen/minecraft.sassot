package solipingen.sassot.entity.ai.goal;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.village.ModVillagerProfessions;


public class VillagerMeleeAttackGoal extends MeleeAttackGoal {
    private final VillagerEntity villager;

    public VillagerMeleeAttackGoal(VillagerEntity villager) {
        super(villager, 0.67, false);
        this.villager = villager;
    }

    @Override
    public void start() {
        super.start();
        if (this.villager.getWorld() instanceof ServerWorld) {
            this.villager.playSound(ModSoundEvents.VILLAGER_ATTACK, 1.0f, this.villager.getSoundPitch());
        }
    }

    @Override
    public void stop() {
        super.stop();
        VillagerEntity villager = (VillagerEntity)this.mob;
        VillagerProfession profession = villager.getVillagerData().getProfession();
        if (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN) {
            int i = villager.getVillagerData().getLevel();
            boolean levelUpBl = VillagerData.canLevelUp(i) && villager.getExperience() >= VillagerData.getUpperLevelExperience(i);
            if (levelUpBl) {
                villager.setVillagerData(villager.getVillagerData().withLevel(i + 1));
                villager.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1));
            }
        }
        villager.reinitializeBrain((ServerWorld)villager.getWorld());
    }

}
