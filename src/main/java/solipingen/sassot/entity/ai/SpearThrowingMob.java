package solipingen.sassot.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;


public interface SpearThrowingMob extends RangedAttackMob {
    
    public void spearAttack(LivingEntity target, float pullProgress);

}
