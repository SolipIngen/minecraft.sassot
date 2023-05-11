package solipingen.sassot.mixin.entity.passive;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;


@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends GolemEntity implements Angerable {


    protected IronGolemEntityMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canTarget", at = @At("TAIL"), cancellable = true)
    private void injectedCanTarget(EntityType<?> type, CallbackInfoReturnable<Boolean> cbireturn) {
        if (type == EntityType.VILLAGER || type == EntityType.WANDERING_TRADER) {
            cbireturn.setReturnValue(false);
        }
    }

    
}
