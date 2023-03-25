package solipingen.sassot.mixin.entity.mob;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.entity.projectile.spear.FlintSpearEntity;
import solipingen.sassot.entity.projectile.spear.GoldenSpearEntity;
import solipingen.sassot.entity.projectile.spear.SpearEntity;
import solipingen.sassot.entity.projectile.spear.WoodenSpearEntity;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.sound.ModSoundEvents;


@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends AbstractPiglinEntity implements SpearThrowingMob {


    public PiglinEntityMixin(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "makeInitialWeapon", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;GOLDEN_SWORD:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC))
    private Item redirectedMakeInitialWeapon() {
        if (this.random.nextFloat() < 0.33f) {
            float spearRandomf = this.random.nextFloat()*this.world.getDifficulty().getId() + 0.1f*this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
            if (spearRandomf < 0.15f) {
                return ModItems.WOODEN_SPEAR;
            }
            else if (spearRandomf >= 0.15f && spearRandomf < 0.25f) {
                return ModItems.STONE_SPEAR;
            }
            else if (spearRandomf >= 0.25f && spearRandomf < 0.5f) {
                return ModItems.FLINT_SPEAR;
            }
            return ModItems.GOLDEN_SPEAR;
        }
        else {
            float swordRandomf = this.random.nextFloat()*this.world.getDifficulty().getId() + 0.1f*this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
            if (swordRandomf < 0.15f) {
                return Items.WOODEN_SWORD;
            }
            else if (swordRandomf >= 0.15f && swordRandomf < 0.5f) {
                return Items.STONE_SWORD;
            }
            return Items.GOLDEN_SWORD;
        }
    }

    @Inject(method = "initialize", at = @At("RETURN"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cbireturn) {
        if (spawnReason == SpawnReason.STRUCTURE) {
            if (!(this.getMainHandStack().getItem() instanceof RangedWeaponItem)) {
                if (this.random.nextFloat() < 0.33f) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLDEN_SPEAR));
                }
                else {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                }
            }
            this.updateEnchantments(world.getRandom(), difficulty);
        }
    }

    @Redirect(method = "getActivity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PiglinEntity;isHoldingTool()Z"))
    private boolean redirectedIsHoldingTool(PiglinEntity piglin) {
        return this.isHoldingTool() || this.getMainHandStack().getItem() instanceof SpearItem || this.getOffHandStack().getItem() instanceof SpearItem || this.isHolding(ModItems.BLAZEARM);
    }

    @Override
    public void spearAttack(LivingEntity target, float pullProgress) {
        ItemStack spearStack = this.getMainHandStack();
        SpearEntity spearEntity = new WoodenSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.WOODEN_SPEAR));
        int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        float speed = 2.0f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        if (spearStack.isOf(ModItems.FLINT_SPEAR)) {
            spearEntity = new FlintSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.FLINT_SPEAR));
            speed = 1.8f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.GOLDEN_SPEAR)) {
            spearEntity = new GoldenSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.GOLDEN_SPEAR));
            speed = 2.0f + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - spearEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        spearEntity.setVelocity(d, e + g * 0.15, f, speed, 14 - this.world.getDifficulty().getId() * 4);
        this.playSound(ModSoundEvents.PIGLIN_SPEAR_THROW, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntity(spearEntity);
    }

    @Override
    public double squaredAttackRange(LivingEntity target) {
        Item mainHandItem = this.getMainHandStack().getItem();
        if (mainHandItem instanceof SwordItem) {
            return MathHelper.square(this.getWidth() * 2.0f + 1.0f) + target.getWidth();
        }
        return super.squaredAttackRange(target);
    }


    
}
