package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends MiningToolItem {
    @Unique private static final float ATTACK_DAMAGE = 5.0f;


    public AxeItemMixin(ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        super(material, effectiveBlocks, settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), index = 2)
    private static Item.Settings modifiedInit(Item.Settings settings, ToolMaterial toolMaterial, Item.Settings settings2) {
        if (toolMaterial == ToolMaterials.WOOD) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.WOOD, ATTACK_DAMAGE, -2.5f));
        }
        else if (toolMaterial == ToolMaterials.STONE) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.STONE, ATTACK_DAMAGE, -3.2f));
        }
        else if (toolMaterial == ToolMaterials.GOLD) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.GOLD, ATTACK_DAMAGE, -3.0f));
        }
        else if (toolMaterial == ToolMaterials.IRON) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.IRON, ATTACK_DAMAGE, -2.9f));
        }
        else if (toolMaterial == ToolMaterials.DIAMOND) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, ATTACK_DAMAGE, -2.7f));
        }
        else if (toolMaterial == ToolMaterials.NETHERITE) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.NETHERITE, ATTACK_DAMAGE, -2.6f));
        }
        return settings;
    }


}
