package solipingen.sassot.mixin.item;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import solipingen.sassot.item.ModMiningLevels;
import solipingen.sassot.registry.tag.ModBlockTags;


@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
    @Shadow @Final private TagKey<Block> effectiveBlocks;
    @Shadow @Final protected float miningSpeed;


    public MiningToolItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
    private void injectedGetMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cbireturn) {
        if (state.isIn(ModBlockTags.GLASSLIKE) && state.getBlock().getHardness() > 0.0f && stack.getItem() instanceof PickaxeItem) {
            cbireturn.setReturnValue(this.miningSpeed);
        }
    }

    @ModifyConstant(method = "postHit", constant = @Constant(intValue = 2))
    private int modifiedPostMineDurabilityDamage(int originalDamage) {
        return 1;
    }

    @Redirect(method = "postMine", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    private void redirectedStackDamage(ItemStack originalItemStack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback, ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (this.getMiningSpeedMultiplier(stack, state) > 1.0f) {
            stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
    }

    @Inject(method = "isSuitableFor", at = @At("HEAD"), cancellable = true)
    private void injectedIsSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cbireturn) {
        int i = this.getMaterial().getMiningLevel();
        if (i < ModMiningLevels.NETHERITE && state.isIn(ModBlockTags.NEEDS_NETHERITE_TOOL)) {
            cbireturn.setReturnValue(false);
        }
        else if (i < ModMiningLevels.DIAMOND && state.isIn(ModBlockTags.NEEDS_DIAMOND_TOOL)) {
            cbireturn.setReturnValue(false);
        }
        else if (i < ModMiningLevels.IRON && state.isIn(ModBlockTags.NEEDS_IRON_TOOL)) {
            cbireturn.setReturnValue(false);
        }
        else if (i < ModMiningLevels.COPPER && state.isIn(ModBlockTags.NEEDS_COPPER_TOOL)) {
            cbireturn.setReturnValue(false);
        }
        else if (i < ModMiningLevels.STONE && state.isIn(ModBlockTags.NEEDS_STONE_TOOL)) {
            cbireturn.setReturnValue(false);
        }
        else {
            cbireturn.setReturnValue(state.isIn(this.effectiveBlocks));
        }
    }


    
}
