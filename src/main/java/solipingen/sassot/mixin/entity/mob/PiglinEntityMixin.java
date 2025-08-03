package solipingen.sassot.mixin.entity.mob;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.entity.projectile.spear.*;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.registry.tag.ModItemTags;
import solipingen.sassot.sound.ModSoundEvents;


@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends AbstractPiglinEntity implements SpearThrowingMob {


    public PiglinEntityMixin(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"), cancellable = true)
    private void injectedInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cbireturn) {
        if (this.isAdult()) {
            if (spawnReason == SpawnReason.STRUCTURE) {
                if (!(this.getWeaponStack().getItem() instanceof RangedWeaponItem)) {
                    if (world.getRandom().nextFloat() < 0.33f) {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLDEN_SPEAR));
                    }
                    else {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                    }
                    this.updateEnchantments(world, world.getRandom(), difficulty);
                }
            }
            else {
                if (!(this.getWeaponStack().getItem() instanceof RangedWeaponItem)) {
                    if (this.random.nextFloat() < 0.33f) {
                        float spearRandomf = this.random.nextFloat()*this.getWorld().getDifficulty().getId() + 0.1f*this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
                        if (spearRandomf < 0.15f) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WOODEN_SPEAR));
                        }
                        else if (spearRandomf >= 0.15f && spearRandomf < 0.25f) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
                        }
                        else if (spearRandomf >= 0.25f && spearRandomf < 0.5f) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FLINT_SPEAR));
                        }
                        else {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLDEN_SPEAR));
                        }
                    }
                    else {
                        float swordRandomf = this.random.nextFloat()*this.getWorld().getDifficulty().getId() + 0.1f*this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty();
                        if (swordRandomf < 0.15f) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
                        }
                        else if (swordRandomf >= 0.15f && swordRandomf < 0.5f) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                        }
                        else {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                        }
                    }
                    this.updateEnchantments(world, world.getRandom(), difficulty);
                }
            }
        }
    }

    @Inject(method = "prefersNewEquipment", at = @At("TAIL"), cancellable = true)
    private void injectedPrefersNewEquipment(ItemStack newStack, ItemStack currentStack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cbireturn) {
        if (newStack.getItem() instanceof ShieldItem) {
            cbireturn.setReturnValue(false);
        }
    }

    @Inject(method = "getPreferredWeapons", at = @At("HEAD"), cancellable = true)
    private void injectedGetPreferredWeapons(CallbackInfoReturnable<TagKey<Item>> cbireturn) {
        if (this.isBaby()) {
            cbireturn.setReturnValue(null);
        }
        else {
            if (this.getWeaponStack().getItem() instanceof SpearItem || this.getWeaponStack().isOf(ModItems.BLAZEARM)) {
                cbireturn.setReturnValue(ModItemTags.PIGLIN_PREFERRED_THROWING_WEAPONS);
            }
        }
    }

    @Override
    public ItemStack tryEquip(ServerWorld world, ItemStack stack) {
        if (stack.getItem() instanceof ShieldItem) {
            return ItemStack.EMPTY;
        }
        return super.tryEquip(world, stack);
    }

    @Override
    public void spearAttack(LivingEntity target, float pullProgress) {
        ItemStack spearStack = this.getWeaponStack().copy();
        if (spearStack.getItem() instanceof SpearItem spearItem) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = this.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            if (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.LOYALTY), spearStack) > 0) {
                ItemEnchantmentsComponent.Builder enchantmentsComponentBuilder = new ItemEnchantmentsComponent.Builder(spearStack.getEnchantments());
                enchantmentsComponentBuilder.remove(entry -> entry.matchesKey(Enchantments.LOYALTY));
                spearStack.set(DataComponentTypes.ENCHANTMENTS, enchantmentsComponentBuilder.build());
            }
            Vec3d shootPos = new Vec3d(this.getX(), this.getEyeY() - 0.1, this.getZ());
            ProjectileEntity spearEntity = spearItem.createEntity(this.getWorld(), shootPos, spearStack, Direction.getFacing(this.getRotationVecClient()));
            spearEntity.setOwner(this);
            if (spearEntity instanceof PersistentProjectileEntity) {
                ((PersistentProjectileEntity)spearEntity).pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
            }
            int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
            int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
            float speed = WoodenSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            if (spearStack.isOf(ModItems.FLINT_SPEAR)) {
                speed = FlintSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            }
            else if (spearStack.isOf(ModItems.STONE_SPEAR)) {
                speed = StoneSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            }
            else if (spearStack.isOf(ModItems.GOLDEN_SPEAR)) {
                speed = GoldenSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel;
            }
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333) - spearEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            spearEntity.setVelocity(d, e + g * 0.15, f, speed, 14 - this.getWorld().getDifficulty().getId() * 4);
            this.playSound(ModSoundEvents.PIGLIN_SPEAR_THROW, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
            this.getWorld().spawnEntity(spearEntity);
        }
    }


    
}
