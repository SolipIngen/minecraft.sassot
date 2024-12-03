package solipingen.sassot.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class ModOnAStickItem<T extends Entity> extends Item {
    private final EntityType<T> target;
    private final int damagePerUse;
    private final ToolMaterial material;


    public ModOnAStickItem(Item.Settings settings, EntityType<T> target, ToolMaterial material, int damagePerUse) {
        super(settings);
        this.target = target;
        this.material = material;
        this.damagePerUse = damagePerUse;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient) {
            return TypedActionResult.pass(itemStack);
        }
        Entity entity = user.getControllingVehicle();
        if (user.hasVehicle() && entity instanceof ItemSteerable itemSteerable) {
            if (entity.getType() == this.target && itemSteerable.consumeOnAStickItem()) {
                itemStack.damage(this.damagePerUse, user, LivingEntity.getSlotForHand(hand));
                if (itemStack.isEmpty()) {
                    Item item = getFishingRodItem();
                    ItemStack itemStack2 = itemStack.copyComponentsToNewStack(item, 1);
                    return TypedActionResult.success(itemStack2);
                }
                return TypedActionResult.success(itemStack);
            }
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.pass(itemStack);
    }

    private Item getFishingRodItem() {
        Item item = Items.FISHING_ROD;
        if (this.material == ModToolMaterials.COPPER) {
            item = ModItems.COPPER_FUSED_FISHING_ROD;
        }
        else if (this.material == ToolMaterials.IRON) {
            item = ModItems.IRON_FUSED_FISHING_ROD;
        }
        else if (this.material == ToolMaterials.GOLD) {
            item = ModItems.GOLD_FUSED_FISHING_ROD;
        }
        else if (this.material == ToolMaterials.DIAMOND) {
            item = ModItems.DIAMOND_FUSED_FISHING_ROD;
        }
        else if (this.material == ToolMaterials.NETHERITE) {
            item = ModItems.NETHERITE_FUSED_FISHING_ROD;
        }
        return item;
    }
}
