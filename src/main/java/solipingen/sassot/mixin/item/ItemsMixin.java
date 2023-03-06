package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


@Mixin(Items.class)
public abstract class ItemsMixin {

    
    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("TAIL"), cancellable = true)
    private static void injectedRegister(Identifier id, Item item, CallbackInfoReturnable<Item> cbireturn) {
        String idPath = id.getPath();
        if (item instanceof SwordItem) {
            if (idPath.matches("wooden_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.WOOD, 2, -1.8f, new Item.Settings());
                int swordRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, swordRawId, "wooden_sword", newSwordItem));
            }
            else if (idPath.matches("stone_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.STONE, 3, -2.4f, new Item.Settings());
                int swordRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, swordRawId, "stone_sword", newSwordItem));
            }
            else if (idPath.matches("golden_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.GOLD, 4, -2.3f, new Item.Settings());
                int swordRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, swordRawId, "golden_sword", newSwordItem));
            }
            else if (idPath.matches("iron_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.IRON, 5, -2.2f, new Item.Settings());
                int swordRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, swordRawId, "iron_sword", newSwordItem));
            }
            else if (idPath.matches("diamond_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.DIAMOND, 6, -2.0f, new Item.Settings());
                int swordRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, swordRawId, "diamond_sword", newSwordItem));
            }
            else if (idPath.matches("netherite_sword")) {
                Item newSwordItem = (Item)new SwordItem(ToolMaterials.NETHERITE, 6, -1.9f, new Item.Settings());
                int swordRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, swordRawId, "netherite_sword", newSwordItem));
            }
        }
        else if (item instanceof AxeItem) {
            if  (idPath.matches("wooden_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.WOOD, 3.0f, -2.8f, new Item.Settings());
                int axeRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, axeRawId, "wooden_axe", newAxeItem));
            }
            else if (idPath.matches("stone_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.STONE, 5.0f, -3.2f, new Item.Settings());
                int axeRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, axeRawId, "stone_axe", newAxeItem));
            }
            else if (idPath.matches("golden_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.GOLD, 6.0f, -3.0f, new Item.Settings());
                int axeRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, axeRawId, "golden_axe", newAxeItem));
            }
            else if (idPath.matches("iron_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.IRON, 7.0f, -2.9f, new Item.Settings());
                int axeRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, axeRawId, "iron_axe", newAxeItem));
            }
            else if (idPath.matches("diamond_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.DIAMOND, 8.0f, -2.7f, new Item.Settings());
                int axeRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, axeRawId, "diamond_axe", newAxeItem));
            }
            else if (idPath.matches("netherite_axe")) {
                Item newAxeItem = (Item)new AxeItem(ToolMaterials.NETHERITE, 8.0f, -2.6f, new Item.Settings());
                int axeRawId = Item.getRawId(item);
                cbireturn.setReturnValue(Registry.register(Registries.ITEM, axeRawId, "netherite_axe", newAxeItem));
            }
        }
        else if (item instanceof TridentItem) {
            Item newTridentItem = (Item)new TridentItem(new Item.Settings().maxDamage(1095));
            int tridentRawId = Item.getRawId(item);
            cbireturn.setReturnValue(Registry.register(Registries.ITEM, tridentRawId, "trident", newTridentItem));
        }
    }
    
}
