package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends Item {
    @Unique private static final float ATTACK_DAMAGE = 5.0f;


    public AxeItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static float modifiedAttackDamage(float attackDamage) {
        return ATTACK_DAMAGE;
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 1)
    private static float modifiedAttackDamage(float attackSpeed, ToolMaterial material, float attackDamage, float attackSpeed2, Item.Settings settings) {
        if (material == ToolMaterial.WOOD) {
            return -2.5f;
        }
        else if (material == ToolMaterial.STONE) {
            return -3.2f;
        }
        else if (material == ToolMaterial.GOLD) {
            return -3.0f;
        }
        else if (material == ToolMaterial.IRON) {
            return -2.9f;
        }
        else if (material == ToolMaterial.DIAMOND) {
            return -2.7f;
        }
        else if (material == ToolMaterial.NETHERITE) {
            return -2.6f;
        }
        else {
            return attackSpeed;
        }
    }


}
