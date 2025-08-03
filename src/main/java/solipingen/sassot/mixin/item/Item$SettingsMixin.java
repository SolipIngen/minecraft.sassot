package solipingen.sassot.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(Item.Settings.class)
public abstract class Item$SettingsMixin {

    @ModifyVariable(method = "pickaxe", at = @At("HEAD"), ordinal = 0)
    private float modifiedPickaxeAttackDamage(float attackDamage, ToolMaterial material, float attackDamage2, float attackSpeed) {
        return 2.0f;
    }

    @ModifyVariable(method = "pickaxe", at = @At("HEAD"), ordinal = 1)
    private float modifiedPickaxeAttackSpeed(float attackSpeed, ToolMaterial material, float attackDamage, float attackSpeed2) {
        if (material == ToolMaterial.WOOD) {
            return -2.4f;
        }
        else if (material == ToolMaterial.STONE) {
            return -2.8f;
        }
        else if (material == ToolMaterial.GOLD) {
            return -2.6f;
        }
        else if (material == ToolMaterial.IRON) {
            return -2.5f;
        }
        else if (material == ToolMaterial.DIAMOND) {
            return -2.5f;
        }
        else if (material == ToolMaterial.NETHERITE) {
            return -2.5f;
        }
        else {
            return attackSpeed;
        }
    }


}
