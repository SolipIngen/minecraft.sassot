package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


@Mixin(PickaxeItem.class)
public abstract class PickaxeItemMixin extends MiningToolItem {


    public PickaxeItemMixin(ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        super(material, effectiveBlocks, settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), index = 2)
    private static Item.Settings modifiedInit(Item.Settings settings, ToolMaterial toolMaterial, Item.Settings settings2) {
        if (toolMaterial == ToolMaterials.WOOD) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.WOOD, 2.0f, -2.4f));
        }
        else if (toolMaterial == ToolMaterials.STONE) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.STONE, 2.0f, -2.8f));
        }
        else if (toolMaterial == ToolMaterials.GOLD) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.GOLD, 2.0f, -2.6f));
        }
        else if (toolMaterial == ToolMaterials.IRON) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.IRON, 2.0f, -2.5f));
        }
        else if (toolMaterial == ToolMaterials.DIAMOND) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 2.0f, -2.5f));
        }
        else if (toolMaterial == ToolMaterials.NETHERITE) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.NETHERITE, 2.0f, -2.5f));
        }
        return settings;
    }


}
