package solipingen.sassot.mixin.entity.mob;

import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow @Final private static double ATTACK_RANGE;

    @Invoker("getAttackDamageWith")
    public abstract double invokeGetAttackDamageWith(ItemStack stack);


    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "prefersNewEquipment", at = @At("TAIL"), cancellable = true)
    private void injectedPrefersNewEquipment(ItemStack newStack, ItemStack oldStack, CallbackInfoReturnable<Boolean> cbireturn) {
        if (newStack.isIn(ModItemTags.SPEARS)) {
            if (!(oldStack.isIn(ModItemTags.SPEARS))) {
                cbireturn.setReturnValue(true);
            }
            else {
                if (this.invokeGetAttackDamageWith(newStack) != this.invokeGetAttackDamageWith(oldStack)) {
                    cbireturn.setReturnValue(this.invokeGetAttackDamageWith(newStack) > this.invokeGetAttackDamageWith(oldStack));
                }
                else {
                    cbireturn.setReturnValue(((MobEntity)(Object)this).prefersNewDamageableItem(newStack, oldStack));
                }
            }
        }
        if (newStack.isOf(Items.TRIDENT)) {
            if (!(oldStack.isOf(Items.TRIDENT))) {
                cbireturn.setReturnValue(true);
            }
            else {
                cbireturn.setReturnValue(((MobEntity)(Object)this).prefersNewDamageableItem(newStack, oldStack));
            }
        }
        if (newStack.isOf(ModItems.BLAZEARM)) {
            if (!(oldStack.isOf(ModItems.BLAZEARM))) {
                cbireturn.setReturnValue(true);
            }
            else {
                cbireturn.setReturnValue(((MobEntity)(Object)this).prefersNewDamageableItem(newStack, oldStack));
            }
        }
    }

    @Inject(method = "getAttackBox", at = @At("HEAD"), cancellable = true)
    private void injectedGetAttackBox(CallbackInfoReturnable<Box> cbireturn) {
        ItemStack mainHandStack = this.getMainHandStack();
        if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
            Box box3;
            Entity entity = this.getVehicle();
            if (entity != null) {
                Box box = entity.getBoundingBox();
                Box box2 = this.getBoundingBox();
                box3 = new Box(Math.min(box2.minX, box.minX), box2.minY, Math.min(box2.minZ, box.minZ), Math.max(box2.maxX, box.maxX), box2.maxY, Math.max(box2.maxZ, box.maxZ));
            } 
            else {
                box3 = this.getBoundingBox();
            }
            cbireturn.setReturnValue(box3.expand(ATTACK_RANGE + 0.5, 0.5, ATTACK_RANGE + 0.5));
        }
        else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
            Box box3;
            Entity entity = this.getVehicle();
            if (entity != null) {
                Box box = entity.getBoundingBox();
                Box box2 = this.getBoundingBox();
                box3 = new Box(Math.min(box2.minX, box.minX), box2.minY, Math.min(box2.minZ, box.minZ), Math.max(box2.maxX, box.maxX), box2.maxY, Math.max(box2.maxZ, box.maxZ));
            } 
            else {
                box3 = this.getBoundingBox();
            }
            cbireturn.setReturnValue(box3.expand(ATTACK_RANGE + 1.0, 1.0, ATTACK_RANGE + 1.0));
        }
    }
    
    


}
