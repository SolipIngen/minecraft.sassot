package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends MiningToolItem {


    public ShovelItemMixin(ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        super(material, effectiveBlocks, settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), index = 2)
    private static Item.Settings modifiedInit(Item.Settings settings, ToolMaterial toolMaterial, Item.Settings settings2) {
        if (toolMaterial == ToolMaterials.WOOD) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.WOOD, 1.0f, -2.3f));
        }
        else if (toolMaterial == ToolMaterials.STONE) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.STONE, 1.0f, -2.7f));
        }
        else if (toolMaterial == ToolMaterials.GOLD) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.GOLD, 1.0f, -2.5f));
        }
        else if (toolMaterial == ToolMaterials.IRON) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.IRON, 1.0f, -2.4f));
        }
        else if (toolMaterial == ToolMaterials.DIAMOND) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.DIAMOND, 1.0f, -2.4f));
        }
        else if (toolMaterial == ToolMaterials.NETHERITE) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.NETHERITE, 1.0f, -2.4f));
        }
        return settings;
    }


}
