package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends MiningToolItem {


    public ShovelItemMixin(ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        super(material, effectiveBlocks, settings);
    }

    @ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/MiningToolItem;<init>(Lnet/minecraft/item/ToolMaterial;Lnet/minecraft/registry/tag/TagKey;Lnet/minecraft/item/Item$Settings;)V"))
    private static void modifiedInit(Args args) {
        ToolMaterial material = args.get(0);
        Item.Settings settings = args.get(2);
        if (material == ToolMaterials.WOOD) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.WOOD, 1.0f, -2.3f));
        }
        else if (material == ToolMaterials.STONE) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.STONE, 1.0f, -2.7f));
        }
        else if (material == ToolMaterials.GOLD) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.GOLD, 1.0f, -2.5f));
        }
        else if (material == ToolMaterials.IRON) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.IRON, 1.0f, -2.4f));
        }
        else if (material == ToolMaterials.DIAMOND) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.DIAMOND, 1.0f, -2.4f));
        }
        else if (material == ToolMaterials.NETHERITE) {
            settings.attributeModifiers(ShovelItem.createAttributeModifiers(ToolMaterials.NETHERITE, 1.0f, -2.4f));
        }
        args.set(2, settings);
    }


}
