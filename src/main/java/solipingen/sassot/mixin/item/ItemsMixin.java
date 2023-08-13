package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Blocks;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.sassot.mixin.item.accessors.HoeItemInvoker;


@Mixin(Items.class)
public abstract class ItemsMixin {

    
    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("RETURN"), cancellable = true)
    private static void injectedRegister(Identifier id, Item item, CallbackInfoReturnable<Item> cbireturn) {
        String name = id.getPath();
        int rawId = Item.getRawId(item);
        if (item instanceof SwordItem) {
            if (name.matches("wooden_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.WOOD, 3, -1.8f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newSwordItem));
            }
            else if (name.matches("stone_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.STONE, 3, -2.4f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newSwordItem));
            }
            else if (name.matches("golden_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.GOLD, 5, -2.3f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newSwordItem));
            }
            else if (name.matches("iron_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.IRON, 5, -2.2f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newSwordItem));
            }
            else if (name.matches("diamond_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.DIAMOND, 6, -2.0f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newSwordItem));
            }
            else if (name.matches("netherite_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.NETHERITE, 6, -1.9f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newSwordItem));
            }
        }
        else if (item instanceof AxeItem) {
            if  (name.matches("wooden_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.WOOD, 5.0f, -2.5f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newAxeItem));
            }
            else if (name.matches("stone_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.STONE, 5.0f, -3.2f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newAxeItem));
            }
            else if (name.matches("golden_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.GOLD, 7.0f, -3.0f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newAxeItem));
            }
            else if (name.matches("iron_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.IRON, 7.0f, -2.9f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newAxeItem));
            }
            else if (name.matches("diamond_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.DIAMOND, 8.0f, -2.7f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newAxeItem));
            }
            else if (name.matches("netherite_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.NETHERITE, 8.0f, -2.6f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newAxeItem));
            }
        }
        else if (item instanceof PickaxeItem) {
            if  (name.matches("wooden_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.WOOD, 2, -2.4f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newPickaxeItem));
            }
            else if (name.matches("stone_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.STONE, 2, -2.8f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newPickaxeItem));
            }
            else if (name.matches("golden_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.GOLD, 2, -2.6f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newPickaxeItem));
            }
            else if (name.matches("iron_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.IRON, 2, -2.5f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newPickaxeItem));
            }
            else if (name.matches("diamond_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.DIAMOND, 2, -2.5f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newPickaxeItem));
            }
            else if (name.matches("netherite_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.NETHERITE, 2, -2.5f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newPickaxeItem));
            }
        }
        else if (item instanceof ShovelItem) {
            if  (name.matches("wooden_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.WOOD, 1.0f, -2.3f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newShovelItem));
            }
            else if (name.matches("stone_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.STONE, 1.0f, -2.7f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newShovelItem));
            }
            else if (name.matches("golden_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.GOLD, 1.0f, -2.5f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newShovelItem));
            }
            else if (name.matches("iron_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.IRON, 1.0f, -2.4f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newShovelItem));
            }
            else if (name.matches("diamond_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.DIAMOND, 1.0f, -2.4f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newShovelItem));
            }
            else if (name.matches("netherite_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.NETHERITE, 1.0f, -2.4f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newShovelItem));
            }
        }
        else if (item instanceof HoeItem) {
            if  (name.matches("wooden_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.WOOD, 2, -2.1f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newHoeItem));
            }
            else if (name.matches("stone_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.STONE, 2, -2.4f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newHoeItem));
            }
            else if (name.matches("golden_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.GOLD, 2, -2.3f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newHoeItem));
            }
            else if (name.matches("iron_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.IRON, 2, -2.2f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newHoeItem));
            }
            else if (name.matches("diamond_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.DIAMOND, 2, -2.2f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newHoeItem));
            }
            else if (name.matches("netherite_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.NETHERITE, 2, -2.2f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newHoeItem));
            }
        }
        else if (item instanceof TridentItem) {
            Item newTridentItem = (Item)new TridentItem(new Item.Settings().maxDamage(1095));
            cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newTridentItem));
        }
        else if (name.matches("blaze_rod")) {
            Item newBlazeRodItem = new Item(new Item.Settings().fireproof());
            cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newBlazeRodItem));
        }
        else if (name.matches("end_rod")) {
            Item newEndRodItem = new BlockItem(Blocks.END_ROD, new Item.Settings().fireproof());
            cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, name, newEndRodItem));
        }
    }

    
}
