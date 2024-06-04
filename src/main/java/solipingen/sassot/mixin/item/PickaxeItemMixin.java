package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


@Mixin(PickaxeItem.class)
public abstract class PickaxeItemMixin extends MiningToolItem {


    public PickaxeItemMixin(ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        super(material, effectiveBlocks, settings);
    }

    @ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/MiningToolItem;<init>(Lnet/minecraft/item/ToolMaterial;Lnet/minecraft/registry/tag/TagKey;Lnet/minecraft/item/Item$Settings;)V"))
    private static void modifiedInit(Args args) {
        ToolMaterial material = args.get(0);
        Item.Settings settings = args.get(2);
        if (material == ToolMaterials.WOOD) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.WOOD, 2.0f, -2.4f));
        }
        else if (material == ToolMaterials.STONE) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.STONE, 2.0f, -2.8f));
        }
        else if (material == ToolMaterials.GOLD) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.GOLD, 2.0f, -2.6f));
        }
        else if (material == ToolMaterials.IRON) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.IRON, 2.0f, -2.5f));
        }
        else if (material == ToolMaterials.DIAMOND) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 2.0f, -2.5f));
        }
        else if (material == ToolMaterials.NETHERITE) {
            settings.attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.NETHERITE, 2.0f, -2.5f));
        }
        args.set(2, settings);
    }


}
