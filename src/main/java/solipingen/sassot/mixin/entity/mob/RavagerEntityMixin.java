package solipingen.sassot.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;


@Mixin(RavagerEntity.class)
public abstract class RavagerEntityMixin extends RaiderEntity {


    protected RavagerEntityMixin(EntityType<? extends RaiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "knockBack", at = @At("TAIL"))
    private void injectedRavagerKnockback(Entity entity, CallbackInfo cbi) {
        if (entity instanceof LivingEntity) {
            float knockbackFactor = 1.0f - (float)((LivingEntity)entity).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
            if (((LivingEntity)entity).isBlocking()) {
                ItemStack shieldStack = ((LivingEntity)entity).getActiveItem();
                if (shieldStack.isOf(ModItems.IRON_SHIELD)) {
                    knockbackFactor *= 0.75f;
                }
                else if (shieldStack.isOf(ModItems.DIAMOND_SHIELD)) {
                    knockbackFactor *= 0.67f;
                }
                else if (shieldStack.isOf(ModItems.NETHERITE_SHIELD)) {
                    knockbackFactor *= 0.5f;
                }
            }
            entity.setVelocity(entity.getVelocity().multiply(knockbackFactor));
        }

    }
    

    


}
