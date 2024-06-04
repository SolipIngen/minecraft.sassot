package solipingen.sassot.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import solipingen.sassot.util.interfaces.mixin.entity.projectile.FishingBobberEntityInterface;


public class ModFishingRodItem extends FishingRodItem {
    private final ToolMaterial material;


    public ModFishingRodItem(Settings settings, ToolMaterial material) {
        super(settings.maxDamage(64 + MathHelper.floor(2.0f/3.0f*material.getDurability())));
        this.material = material;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
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
            int i = EnchantmentHelper.getLure(itemStack);
            int j = EnchantmentHelper.getLuckOfTheSea(itemStack);
            FishingBobberEntity fishingBobberEntity = new FishingBobberEntity(user, world, j, i);
            ((FishingBobberEntityInterface)fishingBobberEntity).setFishingRodStack(itemStack);
            if (!world.isClient) {
                world.spawnEntity(fishingBobberEntity);
            }
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }
    
    @Override
    public int getEnchantability() {
        return this.material.getEnchantability();
    }



    
}
