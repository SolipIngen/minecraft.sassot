package solipingen.sassot.client.util.interfaces.mixin.render;


public interface LivingEntityRenderStateInterface {

    public boolean isPlayer();
    public void setIsPlayer(boolean bl);

    public boolean getIsAttackingWithSpear();
    public void setIsAttackingWithSpear(boolean bl);

    public boolean getIsAttackingWithBlazearm();
    public void setIsAttackingWithBlazearm(boolean bl);

    public boolean getIsUsingWhirlwind();
    public void setIsUsingWhirlwind(boolean bl);

    public boolean getIsUsingFlare();
    public void setIsUsingFlare(boolean bl);

    public boolean getIsUsingTridentRiptide();
    public void setIsUsingTridentRiptide(boolean bl);

    public boolean getIsBeingSnowedOn();
    public void setIsBeingSnowedOn(boolean bl);

    public boolean getInPowderSnow();
    public void setInPowderSnow(boolean bl);
}
