package solipingen.sassot.mixin.entity.projectile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import solipingen.sassot.entity.projectile.BlazearmEntity;
import solipingen.sassot.entity.projectile.spear.SpearEntity;
import solipingen.sassot.sound.ModSoundEvents;


@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {


    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "onBlockHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setSound(Lnet/minecraft/sound/SoundEvent;)V"))
    private void redirectedSetSound(PersistentProjectileEntity persistentProjectileEntity, SoundEvent soundEvent) {
        if (persistentProjectileEntity instanceof SpearEntity) {
            persistentProjectileEntity.setSound(ModSoundEvents.SPEAR_HIT_BLOCK);
        }
        else if ((PersistentProjectileEntity)(Object)this instanceof TridentEntity) {
            persistentProjectileEntity.setSound(SoundEvents.ITEM_TRIDENT_HIT_GROUND);
        }
        else if ((PersistentProjectileEntity)(Object)this instanceof BlazearmEntity) {
            persistentProjectileEntity.setSound(ModSoundEvents.BLAZEARM_HIT_BLOCK);
        }
        else {
            persistentProjectileEntity.setSound(SoundEvents.ENTITY_ARROW_HIT);
        }
    }

    @ModifyConstant(method = "age", constant = @Constant(intValue = 1200))
    private int modifiedAgeLimit(int originalInt) {
        if (((PersistentProjectileEntity)(Object)this).pickupType == PickupPermission.ALLOWED && (((PersistentProjectileEntity)(Object)this) instanceof SpearEntity || ((PersistentProjectileEntity)(Object)this) instanceof TridentEntity || ((PersistentProjectileEntity)(Object)this) instanceof BlazearmEntity)) {
            return 3*originalInt;
        }
        return originalInt;
    }

    @ModifyConstant(method = "getDragInWater", constant = @Constant(floatValue = 0.6f))
    private float modifiedWaterDrag(float originalf) {
        return 0.67f;
    }


    
}
