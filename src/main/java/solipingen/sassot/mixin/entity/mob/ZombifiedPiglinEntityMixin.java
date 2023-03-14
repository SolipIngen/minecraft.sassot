package solipingen.sassot.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;


@Mixin(ZombifiedPiglinEntity.class)
public abstract class ZombifiedPiglinEntityMixin extends ZombieEntity implements Angerable {


    public ZombifiedPiglinEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        if (this.random.nextFloat() < 0.33f) {
            float spearRandomf = this.random.nextFloat()*this.world.getDifficulty().getId() + 0.1f*this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
            if (spearRandomf < 0.15f) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WOODEN_SPEAR));
            }
            else if (spearRandomf >= 0.15f && spearRandomf < 0.25f) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
            }
            else if (spearRandomf >= 0.25f && spearRandomf < 0.5f) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FLINT_SPEAR));
            }
            else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLDEN_SPEAR));
            }
        }
        else {
            float swordRandomf = this.random.nextFloat()*this.world.getDifficulty().getId() + 0.1f*this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
            if (swordRandomf < 0.15f) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
            }
            else if (swordRandomf >= 0.15f && swordRandomf < 0.5f) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
            }
            else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
            }
        }
    }


    
}
