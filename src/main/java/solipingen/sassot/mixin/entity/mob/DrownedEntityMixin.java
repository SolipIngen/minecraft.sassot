package solipingen.sassot.mixin.entity.mob;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;


@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin extends ZombieEntity implements RangedAttackMob {


    public DrownedEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "initCustomGoals", constant = @Constant(intValue = 40))
    private int modifiedAttackInterval(int originalInterval) {
        return originalInterval - 5*(this.getWorld().getDifficulty().getId() - 1);
    }

    @ModifyConstant(method = "initialize", constant = @Constant(floatValue = 0.03f))
    private float modifiedNautilusThreshold(float originalf) {
        return 0.045f;
    }

    @ModifyConstant(method = "initEquipment", constant = @Constant(doubleValue = 0.9))
    private double modifiedEquipThreshold(double originald, Random random, LocalDifficulty localDifficulty) {
        double threshold = 1.0 - MathHelper.clamp(this.getWorld().getDifficulty().getId() * localDifficulty.getClampedLocalDifficulty(), 0.0f, 0.2f + 0.1f*this.getWorld().getDifficulty().getId());
        return threshold;
    }

    @ModifyConstant(method = "initEquipment", constant = @Constant(intValue = 10))
    private int modifiedTridentThreshold(int originalInt, Random random, LocalDifficulty localDifficulty) {
        return 6 + Math.round(this.getWorld().getDifficulty().getId()*(1.0f + localDifficulty.getClampedLocalDifficulty()));
    }

    @Redirect(method = "initEquipment", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;FISHING_ROD:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC))
    private Item redirectedFishingRod(Random random, LocalDifficulty localDifficulty) {
        int i = 0;
        for (int j = 0; j < 4; j++) {
            if (random.nextFloat() < localDifficulty.getClampedLocalDifficulty()) {
                i++;
            }
            else {
                break;
            }
        }
        switch (i) {
            case 1: return ModItems.COPPER_FUSED_FISHING_ROD;
            case 2: return ModItems.IRON_FUSED_FISHING_ROD;
            case 3: return ModItems.GOLD_FUSED_FISHING_ROD;
            case 4: return ModItems.DIAMOND_FUSED_FISHING_ROD;
            default: return Items.FISHING_ROD;
        }
    }

    @ModifyConstant(method = "shootAt", constant = @Constant(doubleValue = (double)0.2f))
    private double modifiedSpearThrowHeight(double originalDouble) {
        return 0.15;
    }

    @ModifyConstant(method = "shootAt", constant = @Constant(floatValue = 1.6f))
    private float modifiedThrowSpeed(float originalf) {
        int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        return 1.8f + 0.1f*strengthLevel - 0.1f*weaknessLevel;
    }

    
}
