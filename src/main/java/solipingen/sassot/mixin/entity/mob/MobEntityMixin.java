package solipingen.sassot.mixin.entity.mob;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.ModShieldItem;
import solipingen.sassot.item.SpearItem;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {


    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "prefersNewEquipment", at = @At("TAIL"), cancellable = true)
    private void injectedPrefersNewEquipment(ItemStack newStack, ItemStack oldStack, CallbackInfoReturnable<Boolean> cbireturn) {
        if (newStack.getItem() instanceof SpearItem) {
            if (!(oldStack.getItem() instanceof SpearItem)) {
                cbireturn.setReturnValue(true);
            }
            else {
                SpearItem spearItem = (SpearItem)newStack.getItem();
                SpearItem spearItem2 = (SpearItem)oldStack.getItem();
                if (spearItem.getAttackDamage() != spearItem2.getAttackDamage()) {
                    cbireturn.setReturnValue(spearItem.getAttackDamage() > spearItem2.getAttackDamage());
                }
                else {
                    cbireturn.setReturnValue(((MobEntity)(Object)this).prefersNewDamageableItem(newStack, oldStack));
                }
            }
        }
        if (newStack.getItem() instanceof TridentItem) {
            if (!(oldStack.getItem() instanceof TridentItem)) {
                cbireturn.setReturnValue(true);
            }
            else {
                cbireturn.setReturnValue(((MobEntity)(Object)this).prefersNewDamageableItem(newStack, oldStack));
            }
        }
        if (newStack.getItem() instanceof BlazearmItem) {
            if (!(oldStack.getItem() instanceof BlazearmItem)) {
                cbireturn.setReturnValue(true);
            }
            else {
                cbireturn.setReturnValue(((MobEntity)(Object)this).prefersNewDamageableItem(newStack, oldStack));
            }
        }
    }

    @Nullable
    @Inject(method = "initialize", at = @At("TAIL"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cbireturn) {
        if (((MobEntity)(Object)this) instanceof ZombieVillagerEntity && spawnReason == SpawnReason.STRUCTURE) {
            if (world.getRandom().nextInt(3) == 0) {
                float meleeEquipThreshold = 0.15f*world.getDifficulty().getId() + 0.15f*difficulty.getClampedLocalDifficulty();
                if (world.getRandom().nextFloat() < meleeEquipThreshold) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WOODEN_SPEAR));
                    Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(this.getBlockPos(), 4, 1, 4);
                    for (BlockPos blockPos : blockPosIterable) {
                        if (this.world.getBlockState(blockPos).isOf(Blocks.BAMBOO)) {
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
            else {
                this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
        }
    }

    @Redirect(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;disablePlayerShield(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V"))
    private void redirectedDisableShield(MobEntity mobEntity, PlayerEntity playerEntity, ItemStack mobStack, ItemStack playerStack) {
        if (this.disablesShield()) {
            float amount = (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            amount += EnchantmentHelper.getAttackDamage(mobStack, playerEntity.getGroup());
            if (!(playerStack.getItem() instanceof ShieldItem)) {
                return;
            }
            if (!this.world.isClient) {
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(playerStack.getItem()));
            }
            int unyieldingLevel = EnchantmentHelper.getLevel(ModEnchantments.UNYIELDING, playerStack);
            if (playerStack.getItem() instanceof ModShieldItem && amount >= ((ModShieldItem)playerStack.getItem()).getMinDamageToBreak()) {
                int i = 1 + MathHelper.floor((1.0f - 0.25f*unyieldingLevel)*amount);
                Hand hand = playerEntity.getActiveHand();
                playerStack.damage(i, this, player -> player.sendToolBreakStatus(hand));
            }
            if (playerStack.isEmpty()) {
                Hand hand = this.getActiveHand();
                if (hand == Hand.MAIN_HAND) {
                    playerEntity.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } 
                else {
                    playerEntity.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                playerStack = ItemStack.EMPTY;
                playerEntity.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8f, 0.8f + this.world.random.nextFloat() * 0.4f);
            }
        }
    }

    @Inject(method = "squaredAttackRange", at = @At("HEAD"), cancellable = true)
    private void injectedSquaredAttackRange(LivingEntity target, CallbackInfoReturnable<Double> cbireturn) {
        Item mainHandItem = this.getMainHandStack().getItem();
        if (mainHandItem instanceof SwordItem) {
            cbireturn.setReturnValue((double)MathHelper.square(this.getWidth() * 2.0f + 1.0f + target.getWidth()));
        }
        else if (mainHandItem instanceof SpearItem || this.getMainHandStack().isOf(ModItems.BLAZEARM)) {
            cbireturn.setReturnValue((double)MathHelper.square(this.getWidth() * 2.0f + 2.0f + target.getWidth()));
        }
    }
    
    


}
