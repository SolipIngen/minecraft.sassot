package solipingen.sassot.mixin.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import solipingen.sassot.client.util.interfaces.mixin.render.SkeletonEntityRenderStateInterface;


@Mixin(SkeletonEntityRenderState.class)
@Environment(EnvType.CLIENT)
public abstract class SkeletonEntityRenderStateMixin extends BipedEntityRenderState implements SkeletonEntityRenderStateInterface {
    @Unique private boolean throwing;

    @Override
    public boolean getIsThrowing() {
        return this.throwing;
    }

    @Override
    public void setIsThrowing(boolean bl) {
        this.throwing = bl;
    }

}
