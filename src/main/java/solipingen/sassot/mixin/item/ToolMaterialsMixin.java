package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.math.MathHelper;


@Mixin(ToolMaterials.class)
public abstract class ToolMaterialsMixin implements ToolMaterial {
    @Shadow @Final private TagKey<Block> inverseTag;
    @Shadow @Final private int itemDurability;
    @Shadow @Final private float miningSpeed;


    @Inject(method = "getDurability", at = @At("HEAD"), cancellable = true)
    private void injectedGetDurability(CallbackInfoReturnable<Integer> cbireturn) {
        if (this.inverseTag == BlockTags.INCORRECT_FOR_WOODEN_TOOL) {
            cbireturn.setReturnValue(42);
        }
        else if (this.inverseTag == BlockTags.INCORRECT_FOR_STONE_TOOL) {
            cbireturn.setReturnValue(148);
        }
        else if (this.inverseTag == BlockTags.INCORRECT_FOR_GOLD_TOOL) {
            cbireturn.setReturnValue(123);
        }
        else if (this.inverseTag == BlockTags.INCORRECT_FOR_IRON_TOOL) {
            cbireturn.setReturnValue(2*this.itemDurability);
        }
        else if (this.inverseTag == BlockTags.INCORRECT_FOR_DIAMOND_TOOL || this.inverseTag == BlockTags.INCORRECT_FOR_NETHERITE_TOOL) {
            cbireturn.setReturnValue(MathHelper.ceil(1.2f*this.itemDurability));
        }
    }

    @Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
    private void injectedGetMiningSpeedMultiplier(CallbackInfoReturnable<Float> cbireturn) {
        if (this.inverseTag == BlockTags.INCORRECT_FOR_GOLD_TOOL) {
            cbireturn.setReturnValue(this.miningSpeed + 1.0f);
        }
        else if (this.inverseTag == BlockTags.INCORRECT_FOR_IRON_TOOL) {
            cbireturn.setReturnValue(this.miningSpeed + 2.0f);
        }
        else if (this.inverseTag == BlockTags.INCORRECT_FOR_DIAMOND_TOOL || this.inverseTag == BlockTags.INCORRECT_FOR_NETHERITE_TOOL) {
            cbireturn.setReturnValue(this.miningSpeed + 4.0f);
        }
    }



}
