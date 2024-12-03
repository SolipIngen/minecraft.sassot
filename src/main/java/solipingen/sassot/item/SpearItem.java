package solipingen.sassot.item;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.advancement.ModCriteria;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.entity.projectile.spear.*;
import solipingen.sassot.registry.tag.ModEntityTypeTags;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


public class SpearItem extends ToolItem implements ProjectileItem {
    public static final Identifier ATTACK_RANGE_MODIFIER_ID = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "base_attack_range");
    public static final Identifier RANGE_MODIFIER_ID = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "base_range");


    public SpearItem(ToolMaterial material, Item.Settings settings) {
        super(material, settings.maxDamage(material.getDurability()).component(DataComponentTypes.TOOL, SpearItem.createToolComponent()));
    }

    public static AttributeModifiersComponent createAttributeModifiers(ToolMaterial material, float baseAttackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, baseAttackDamage + material.getAttackDamage(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE,
                        new EntityAttributeModifier(RANGE_MODIFIER_ID, 1.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                        new EntityAttributeModifier(ATTACK_RANGE_MODIFIER_ID, 1.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    private static ToolComponent createToolComponent() {
        return new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(List.of(Blocks.COBWEB), 20.0f),
                ToolComponent.Rule.of(BlockTags.SWORD_EFFICIENT, 2.0f)), 1.0f, 1);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity) || !(stack.getItem() instanceof SpearItem)) {
            return;
        }
        int usedTicks = this.getMaxUseTime(stack, playerEntity) - remainingUseTicks;
        if (usedTicks < 10 || SpearItem.isAboutToBreak(stack)) {
            return;
        }
        float j = EnchantmentHelper.getTridentSpinAttackStrength(stack, playerEntity);
        RegistryEntry<SoundEvent> soundEntry = EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.TRIDENT_SOUND).orElse(ModSoundEvents.SPEAR_THROW);
        if (!world.isClient()) {
            stack.damage(1, playerEntity, LivingEntity.getSlotForHand(user.getActiveHand()));
            if (j <= 0.0f) {
                int strengthLevel = user.hasStatusEffect(StatusEffects.STRENGTH) ? user.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
                int weaknessLevel = user.hasStatusEffect(StatusEffects.WEAKNESS) ? user.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
                Vec3d shootPos = new Vec3d(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());
                ProjectileEntity entity = this.createEntity(world, shootPos, stack, Direction.getFacing(playerEntity.getRotationVecClient()));
                entity.setOwner(playerEntity);
                if (!(entity instanceof SpearEntity spearEntity)) return;
                spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, spearEntity.getLaunchSpeed() + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                if (playerEntity.getAbilities().creativeMode) {
                    spearEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
                world.spawnEntity(spearEntity);
                world.playSoundFromEntity(null, spearEntity, soundEntry.value(), SoundCategory.PLAYERS, 1.0f, 1.0f + 0.1f*user.getRandom().nextFloat());
                if (!playerEntity.getAbilities().creativeMode) {
                    playerEntity.getInventory().removeOne(stack);
                }
            }
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        if (j > 0.0f && !(playerEntity.isSubmergedInWater() || playerEntity.isSubmergedIn(FluidTags.LAVA))) {
            world.playSoundFromEntity(null, playerEntity, soundEntry.value(), SoundCategory.PLAYERS, 1.0f, 1.0f);
            Iterable<BlockPos> blockPosList = BlockPos.iterateOutwards(playerEntity.getBlockPos(), MathHelper.ceil(playerEntity.getWidth()), MathHelper.ceil(playerEntity.getHeight()), MathHelper.ceil(playerEntity.getWidth()));
            for (BlockPos currentBlockPos : blockPosList) {
                if (world.getBlockState(currentBlockPos).isOf(Blocks.POWDER_SNOW)) {
                    world.breakBlock(currentBlockPos, false);
                }
            }
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)playerEntity;
            iLivingEntity.setIsUsingWhirlwind(true);
            playerEntity.setNoDrag(true);
            float attackDamage = (float)playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            float f = playerEntity.getYaw();
            float g = playerEntity.getPitch();
            float h = -MathHelper.sin(f * ((float)Math.PI / 180)) * MathHelper.cos(g * ((float)Math.PI / 180));
            float k = -MathHelper.sin(g * ((float)Math.PI / 180));
            float l = MathHelper.cos(f * ((float)Math.PI / 180)) * MathHelper.cos(g * ((float)Math.PI / 180));
            float m = MathHelper.sqrt(h * h + k * k + l * l);
            playerEntity.addVelocity(h *= j / m, k *= j / m, l *= j / m);
            playerEntity.useRiptide(20, attackDamage, stack);
            if (playerEntity.isOnGround()) {
                playerEntity.move(MovementType.SELF, new Vec3d(0.0, 1.1999999284744263, 0.0));
            }
            if (!(world instanceof ServerWorld serverWorld)) return;
            RegistryEntryLookup<Enchantment> enchantmentLookup = playerEntity.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int level = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.WHIRLWIND), stack);
            Box whirlwindBox = playerEntity.getBoundingBox().expand(4.0*level, 2.0*level, 4.0*level);
            List<Entity> entityList = world.getOtherEntities(playerEntity, whirlwindBox);
            for (Entity otherEntity : entityList) {
                if (playerEntity.squaredDistanceTo(otherEntity) > (4.0*level)*(4.0*level)) continue;
                boolean reachBl = false;
                for (int i = 0; i < MathHelper.ceil(otherEntity.getHeight() + 1.0f); ++i) {
                    Vec3d reachVec3d = new Vec3d(otherEntity.getX(), otherEntity.getBodyY(1.0/MathHelper.ceil(otherEntity.getHeight())*i), otherEntity.getZ());
                    BlockHitResult hitResult = world.raycast(new RaycastContext(playerEntity.getPos(), reachVec3d, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, playerEntity));
                    if (((HitResult)hitResult).getType() != HitResult.Type.MISS) continue;
                    reachBl = true;
                    break;
                }
                if (!reachBl) continue;
                double xDiff = playerEntity.getX() - otherEntity.getX();
                double yDiff = playerEntity.getY() - otherEntity.getY();
                double zDiff = playerEntity.getZ() - otherEntity.getZ();
                double distanceModifier = 1.0/(1.0 + Math.sqrt(xDiff*xDiff + yDiff*yDiff + zDiff*zDiff));
                if (otherEntity instanceof LivingEntity livingOtherEntity) {
                    if (otherEntity.getType().isIn(ModEntityTypeTags.IMMUNE_TO_WHIRLWIND)) return;
                    float damageAmount = EnchantmentHelper.getDamage(serverWorld, stack, livingOtherEntity, playerEntity.getDamageSources().trident(playerEntity, playerEntity), attackDamage);
                    damageAmount *= 0.25f*level*(float)distanceModifier;
                    damageAmount *= livingOtherEntity.getType().isIn(ModEntityTypeTags.SENSITIVE_TO_WHIRLWIND) || ((EntityInterface)livingOtherEntity).isBeingSnowedOn() ? 2.0f : 1.0f;
                    damageAmount *= playerEntity.getWorld().isThundering() ? 1.5f : 1.0f;
                    if (!(livingOtherEntity.isSubmergedInWater() || livingOtherEntity.isSubmergedIn(FluidTags.LAVA))) {
                        livingOtherEntity.damage(playerEntity.getDamageSources().playerAttack(playerEntity), damageAmount);
                        if (((EntityInterface)livingOtherEntity).isBeingSnowedOn()) {
                            livingOtherEntity.setFrozenTicks(livingOtherEntity.getMinFreezeDamageTicks()*(level + 1));
                        }
                        if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*1.5*level*distanceModifier <= 0.0)) {
                            Vec3d vec3d = livingOtherEntity.getVelocity();
                            Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*1.5*level*distanceModifier);
                            otherEntity.setVelocity(vec3d.x - vec3d2.x, livingOtherEntity.isOnGround() ? Math.min(vec3d.y + (1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*1.5*level*distanceModifier, 0.4) : vec3d.y, vec3d.z - vec3d2.z);
                        }
                    }
                }
                else if (!(otherEntity instanceof LivingEntity) && !(otherEntity.isSubmergedInWater() || otherEntity.isSubmergedIn(FluidTags.LAVA))) {
                    Vec3d vec3d = otherEntity.getVelocity();
                    Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply(1.5*level*distanceModifier);
                    otherEntity.setVelocity(vec3d.x - vec3d2.x, otherEntity.isOnGround() ? Math.min(vec3d.y + 1.5*level*distanceModifier, 0.4) : vec3d.y, vec3d.z - vec3d2.z);
                }
            }
        }
        if (playerEntity instanceof ServerPlayerEntity) {
            ModCriteria.THROW_SPEAR.trigger((ServerPlayerEntity)playerEntity, stack);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        }
        RegistryEntryLookup<Enchantment> enchantmentLookup = user.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        if ((user.isSubmergedInWater() || user.isSubmergedIn(FluidTags.LAVA)) && EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.WHIRLWIND), itemStack) > 0) {
            return TypedActionResult.fail(itemStack);
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        SpearEntity spearEntity = new WoodenSpearEntity(pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        if (stack.isOf(ModItems.BAMBOO_SPEAR)) {
            spearEntity = new BambooSpearEntity(pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        }
        else if (stack.isOf(ModItems.STONE_SPEAR)) {
            spearEntity = new StoneSpearEntity(pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        }
        else if (stack.isOf(ModItems.FLINT_SPEAR)) {
            spearEntity = new FlintSpearEntity(pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        }
        else if (stack.isOf(ModItems.COPPER_SPEAR)) {
            spearEntity = new CopperSpearEntity( pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        }
        else if (stack.isOf(ModItems.GOLDEN_SPEAR)) {
            spearEntity = new GoldenSpearEntity(pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        }
        else if (stack.isOf(ModItems.IRON_SPEAR)) {
            spearEntity = new IronSpearEntity(pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        }
        else if (stack.isOf(ModItems.DIAMOND_SPEAR)) {
            spearEntity = new DiamondSpearEntity(pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        }
        else if (stack.isOf(ModItems.NETHERITE_SPEAR)) {
            spearEntity = new NetheriteSpearEntity(pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        }
        spearEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return spearEntity;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if ((double)state.getHardness(world, pos) != 0.0) {
            stack.damage(0, miner, EquipmentSlot.MAINHAND);
        }
        return true;
    }

    private static boolean isAboutToBreak(ItemStack stack) {
        return stack.getDamage() >= stack.getMaxDamage() - 1;
    }

}

