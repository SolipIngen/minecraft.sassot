package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.item.ModMiningLevels;


@Mixin(ToolMaterials.class)
public abstract class ToolMaterialsMixin implements ToolMaterial {
    @Shadow @Final private int miningLevel;
    @Shadow @Final private int itemDurability;
    @Shadow @Final private float miningSpeed;


    @Inject(method = "getMiningLevel", at = @At("HEAD"), cancellable = true)
    private void injectedGetMiningLevel(CallbackInfoReturnable<Integer> cbireturn) {
        if (this.miningLevel == MiningLevels.WOOD) {
            if (this.miningSpeed >= 12.0f) {
                cbireturn.setReturnValue(ModMiningLevels.STONE);
            }
            else {
                cbireturn.setReturnValue(ModMiningLevels.WOOD);
            }
        }
        else if (this.miningLevel == MiningLevels.STONE) {
            cbireturn.setReturnValue(ModMiningLevels.STONE);
        }
        else if (this.miningLevel == MiningLevels.IRON) {
            cbireturn.setReturnValue(ModMiningLevels.IRON);
        }
        else if (this.miningLevel == MiningLevels.DIAMOND) {
            cbireturn.setReturnValue(ModMiningLevels.DIAMOND);
        }
        else if (this.miningLevel == MiningLevels.NETHERITE) {
            cbireturn.setReturnValue(ModMiningLevels.NETHERITE);
        }
    }

    @Inject(method = "getDurability", at = @At("HEAD"), cancellable = true)
    private void injectedGetDurability(CallbackInfoReturnable<Integer> cbireturn) {
        if (this.miningLevel == MiningLevels.WOOD) {
            if (this.itemDurability <= 32) {
                cbireturn.setReturnValue(123);
            }
            else {
                cbireturn.setReturnValue(42);
            }
        }
        else if (this.miningLevel == MiningLevels.STONE) {
            cbireturn.setReturnValue(148);
        }
        else if (this.miningLevel == MiningLevels.IRON) {
            cbireturn.setReturnValue(2*this.itemDurability);
        }
        else if (this.miningLevel >= MiningLevels.DIAMOND) {
            cbireturn.setReturnValue(MathHelper.ceil(1.25f*this.itemDurability));
        }
    }

    @Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
    private void injectedGetMiningSpeedMultiplier(CallbackInfoReturnable<Float> cbireturn) {
        if (this.miningLevel == MiningLevels.WOOD) {
            if (this.miningSpeed >= 12.0f) {
                cbireturn.setReturnValue(this.miningSpeed + 1.0f);
            }
        }
        else if (this.miningLevel == MiningLevels.IRON) {
            cbireturn.setReturnValue(this.miningSpeed + 2.0f);
        }
        else if (this.miningLevel >= MiningLevels.DIAMOND) {
            cbireturn.setReturnValue(this.miningSpeed + 4.0f);
        }
    }


}
