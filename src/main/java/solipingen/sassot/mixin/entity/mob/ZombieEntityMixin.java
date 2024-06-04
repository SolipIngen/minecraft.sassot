package solipingen.sassot.mixin.entity.mob;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.mob.ZombieEntity.ZombieData;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;


@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity {

    @Invoker("shouldBreakDoors")
    public abstract boolean invokeShouldBreakDoors();

    @Invoker("applyAttributeModifiers")
    public abstract void invokeApplyAttributeModifiers(float chanceMultiplier);


    protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Inject(method = "initialize", at = @At("HEAD"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cbireturn) {
        Random random = world.getRandom();
        entityData = super.initialize(world, difficulty, spawnReason, entityData);
        float f = difficulty.getClampedLocalDifficulty();
        this.setCanPickUpLoot(random.nextFloat() < 0.55f * f);
        if (entityData == null) {
            entityData = new ZombieData(ZombieEntity.shouldBeBaby(random), true);
        }
        if (entityData instanceof ZombieData) {
            ZombieData zombieData = (ZombieData)entityData;
            if (zombieData.baby) {
                this.setBaby(true);
                if (zombieData.tryChickenJockey) {
                    ChickenEntity chickenEntity2;
                    if ((double)random.nextFloat() < 0.05) {
                        List<ChickenEntity> list = world.getEntitiesByClass(ChickenEntity.class, this.getBoundingBox().expand(5.0, 3.0, 5.0), EntityPredicates.NOT_MOUNTED);
                        if (!list.isEmpty()) {
                            ChickenEntity chickenEntity = (ChickenEntity)list.get(0);
                            chickenEntity.setHasJockey(true);
                            this.startRiding(chickenEntity);
                        }
                    } else if ((double)random.nextFloat() < 0.05 && (chickenEntity2 = EntityType.CHICKEN.create(this.getWorld())) != null) {
                        chickenEntity2.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
                        chickenEntity2.initialize(world, difficulty, SpawnReason.JOCKEY, null);
                        chickenEntity2.setHasJockey(true);
                        this.startRiding(chickenEntity2);
                        world.spawnEntity(chickenEntity2);
                    }
                }
            }
            ((ZombieEntity)(Object)this).setCanBreakDoors(this.invokeShouldBreakDoors() && random.nextFloat() < f * 0.1f);
            if (((ZombieEntity)(Object)this) instanceof ZombieVillagerEntity && spawnReason == SpawnReason.STRUCTURE) {
                if (random.nextInt(3) == 0) {
                    float meleeEquipThreshold = 0.15f*world.getDifficulty().getId() + 0.15f*difficulty.getClampedLocalDifficulty();
                    if (random.nextFloat() < meleeEquipThreshold) {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WOODEN_SPEAR));
                        Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(this.getBlockPos(), 4, 1, 4);
                        for (BlockPos blockPos : blockPosIterable) {
                            if (world.getBlockState(blockPos).isOf(Blocks.BAMBOO)) {
                                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BAMBOO_SPEAR));
                                break;
                            }
                        }
                        if (random.nextBoolean()) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
                        }
                    }
                    else {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
                        if (random.nextBoolean()) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                        }
                    }
                }
            }
            else {
                this.initEquipment(random, difficulty);
            }
            this.updateEnchantments(random, difficulty);
        }
        if (this.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate localDate = LocalDate.now();
            int i = localDate.get(ChronoField.DAY_OF_MONTH);
            int j = localDate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && random.nextFloat() < 0.25f) {
                this.equipStack(EquipmentSlot.HEAD, new ItemStack(random.nextFloat() < 0.1f ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getEntitySlotId()] = 0.0f;
            }
        }
        this.invokeApplyAttributeModifiers(f);
        cbireturn.setReturnValue(entityData);
    }

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo cbi) {
        float f = random.nextFloat();
        float f2 = this.getWorld().getDifficulty() == Difficulty.HARD ? 0.5f : 0.25f;
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
                        if (this.getWorld().getBlockState(blockPos).isOf(Blocks.BAMBOO)) {
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
