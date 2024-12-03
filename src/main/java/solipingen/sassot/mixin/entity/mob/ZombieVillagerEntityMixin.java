package solipingen.sassot.mixin.entity.mob;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.item.ModItems;


@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerEntityMixin extends ZombieEntity implements VillagerDataContainer {


    public ZombieVillagerEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("HEAD"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cbireturn) {
        if (spawnReason == SpawnReason.STRUCTURE) {
            if (world.getRandom().nextInt(3) == 0) {
                float meleeEquipThreshold = 0.15f*difficulty.getGlobalDifficulty().getId() + 0.15f*difficulty.getClampedLocalDifficulty();
                if (world.getRandom().nextFloat() < meleeEquipThreshold) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WOODEN_SPEAR));
                    Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(this.getBlockPos(), 4, 1, 4);
                    for (BlockPos blockPos : blockPosIterable) {
                        if (world.getBlockState(blockPos).isOf(Blocks.BAMBOO)) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BAMBOO_SPEAR));
                            break;
                        }
                    }
                    if (world.getRandom().nextBoolean()) {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
                    }
                }
                else {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
                    if (world.getRandom().nextBoolean()) {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                    }
                }
            }
        }
        else {
            this.initEquipment(world.getRandom(), difficulty);
        }
        this.updateEnchantments(world, world.getRandom(), difficulty);
    }



}
