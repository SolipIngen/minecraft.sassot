package solipingen.sassot.util.interfaces.mixin.entity;


import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.jetbrains.annotations.Nullable;

public interface LivingEntityInterface {
    
    public boolean getIsSkewered();
    public void setIsSkewered(boolean isSkewered);

    public boolean canBeSkewered();

    @Nullable
    public PersistentProjectileEntity getSkeweringEntity();
    public void setSkeweringEntity(@Nullable PersistentProjectileEntity entity);

    public boolean getIsUsingWhirlwind();
    public void setIsUsingWhirlwind(boolean usingWhirlwind);

    public boolean getIsUsingFlare();
    public void setIsUsingFlare(boolean usingFlare);

    public boolean getIsUsingTridentRiptide();
    public void setIsUsingTridentRiptide(boolean usingRiptide);

    public int getRiptideTicks();



}
