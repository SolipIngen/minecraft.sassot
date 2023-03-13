package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.AxeItem;
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

    
    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("TAIL"), cancellable = true)
    private static void injectedRegister(Identifier id, Item item, CallbackInfoReturnable<Item> cbireturn) {
        String idPath = id.getPath();
        int rawId = Item.getRawId(item);
        if (item instanceof SwordItem) {
            if (idPath.matches("wooden_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.WOOD, 2, -1.8f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "wooden_sword", newSwordItem));
            }
            else if (idPath.matches("stone_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.STONE, 3, -2.4f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "stone_sword", newSwordItem));
            }
            else if (idPath.matches("golden_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.GOLD, 4, -2.3f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "golden_sword", newSwordItem));
            }
            else if (idPath.matches("iron_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.IRON, 5, -2.2f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "iron_sword", newSwordItem));
            }
            else if (idPath.matches("diamond_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.DIAMOND, 6, -2.0f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "diamond_sword", newSwordItem));
            }
            else if (idPath.matches("netherite_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.NETHERITE, 6, -1.9f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "netherite_sword", newSwordItem));
            }
        }
        else if (item instanceof AxeItem) {
            if  (idPath.matches("wooden_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.WOOD, 3.0f, -2.8f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "wooden_axe", newAxeItem));
            }
            else if (idPath.matches("stone_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.STONE, 5.0f, -3.2f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "stone_axe", newAxeItem));
            }
            else if (idPath.matches("golden_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.GOLD, 6.0f, -3.0f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "golden_axe", newAxeItem));
            }
            else if (idPath.matches("iron_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.IRON, 7.0f, -2.9f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "iron_axe", newAxeItem));
            }
            else if (idPath.matches("diamond_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.DIAMOND, 8.0f, -2.7f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "diamond_axe", newAxeItem));
            }
            else if (idPath.matches("netherite_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.NETHERITE, 8.0f, -2.6f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "netherite_axe", newAxeItem));
            }
        }
        else if (item instanceof TridentItem) {
            Item newTridentItem = (Item)new TridentItem(new Item.Settings().maxDamage(1095));
            cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "trident", newTridentItem));
        }
        else if (item instanceof PickaxeItem) {
            if  (idPath.matches("wooden_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.WOOD, 2, -2.6f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "wooden_pickaxe", newPickaxeItem));
            }
            else if (idPath.matches("stone_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.STONE, 2, -2.8f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "stone_pickaxe", newPickaxeItem));
            }
            else if (idPath.matches("golden_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.GOLD, 2, -2.6f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "golden_pickaxe", newPickaxeItem));
            }
            else if (idPath.matches("iron_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.IRON, 2, -2.5f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "iron_pickaxe", newPickaxeItem));
            }
            else if (idPath.matches("diamond_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.DIAMOND, 2, -2.5f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "diamond_pickaxe", newPickaxeItem));
            }
            else if (idPath.matches("netherite_pickaxe")) {
                Item newPickaxeItem = (Item)new PickaxeItem(ToolMaterials.NETHERITE, 2, -2.5f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "netherite_pickaxe", newPickaxeItem));
            }
        }
        else if (item instanceof ShovelItem) {
            if  (idPath.matches("wooden_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.WOOD, 1.0f, -2.4f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "wooden_shovel", newShovelItem));
            }
            else if (idPath.matches("stone_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.STONE, 1.0f, -2.6f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "stone_shovel", newShovelItem));
            }
            else if (idPath.matches("golden_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.GOLD, 1.0f, -2.4f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "golden_shovel", newShovelItem));
            }
            else if (idPath.matches("iron_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.IRON, 1.0f, -2.3f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "iron_shovel", newShovelItem));
            }
            else if (idPath.matches("diamond_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.DIAMOND, 1.0f, -2.3f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "diamond_shovel", newShovelItem));
            }
            else if (idPath.matches("netherite_shovel")) {
                Item newShovelItem = (Item)new ShovelItem(ToolMaterials.NETHERITE, 1.0f, -2.3f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "netherite_shovel", newShovelItem));
            }
        }
        else if (item instanceof HoeItem) {
            if  (idPath.matches("wooden_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.WOOD, 2, -2.2f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "wooden_hoe", newHoeItem));
            }
            else if (idPath.matches("stone_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.STONE, 2, -2.4f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "stone_hoe", newHoeItem));
            }
            else if (idPath.matches("golden_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.GOLD, 2, -2.3f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "golden_hoe", newHoeItem));
            }
            else if (idPath.matches("iron_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.IRON, 2, -2.3f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "iron_hoe", newHoeItem));
            }
            else if (idPath.matches("diamond_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.DIAMOND, 2, -2.3f, new Item.Settings());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "diamond_hoe", newHoeItem));
            }
            else if (idPath.matches("netherite_hoe")) {
                Item newHoeItem = (Item)HoeItemInvoker.invokeHoeItem(ToolMaterials.NETHERITE, 2, -2.3f, new Item.Settings().fireproof());
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, rawId, "netherite_hoe", newHoeItem));
            }
        }
    }
    
}
