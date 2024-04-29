package solipingen.sassot.mixin.entity.projectile;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solipingen.sassot.item.ModFishingRodItem;
import solipingen.sassot.util.interfaces.mixin.entity.player.PlayerEntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.projectile.FishingBobberEntityInterface;


@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity implements FishingBobberEntityInterface {
    @Shadow @Nullable private Entity hookedEntity;
    @Shadow @Final private int lureLevel;
    @Nullable private ItemStack fishingRodStack;


    public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void redirectedInitSpeed(FishingBobberEntity bobber, Vec3d originalVec3d, PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel) {
        this.fishingRodStack = ((PlayerEntityInterface)thrower).getFishingRodStack();
        if (this.fishingRodStack != null && this.fishingRodStack.getItem() instanceof ModFishingRodItem) {
            int i = ((ModFishingRodItem)this.fishingRodStack.getItem()).getMaterial().getMiningLevel();
            originalVec3d.multiply(1.0 + 0.4*i);
        }
        bobber.setVelocity(originalVec3d.add(thrower.getVelocity()));
    }

    @ModifyConstant(method = "tick", constant = @Constant(doubleValue = 0.92))
    private double modifiedInitDragFactor(double originald) {
        if (this.fishingRodStack != null && this.fishingRodStack.getItem() instanceof ModFishingRodItem) {
            int i = ((ModFishingRodItem)this.fishingRodStack.getItem()).getMaterial().getMiningLevel();
            return originald + 0.008*i;
        }
        return originald;
    }

    @Redirect(method = "removeIfInvalid", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedIsOfFishingRod(ItemStack itemStack, Item originalItem) {
        return itemStack == this.fishingRodStack;
    }

    @ModifyConstant(method = "tickFishingLogic", constant = @Constant(intValue = 20))
    private int modifiedLureFactor(int originalInt) {
        if (this.fishingRodStack != null && this.fishingRodStack.getItem() instanceof ModFishingRodItem) {
            ToolMaterial material = ((ModFishingRodItem)this.fishingRodStack.getItem()).getMaterial();
            if (material == ToolMaterials.DIAMOND) {
                return originalInt + material.getMiningLevel() + 3;
            }
            return originalInt + material.getMiningLevel();
        }
        return originalInt;
    }

    @Redirect(method = "getPositionType(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/projectile/FishingBobberEntity$PositionType;", 
        at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean redirectedFluidIsIn(FluidState fluidState, TagKey<Fluid> originalTag) {
        if (this.fishingRodStack != null && this.fishingRodStack.getItem() instanceof ModFishingRodItem && ((ModFishingRodItem)this.fishingRodStack.getItem()).getMaterial() == ToolMaterials.NETHERITE) {
            return fluidState.isIn(originalTag) || fluidState.isIn(FluidTags.LAVA);
        }
        return fluidState.isIn(originalTag);
    }
    
    @ModifyConstant(method = "use", constant = @Constant(intValue = 3))
    private int modifiedItemEntityPullDurability(int originalInt) {
        return 1;
    }

    @ModifyConstant(method = "use", constant = @Constant(intValue = 5))
    private int modifiedEntityPullDurability(int originalInt) {
        if (this.hookedEntity != null) {
            double pullResistanceFactor = this.hookedEntity instanceof LivingEntity ? 1.0 - ((LivingEntity)this.hookedEntity).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) : 1.0;
            return MathHelper.ceil(1.0/pullResistanceFactor);
        }
        return 0;
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getLuck()F"))
    private float redirectedPlayerLuck(PlayerEntity player, ItemStack usedStack) {
        if (usedStack != null && usedStack.getItem() instanceof ModFishingRodItem) {
            ToolMaterial material = ((ModFishingRodItem)usedStack.getItem()).getMaterial();
            if (material == ToolMaterials.GOLD) {
                return player.getLuck() + (EnchantmentHelper.getLuckOfTheSea(usedStack) + 1)*0.7f;
            }
            return player.getLuck() + (EnchantmentHelper.getLuckOfTheSea(usedStack) + 1)*0.3f;
        }
        return player.getLuck();
    }

    @ModifyConstant(method = "use", constant = @Constant(doubleValue = 0.1))
    private double modifiedHookedItemPullSpeed(double originald) {
        if (this.fishingRodStack != null && this.fishingRodStack.getItem() instanceof ModFishingRodItem) {
            ToolMaterial material = ((ModFishingRodItem)this.fishingRodStack.getItem()).getMaterial();
            return (material.getMiningLevel()/6.0 + 1.0)*0.15;
        }
        return 0.15;
    }

    @ModifyConstant(method = "use", constant = @Constant(intValue = 2))
    private int modifiedGroundPullDurability(int originalInt) {
        return 0;
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;discard()V"), cancellable = true)
    private void injectedUse(ItemStack usedItem, CallbackInfoReturnable<Integer> cbireturn) {
        if (this.getOwner() instanceof PlayerEntity) {
            ((PlayerEntityInterface)this.getOwner()).setFishingRodStack(null);
        }
    }

    @ModifyConstant(method = "pullHookedEntity", constant = @Constant(doubleValue = 0.1))
    private double modifiedPullSpeedFactor(double originald, Entity entity) {
        double pullResistanceFactor = entity instanceof LivingEntity ? 1.0 - ((LivingEntity)entity).getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) : 1.0;
        if (this.fishingRodStack != null && this.fishingRodStack.getItem() instanceof ModFishingRodItem) {
            ToolMaterial material = ((ModFishingRodItem)this.fishingRodStack.getItem()).getMaterial();
            if (entity instanceof ItemEntity) {
                return (material.getMiningLevel()/6.0 + 1.0)*0.15;
            }
            return pullResistanceFactor*(0.125*material.getMiningLevel() + 1.0)*0.15;
        }
        return pullResistanceFactor*0.15;
    }

    @Override
    @Nullable
    public ItemStack getFishingRodStack() {
        return this.fishingRodStack;
    }

    @Override
    public void setFishingRodStack(ItemStack itemStack) {
        this.fishingRodStack = itemStack;
    }



    


}
