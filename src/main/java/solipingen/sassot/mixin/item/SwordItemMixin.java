package solipingen.sassot.mixin.item;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends ToolItem implements Vanishable {


    public SwordItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @ModifyConstant(method = "getMiningSpeedMultiplier", constant = @Constant(floatValue = 15.0f))
    private float modifiedCobwebMiningSpeedMultiplier(float originalf) {
        return 20.0f;
    }

    @ModifyConstant(method = "getMiningSpeedMultiplier", constant = @Constant(floatValue = 1.5f))
    private float modifiedPlantMiningSpeedMultiplier(float originalf) {
        return 2.0f;
    }

    @Redirect(method = "postMine", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    private void redirectedStackDamage(ItemStack originalItemStack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback, ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (this.getMiningSpeedMultiplier(stack, state) > 1.0f) {
            stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
    }

    
}
