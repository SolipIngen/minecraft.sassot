package solipingen.sassot.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;


@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin extends ZombieEntity implements RangedAttackMob {


    public DrownedEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "initCustomGoals", constant = @Constant(intValue = 40))
    private int modifiedAttackInterval(int originalInterval) {
        return originalInterval - 5*(this.world.getDifficulty().getId() - 1);
    }

    @ModifyConstant(method = "initialize", constant = @Constant(floatValue = 0.03f))
    private float modifiedNautilusThreshold(float originalf) {
        return 0.045f;
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        float tridentThreshold = MathHelper.clamp(this.world.getDifficulty().getId() * localDifficulty.getClampedLocalDifficulty(), 0.0f, 0.2f + 0.1f*this.world.getDifficulty().getId());
        if (random.nextFloat() < tridentThreshold) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
        }
        else {
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        this.updateEnchantments(random, localDifficulty);
    }

    @ModifyConstant(method = "attack", constant = @Constant(doubleValue = (double)0.2f))
    private double modifiedSpearThrowHeight(double originalDouble) {
        return 0.15;
    }

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.6f))
    private float modifiedThrowSpeed(float originalf) {
        int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        return 1.8f + 0.1f*strengthLevel - 0.1f*weaknessLevel;
    }

    
}
