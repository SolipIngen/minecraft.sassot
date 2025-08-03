package solipingen.sassot.mixin.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import solipingen.sassot.client.util.interfaces.mixin.render.LivingEntityRenderStateInterface;


@Mixin(LivingEntityRenderState.class)
@Environment(value = EnvType.CLIENT)
public abstract class LivingEntityRenderStateMixin extends EntityRenderState implements LivingEntityRenderStateInterface {
    @Unique private boolean isPlayer;
    @Unique private boolean isAttackingWithSpear;
    @Unique private boolean isAttackingWithBlazearm;
    @Unique private boolean usingWhirlwind;
    @Unique private boolean usingFlare;
    @Unique private boolean usingTridentRiptide;
    @Unique private boolean isBeingSnowedOn;
    @Unique private boolean inPowderSnow;


    @Override
    public boolean isPlayer() {
        return this.isPlayer;
    }

    @Override
    public void setIsPlayer(boolean bl) {
        this.isPlayer = bl;
    }

    @Override
    public boolean getIsAttackingWithSpear() {
        return this.isAttackingWithSpear;
    }

    @Override
    public void setIsAttackingWithSpear(boolean bl) {
        this.isAttackingWithSpear = bl;
    }

    @Override
    public boolean getIsAttackingWithBlazearm() {
        return this.isAttackingWithBlazearm;
    }

    @Override
    public void setIsAttackingWithBlazearm(boolean bl) {
        this.isAttackingWithBlazearm = bl;
    }

    @Override
    public boolean getIsUsingWhirlwind() {
        return this.usingWhirlwind;
    }

    @Override
    public void setIsUsingWhirlwind(boolean bl) {
        this.usingWhirlwind = bl;
    }

    @Override
    public boolean getIsUsingFlare() {
        return this.usingFlare;
    }

    @Override
    public void setIsUsingFlare(boolean bl) {
        this.usingFlare = bl;
    }

    @Override
    public boolean getIsUsingTridentRiptide() {
        return this.usingTridentRiptide;
    }

    @Override
    public void setIsUsingTridentRiptide(boolean bl) {
        this.usingTridentRiptide = bl;
    }

    @Override
    public boolean getIsBeingSnowedOn() {
        return this.isBeingSnowedOn;
    }

    @Override
    public void setIsBeingSnowedOn(boolean bl) {
        this.isBeingSnowedOn = bl;
    }

    @Override
    public boolean getInPowderSnow() {
        return this.inPowderSnow;
    }

    @Override
    public void setInPowderSnow(boolean bl) {
        this.inPowderSnow = bl;
    }


}
