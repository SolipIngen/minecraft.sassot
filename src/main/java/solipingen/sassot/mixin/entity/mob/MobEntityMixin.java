package solipingen.sassot.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.ModShieldItem;
import solipingen.sassot.item.SpearItem;


@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    @Invoker("updateEnchantments")
    public abstract void invokeUpdateEnchantments(Random random, LocalDifficulty localDifficulty);


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

    @Inject(method = "disablePlayerShield", at = @At("HEAD"), cancellable = true)
    private void injectedDisablePlayerShield(PlayerEntity player, ItemStack mobStack, ItemStack playerStack, CallbackInfo cbi) {
        if (this.disablesShield()) {
            if (!(playerStack.getItem() instanceof ShieldItem)) {
                cbi.cancel();
            }
            if (!this.world.isClient) {
                player.incrementStat(Stats.USED.getOrCreateStat(playerStack.getItem()));
            }
            float damageReductionf = playerStack.getItem() instanceof ModShieldItem ? ((ModShieldItem)playerStack.getItem()).getMinDamageToBreak() : 3.0f;
            float f = 1.0f - (float)Math.exp(-MathHelper.square(damageReductionf/((4 - this.world.getDifficulty().getId())*20.0)));
            f += 0.05f*this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
            int unyieldingLevel = EnchantmentHelper.getLevel(ModEnchantments.UNYIELDING, playerStack);
            float randomf = this.random.nextFloat();
            if (playerStack.isOf(Items.SHIELD)) {
                f -= 0.05f*unyieldingLevel;
                if (randomf < f) {
                    player.getItemCooldownManager().set(playerStack.getItem(), 60 - 20*unyieldingLevel);
                    player.clearActiveItem();
                    player.world.sendEntityStatus(player, EntityStatuses.BREAK_SHIELD);
                }
            }
            else if (playerStack.getItem() instanceof ModShieldItem) {
                ModShieldItem modShieldItem = (ModShieldItem)playerStack.getItem();
                f -= modShieldItem.getUnyieldingModifier()*unyieldingLevel;
                if (randomf < f) {
                    player.getItemCooldownManager().set(playerStack.getItem(), modShieldItem.getDisabledTicks() - 20*unyieldingLevel);
                    player.clearActiveItem();
                    player.world.sendEntityStatus(player, EntityStatuses.BREAK_SHIELD);
                }
            }
        }
        cbi.cancel();
    }

    @Inject(method = "squaredAttackRange", at = @At("HEAD"), cancellable = true)
    private void injectedSquaredAttackRange(LivingEntity target, CallbackInfoReturnable<Double> cbireturn) {
        Item mainHandItem = this.getMainHandStack().getItem();
        if (mainHandItem instanceof SwordItem) {
            cbireturn.setReturnValue((double)MathHelper.square(this.getWidth()*2.0f + 1.0f + target.getWidth()));
        }
        else if (mainHandItem instanceof SpearItem || this.getMainHandStack().isOf(ModItems.BLAZEARM)) {
            cbireturn.setReturnValue((double)MathHelper.square(this.getWidth()*2.0f + 2.0f + target.getWidth()));
        }
    }
    
    


}
