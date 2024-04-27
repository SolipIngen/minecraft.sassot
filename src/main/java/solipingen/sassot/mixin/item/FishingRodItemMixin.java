package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import solipingen.sassot.util.interfaces.mixin.entity.projectile.FishingBobberEntityInterface;


@Mixin(FishingRodItem.class)
public abstract class FishingRodItemMixin extends Item {


    public FishingRodItemMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean redirectedSpawnFishingBobberEntity(World world, Entity entity, World world2, PlayerEntity user, Hand hand) {
        if (entity instanceof FishingBobberEntity) {
            ((FishingBobberEntityInterface)entity).setFishingRodStack(user.getStackInHand(hand));
        }
        return world.spawnEntity(entity);
    }

    @ModifyConstant(method = "getEnchantability", constant = @Constant(intValue = 1))
    private int modifiedEnchantability(int originalInt) {
        return 15;
    }

    
}
