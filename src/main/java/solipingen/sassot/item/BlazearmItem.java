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
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ProjectileItem;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import solipingen.sassot.advancement.ModCriteria;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.entity.projectile.BlazearmEntity;
import solipingen.sassot.registry.tag.ModEntityTypeTags;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


public class BlazearmItem extends Item implements ProjectileItem {
    private static final float ATTACK_DAMAGE = 9.0f;
    private static final double ATTACK_SPEED_MODIFIER = -2.4;


    public BlazearmItem(Item.Settings settings) {
        super(settings.component(DataComponentTypes.TOOL, BlazearmItem.createToolComponent()));
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, ATTACK_DAMAGE, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, ATTACK_SPEED_MODIFIER, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE,
                        new EntityAttributeModifier(SpearItem.RANGE_MODIFIER_ID, 1.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                        new EntityAttributeModifier(SpearItem.ATTACK_RANGE_MODIFIER_ID, 1.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    private static ToolComponent createToolComponent() {
        return new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(List.of(Blocks.COBWEB), 25.0f),
                ToolComponent.Rule.of(BlockTags.SWORD_EFFICIENT, 2.5f)), 1.0f, 1);
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
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }
        int usedTicks = this.getMaxUseTime(stack, playerEntity) - remainingUseTicks;
        if (usedTicks < 10) {
            return;
        }
        float j = EnchantmentHelper.getTridentSpinAttackStrength(stack, playerEntity);
        RegistryEntry<SoundEvent> soundEntry = EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.TRIDENT_SOUND).orElse(ModSoundEvents.BLAZEARM_THROW);
        if (!world.isClient()) {
            stack.damage(1, playerEntity, LivingEntity.getSlotForHand(user.getActiveHand()));
            if (j <= 0.0f) {
                int strengthLevel = user.hasStatusEffect(StatusEffects.STRENGTH) ? user.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
                int weaknessLevel = user.hasStatusEffect(StatusEffects.WEAKNESS) ? user.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
                Vec3d shootPos = new Vec3d(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());
                ProjectileEntity entity = this.createEntity(world, shootPos, stack, Direction.getFacing(playerEntity.getRotationVecClient()));
                entity.setOwner(playerEntity);
                if (!(entity instanceof BlazearmEntity blazearmEntity)) return;
                blazearmEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, 2.7f + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                if (playerEntity.getAbilities().creativeMode) {
                    blazearmEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
                world.spawnEntity(blazearmEntity);
                world.playSoundFromEntity(null, blazearmEntity, soundEntry.value(), SoundCategory.PLAYERS, 1.0f, 0.85f + 0.1f*user.getRandom().nextFloat());
                if (!playerEntity.getAbilities().creativeMode) {
                    playerEntity.getInventory().removeOne(stack);
                }
            }
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        BlockState magmaBlockState = Blocks.AIR.getDefaultState();
        Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(playerEntity.getBlockPos(), 1, 1, 1);
        for (BlockPos blockPos : blockPosIterable) {
            if (blockPos.getManhattanDistance(user.getBlockPos()) > 1) continue;
            magmaBlockState = world.getBlockState(blockPos);
            if (magmaBlockState.isOf(Blocks.MAGMA_BLOCK)) break;
        }
        if (j > 0 && (playerEntity.isOnFire() || playerEntity.isInLava() || magmaBlockState.isOf(Blocks.MAGMA_BLOCK) || (playerEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) && !(playerEntity.isWet() || ((EntityInterface)playerEntity).isBeingSnowedOn())))) {
            world.playSoundFromEntity(null, playerEntity, soundEntry.value(), SoundCategory.PLAYERS, 1.0f, 1.0f);
            LivingEntityInterface ILivingEntity = (LivingEntityInterface)playerEntity;
            ILivingEntity.setIsUsingFlare(true);
            playerEntity.setNoDrag(true);
            float f = playerEntity.getYaw();
            float g = playerEntity.getPitch();
            float h = -MathHelper.sin(f * ((float)Math.PI / 180)) * MathHelper.cos(g * ((float)Math.PI / 180));
            float k = -MathHelper.sin(g * ((float)Math.PI / 180));
            float l = MathHelper.cos(f * ((float)Math.PI / 180)) * MathHelper.cos(g * ((float)Math.PI / 180));
            float m = MathHelper.sqrt(h * h + k * k + l * l);
            playerEntity.addVelocity(h *= j / m, k *= j / m, l *= j / m);
            playerEntity.useRiptide(20, ATTACK_DAMAGE, stack);
            if (playerEntity.isOnGround()) {
                playerEntity.move(MovementType.SELF, new Vec3d(0.0, 1.1999999284744263, 0.0));
            }
            if (!(world instanceof ServerWorld serverWorld)) return;
            RegistryEntryLookup<Enchantment> enchantmentLookup = playerEntity.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int level = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), stack);
            Box flareBox = playerEntity.getBoundingBox().expand(6.0*level, 3.0*level, 6.0*level);
            List<Entity> entityList = serverWorld.getOtherEntities(playerEntity, flareBox);
            for (Entity otherEntity : entityList) {
                if (playerEntity.squaredDistanceTo(otherEntity) > (6.0*level)*(6.0*level)) continue;
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
                BlockState otherEntityMagmaBlockState = Blocks.AIR.getDefaultState();
                Iterable<BlockPos> otherEntityBlockPosIterable = BlockPos.iterateOutwards(otherEntity.getBlockPos(), 1, 1, 1);
                for (BlockPos otherEntityBlockPos : otherEntityBlockPosIterable) {
                    if (otherEntityBlockPos.getManhattanDistance(otherEntity.getBlockPos()) > 1) continue;
                    otherEntityMagmaBlockState = world.getBlockState(otherEntityBlockPos);
                    if (otherEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK)) break;
                }
                if (otherEntity instanceof LivingEntity livingOtherEntity && !otherEntity.isWet()) {
                    if (otherEntity.getType().isIn(ModEntityTypeTags.IMMUNE_TO_FLARE)) return;
                    float damageAmount = EnchantmentHelper.getDamage(serverWorld, stack, livingOtherEntity, playerEntity.getDamageSources().playerAttack(playerEntity), (float)playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                        if (stack.isOf(ModItems.BLAZEARM)) {
                            BlockState targetEntityMagmaBlockState = Blocks.AIR.getDefaultState();
                            Iterable<BlockPos> targetEntityBlockPosIterable = BlockPos.iterateOutwards(livingOtherEntity.getBlockPos(), 1, 1, 1);
                            for (BlockPos targetEntityBlockPos : targetEntityBlockPosIterable) {
                                if (targetEntityBlockPos.getManhattanDistance(livingOtherEntity.getBlockPos()) > 1) continue;
                                targetEntityMagmaBlockState = world.getBlockState(targetEntityBlockPos);
                                if (targetEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK)) break;
                            }
                            if (!livingOtherEntity.getType().isIn(ModEntityTypeTags.SENSITIVE_TO_LEANING) || (livingOtherEntity.isOnFire() || livingOtherEntity.isInLava() || targetEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK) || livingOtherEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE))) {
                                damageAmount += 1.0f*EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.LEANING), stack);
                            }
                        }
                    damageAmount *= 0.5f*level*(float)distanceModifier;
                    damageAmount *= (livingOtherEntity.isOnFire() || livingOtherEntity.isInLava() || otherEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK) || livingOtherEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) ? 2.0f : 1.0f;
                    damageAmount *= playerEntity.getWorld().isThundering() ? 1.5f : 1.0f;
                    livingOtherEntity.damage(playerEntity.getDamageSources().playerAttack(playerEntity), damageAmount);
                    if (!livingOtherEntity.isOnFire()) {
                        livingOtherEntity.setOnFireFor((int)Math.ceil(4.0*level*distanceModifier));
                    }
                    if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*1.5*level*distanceModifier <= 0.0)) {
                        Vec3d vec3d = livingOtherEntity.getVelocity();
                        Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*2.0*level*distanceModifier);
                        otherEntity.setVelocity(vec3d.x - vec3d2.x, vec3d.y, vec3d.z - vec3d2.z);
                    }
                }
                else if (!(otherEntity instanceof LivingEntity) && !otherEntity.isWet() && (otherEntity.isFireImmune() || otherEntity.isOnFire() || otherEntity.isInLava() || otherEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK))) {
                    Vec3d vec3d = otherEntity.getVelocity();
                    Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply(2.0*level*distanceModifier);
                    otherEntity.setVelocity(vec3d.x - vec3d2.x, vec3d.y, vec3d.z - vec3d2.z);
                }
            }
            Iterable<BlockPos> blockPosList = BlockPos.iterateOutwards(playerEntity.getBlockPos(), MathHelper.ceil(playerEntity.getWidth()), MathHelper.ceil(playerEntity.getHeight()), MathHelper.ceil(playerEntity.getWidth()));
            for (BlockPos currentBlockPos : blockPosList) {
                if (world.getBlockState(currentBlockPos).isOf(Blocks.POWDER_SNOW)) {
                    world.breakBlock(currentBlockPos, false);
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
        BlockState magmaBlockState = Blocks.AIR.getDefaultState();
        Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(user.getBlockPos(), 1, 1, 1);
        for (BlockPos blockPos : blockPosIterable) {
            if (blockPos.getManhattanDistance(user.getBlockPos()) > 1) continue;
            magmaBlockState = world.getBlockState(blockPos);
            if (magmaBlockState.isOf(Blocks.MAGMA_BLOCK)) break;
        }
        RegistryEntryLookup<Enchantment> enchantmentLookup = user.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        if (!(user.isOnFire() || user.isInLava() || magmaBlockState.isOf(Blocks.MAGMA_BLOCK) || (user.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) && !(user.isWet() || ((EntityInterface)user).isBeingSnowedOn())))
                && EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.FLARE), itemStack) > 0) {
            return TypedActionResult.fail(itemStack);
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        RegistryEntryLookup<Enchantment> enchantmentLookup = attacker.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        int j = EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.FIRE_ASPECT), stack);
        if (!target.isOnFire()) {
            target.setOnFireFor(8*(j + 1));
        }
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        BlazearmEntity blazearmEntity = new BlazearmEntity(pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1));
        blazearmEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return blazearmEntity;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.BLAZE_ROD);
    }


}
