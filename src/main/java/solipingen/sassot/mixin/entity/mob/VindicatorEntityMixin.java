package solipingen.sassot.mixin.entity.mob;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;


@Mixin(VindicatorEntity.class)
public abstract class VindicatorEntityMixin extends IllagerEntity {

    
    protected VindicatorEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cbireturn) {
        if (spawnReason == SpawnReason.STRUCTURE || this.isPatrolLeader()) {
            float axeEquipFloat = world.getRandom().nextFloat();
            if (axeEquipFloat > 0.96f - 0.005f*(world.getDifficulty().getId() - 1)) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
            }
            else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
            }
            this.updateEnchantments(world.getRandom(), difficulty);
        }
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        float axeEquipFloat = random.nextFloat();
        if (axeEquipFloat > 0.97f - 0.005f*(this.world.getDifficulty().getId() - 1) - 0.01f*localDifficulty.getClampedLocalDifficulty()) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
        }
        else if (axeEquipFloat <= 0.97f - 0.005f*(this.world.getDifficulty().getId() - 1) - 0.01f*localDifficulty.getClampedLocalDifficulty() && axeEquipFloat > 0.5f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty()) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        }
        else if (axeEquipFloat <= 0.5f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.2f*localDifficulty.getClampedLocalDifficulty() && axeEquipFloat > 0.2f - 0.05f*(this.world.getDifficulty().getId() - 1) - 0.1f*localDifficulty.getClampedLocalDifficulty()) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_AXE));
        }
        else {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
        }
    }


    
}
