package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


@Mixin(HoeItem.class)
public abstract class HoeItemMixin extends MiningToolItem {


    public HoeItemMixin(ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        super(material, effectiveBlocks, settings);
    }

    @ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/MiningToolItem;<init>(Lnet/minecraft/item/ToolMaterial;Lnet/minecraft/registry/tag/TagKey;Lnet/minecraft/item/Item$Settings;)V"))
    private static void modifiedInit(Args args) {
        ToolMaterial material = args.get(0);
        Item.Settings settings = args.get(2);
        if (material == ToolMaterials.WOOD) {
            settings.attributeModifiers(HoeItem.createAttributeModifiers(ToolMaterials.WOOD,  2.0f, -2.1f));
        }
        else if (material == ToolMaterials.STONE) {
            settings.attributeModifiers(HoeItem.createAttributeModifiers(ToolMaterials.STONE, 2.0f, -2.4f));
        }
        else if (material == ToolMaterials.GOLD) {
            settings.attributeModifiers(HoeItem.createAttributeModifiers(ToolMaterials.GOLD, 2.0f, -2.3f));
        }
        else if (material == ToolMaterials.IRON) {
            settings.attributeModifiers(HoeItem.createAttributeModifiers(ToolMaterials.IRON, 2.0f, -2.2f));
        }
        else if (material == ToolMaterials.DIAMOND) {
            settings.attributeModifiers(HoeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 2.0f, -2.2f));
        }
        else if (material == ToolMaterials.NETHERITE) {
            settings.attributeModifiers(HoeItem.createAttributeModifiers(ToolMaterials.NETHERITE, 2.0f, -2.2f));
        }
        args.set(2, settings);
    }


}
