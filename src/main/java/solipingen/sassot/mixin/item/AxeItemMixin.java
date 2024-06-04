package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends MiningToolItem {


    public AxeItemMixin(ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        super(material, effectiveBlocks, settings);
    }

    @ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/MiningToolItem;<init>(Lnet/minecraft/item/ToolMaterial;Lnet/minecraft/registry/tag/TagKey;Lnet/minecraft/item/Item$Settings;)V"))
    private static void modifiedInit(Args args) {
        ToolMaterial material = args.get(0);
        Item.Settings settings = args.get(2);
        if (material == ToolMaterials.WOOD) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.WOOD, 5.0f, -2.5f));
        }
        else if (material == ToolMaterials.STONE) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.STONE, 5.0f, -3.2f));
        }
        else if (material == ToolMaterials.GOLD) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.GOLD, 7.0f, -3.0f));
        }
        else if (material == ToolMaterials.IRON) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.IRON, 7.0f, -2.9f));
        }
        else if (material == ToolMaterials.DIAMOND) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 8.0f, -2.7f));
        }
        else if (material == ToolMaterials.NETHERITE) {
            settings.attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.NETHERITE, 8.0f, -2.6f));
        }
        args.set(2, settings);
    }


}
