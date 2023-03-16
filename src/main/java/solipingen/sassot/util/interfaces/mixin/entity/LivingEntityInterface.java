package solipingen.sassot.util.interfaces.mixin.entity;

import net.minecraft.entity.damage.DamageSource;

public interface LivingEntityInterface {
    
    public boolean getIsSkewered();
    public void setIsSkewered(boolean isSkewered);

    public long getLastDamageTime();

    public DamageSource getLastDamageSource();
    public void setLastDamageSource(DamageSource damageSource);

    public boolean getIsUsingWhirlwind();
    public void setIsUsingWhirlwind(boolean usingWhirlwind);

    public boolean getIsUsingFlare();
    public void setIsUsingFlare(boolean usingFlare);

    public int getRiptideTicks();



}
