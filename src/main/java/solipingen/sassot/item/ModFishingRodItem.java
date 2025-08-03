package solipingen.sassot.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import solipingen.sassot.util.interfaces.mixin.entity.projectile.FishingBobberEntityInterface;


public class ModFishingRodItem extends FishingRodItem {
    private final ToolMaterial material;


    public ModFishingRodItem(Settings settings, ToolMaterial material) {
        super(settings.maxDamage(64 + MathHelper.floor(2.0f/3.0f*material.durability()))
                .repairable(material.repairItems())
                .enchantable(material.enchantmentValue()));
        this.material = material;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.fishHook != null) {
            if (!world.isClient) {
                int i = user.fishHook.use(itemStack);
                itemStack.damage(i, user, LivingEntity.getSlotForHand(hand));
            }
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } 
        else {
            if (world instanceof ServerWorld serverWorld) {
                int i = (int)(EnchantmentHelper.getFishingTimeReduction(serverWorld, itemStack, user)*20.0f);
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof ModFishingRodItem modFishingRodItem) {
                    ToolMaterial material = this.getMaterial();
                    if (material == ModToolMaterials.COPPER || material == ToolMaterial.GOLD) {
                        i += 7;
                    }
                    else if (material == ToolMaterial.IRON || material == ToolMaterial.NETHERITE) {
                        i += 4;
                    }
                    else if (material == ToolMaterial.DIAMOND) {
                        i += 12;
                    }
                }
                int j = EnchantmentHelper.getFishingLuckBonus(serverWorld, itemStack, user);
                FishingBobberEntity fishingBobberEntity = new FishingBobberEntity(user, world, j, i);
                ((FishingBobberEntityInterface)fishingBobberEntity).setFishingRodStack(itemStack);
                serverWorld.spawnEntity(fishingBobberEntity);
            }
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
        }
        return ActionResult.SUCCESS;
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }



    
}
