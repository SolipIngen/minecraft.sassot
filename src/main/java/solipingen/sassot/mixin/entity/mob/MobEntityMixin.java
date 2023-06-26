package solipingen.sassot.mixin.entity.mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.registry.tag.ModItemTags;


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
        if (playerStack.getItem() instanceof ShieldItem && this.disablesShield()) {
            player.disableShield(true);
        }
        cbi.cancel();
    }

    @Inject(method = "squaredAttackRange", at = @At("HEAD"), cancellable = true)
    private void injectedSquaredAttackRange(LivingEntity target, CallbackInfoReturnable<Double> cbireturn) {
        ItemStack mainHandStack = this.getMainHandStack();
        if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
            cbireturn.setReturnValue((double)MathHelper.square(this.getWidth()*2.0f + 0.5f + target.getWidth()));
        }
        else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
            cbireturn.setReturnValue((double)MathHelper.square(this.getWidth()*2.0f + 1.0f + target.getWidth()));
        }
    }
    
    


}
