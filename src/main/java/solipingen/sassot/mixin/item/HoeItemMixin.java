package solipingen.sassot.mixin.item;

import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(HoeItem.class)
public abstract class HoeItemMixin extends Item {


    public HoeItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static float modifiedAttackDamage(float attackDamage, ToolMaterial material, float attackDamage2, float attackSpeed, Item.Settings settings) {
        return 2.0f;
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 1)
    private static float modifiedAttackSpeed(float attackSpeed, ToolMaterial material, float attackDamage, float attackSpeed2, Item.Settings settings) {
        if (material == ToolMaterial.WOOD) {
            return -2.1f;
        }
        else if (material == ToolMaterial.STONE) {
            return -2.4f;
        }
        else if (material == ToolMaterial.GOLD) {
            return -2.3f;
        }
        else if (material == ToolMaterial.IRON) {
            return -2.2f;
        }
        else if (material == ToolMaterial.DIAMOND) {
            return -2.2f;
        }
        else if (material == ToolMaterial.NETHERITE) {
            return -2.2f;
        }
        else {
            return attackSpeed;
        }
    }


}
