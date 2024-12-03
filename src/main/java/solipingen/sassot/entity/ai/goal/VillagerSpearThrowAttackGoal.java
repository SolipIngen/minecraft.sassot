package solipingen.sassot.entity.ai.goal;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.village.ModVillagerProfessions;


public class VillagerSpearThrowAttackGoal extends SpearThrowAttackGoal {
    private final VillagerEntity villager;

    public VillagerSpearThrowAttackGoal(SpearThrowingMob spearThrowingMob, double d, int i, float f) {
        super(spearThrowingMob, d, i, f);
        this.villager = (VillagerEntity)spearThrowingMob;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && this.villager.getMainHandStack().getItem() instanceof SpearItem;
    }

    @Override
    public void start() {
        super.start();
        this.villager.setAttacking(true);
        this.villager.setCurrentHand(Hand.MAIN_HAND);
        if (this.villager.getWorld() instanceof ServerWorld) {
            this.villager.playSound(ModSoundEvents.VILLAGER_ATTACK, 1.0f, this.villager.getSoundPitch());
        }
    }

    @Override
    public void stop() {
        super.stop();
        VillagerProfession profession = this.villager.getVillagerData().getProfession();
        if (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN) {
            int i = villager.getVillagerData().getLevel();
            boolean levelUpBl = VillagerData.canLevelUp(i) && villager.getExperience() >= VillagerData.getUpperLevelExperience(i);
            if (levelUpBl) {
                this.villager.setVillagerData(villager.getVillagerData().withLevel(i + 1));
                this.villager.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1));
            }
        }
        this.villager.clearActiveItem();
        this.villager.setAttacking(false);
        this.villager.reinitializeBrain((ServerWorld)this.villager.getWorld());
    }
}
