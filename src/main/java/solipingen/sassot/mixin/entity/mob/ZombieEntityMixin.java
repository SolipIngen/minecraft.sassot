package solipingen.sassot.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;


@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity {


    protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        float f = random.nextFloat();
        float f2 = this.world.getDifficulty() == Difficulty.HARD ? 0.5f : 0.25f;
        if (f < f2) {
            int i = random.nextInt(3);
            float f3 = random.nextFloat()*localDifficulty.getClampedLocalDifficulty();
            if (i == 0) {
                if (f3 > 0.999f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.DIAMOND_SPEAR));
                }
                else if (f3 > 0.75f && f3 <= 0.999f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR));
                }
                else if (f3 > 0.5f && f3 <= 0.75f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SPEAR));
                }
                else if (f3 > 0.35f && f3 <= 0.5f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FLINT_SPEAR));
                }
                else if (f3 > 0.15f && f3 <= 0.35f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
                }
                else {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WOODEN_SPEAR));
                    Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(this.getBlockPos(), 4, 1, 4);
                    for (BlockPos blockPos : blockPosIterable) {
                        if (this.world.getBlockState(blockPos).isOf(Blocks.BAMBOO)) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BAMBOO_SPEAR));
                            break;
                        }
                    }
                }
            }
            else {
                if (f3 > 0.999f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                }
                else if (f3 > 0.75f && f3 <= 0.999f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                }
                else if (f3 > 0.35f && f3 <= 0.75f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SWORD));
                }
                else if (f3 > 0.15f && f3 <= 0.35f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
                else {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
                }
            }
        }
        else {
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }


    
}
