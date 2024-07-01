package solipingen.sassot.mixin.entity.accessors;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(PersistentProjectileEntity.class)
public interface PersistentProjectileEntityInvoker {

    @Invoker("setPierceLevel")
    public void invokeSetPierceLevel(byte level);


}
