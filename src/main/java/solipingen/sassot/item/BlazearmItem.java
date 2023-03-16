package solipingen.sassot.item;

import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Vanishable;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import solipingen.sassot.advancement.ModCriteria;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.entity.projectile.BlazearmEntity;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


public class BlazearmItem extends Item implements Vanishable {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    private final float ATTACK_DAMAGE = 11.0f;
    private final double ATTACK_SPEED_MODIFIER = -2.4;
    public int returnTimer;


    public BlazearmItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", ATTACK_DAMAGE, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", ATTACK_SPEED_MODIFIER, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
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
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity)user;
        int usedTicks = this.getMaxUseTime(stack) - remainingUseTicks;
        if (usedTicks < 10) {
            return;
        }
        int j = EnchantmentHelper.getLevel(ModEnchantments.FLARE, stack);
        if (!world.isClient) {
            stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));
            if (j == 0) {
                BlazearmEntity blazearmEntity = new BlazearmEntity(world, (LivingEntity)playerEntity, stack);
                int strengthLevel = user.hasStatusEffect(StatusEffects.STRENGTH) ? user.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
                int weaknessLevel = user.hasStatusEffect(StatusEffects.WEAKNESS) ? user.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
                blazearmEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, 2.7f + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                blazearmEntity.addVelocity(playerEntity.getVelocity());
                if (playerEntity.getAbilities().creativeMode) {
                    blazearmEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
                world.spawnEntity(blazearmEntity);
                world.playSoundFromEntity(null, blazearmEntity, ModSoundEvents.BLAZEARM_THROW, SoundCategory.PLAYERS, 1.0f, 0.9f);
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
            SoundEvent soundEvent = j >= 3 ? ModSoundEvents.BLAZEARM_FLARE_3 : (j == 2 ? ModSoundEvents.BLAZEARM_FLARE_2 : ModSoundEvents.BLAZEARM_FLARE_1);
            world.playSoundFromEntity(null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
            LivingEntityInterface ILivingEntity = (LivingEntityInterface)playerEntity;
            ILivingEntity.setIsUsingFlare(true);
            playerEntity.setNoDrag(true);
            float f = playerEntity.getYaw();
            float g = playerEntity.getPitch();
            float h = -MathHelper.sin(f * ((float)Math.PI / 180)) * MathHelper.cos(g * ((float)Math.PI / 180));
            float k = -MathHelper.sin(g * ((float)Math.PI / 180));
            float l = MathHelper.cos(f * ((float)Math.PI / 180)) * MathHelper.cos(g * ((float)Math.PI / 180));
            float m = MathHelper.sqrt(h * h + k * k + l * l);
            float n = 2.0f * ((1.0f + (float)j) / 4.0f);
            playerEntity.addVelocity(h *= n / m, k *= n / m, l *= n / m);
            playerEntity.useRiptide(20);
            if (playerEntity.isOnGround()) {
                playerEntity.move(MovementType.SELF, new Vec3d(0.0, 1.1999999284744263, 0.0));
            }
            Box flareBox = playerEntity.getBoundingBox().expand(6.0*j, 3.0*j, 6.0*j);
            List<Entity> entityList = playerEntity.world.getOtherEntities(playerEntity, flareBox);
            for (Entity otherEntity : entityList) {
                if (playerEntity.squaredDistanceTo(otherEntity) > (6.0*j)*(6.0*j)) continue;
                boolean reachBl = false;
                for (int i = 0; i < MathHelper.ceil(otherEntity.getHeight() + 1.0f); ++i) {
                    Vec3d reachVec3d = new Vec3d(otherEntity.getX(), otherEntity.getBodyY(1.0/MathHelper.ceil(otherEntity.getHeight())*i), otherEntity.getZ());
                    BlockHitResult hitResult = world.raycast(new RaycastContext(playerEntity.getPos(), reachVec3d, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, playerEntity));
                    if (((HitResult)hitResult).getType() != HitResult.Type.MISS) continue;
                    reachBl = true;
                    break;
                }
                if (!reachBl) continue;
                float damageAmount = (float)playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
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
                if (otherEntity instanceof LivingEntity && !otherEntity.isWet()) {
                    if (otherEntity instanceof BlazeEntity) return;
                    LivingEntity livingOtherEntity = (LivingEntity)otherEntity;
                    float attackAddition = EnchantmentHelper.getAttackDamage(stack, livingOtherEntity.getGroup());
                    damageAmount += attackAddition;
                    damageAmount *= 0.5f*j*(float)distanceModifier;
                    damageAmount *= (livingOtherEntity.isFireImmune() || livingOtherEntity.isOnFire() || livingOtherEntity.isInLava() || otherEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK) || livingOtherEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) ? 2.0f : 1.0f;
                    damageAmount *= playerEntity.world.isThundering() ? 1.5f : 1.0f;
                    livingOtherEntity.damage(playerEntity.getDamageSources().playerAttack(playerEntity), damageAmount);
                    if (!livingOtherEntity.isOnFire()) {
                        livingOtherEntity.setOnFireFor((int)Math.ceil(4.0*j*distanceModifier));
                    }
                    if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*1.5*j*distanceModifier <= 0.0)) {
                        Vec3d vec3d = livingOtherEntity.getVelocity();
                        Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*2.0*j*distanceModifier);
                        otherEntity.setVelocity(vec3d.x - vec3d2.x, vec3d.y, vec3d.z - vec3d2.z);
                    }
                }
                else if (!(otherEntity instanceof LivingEntity) && !otherEntity.isWet() && (otherEntity.isFireImmune() || otherEntity.isOnFire() || otherEntity.isInLava() || otherEntityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK))) {
                    Vec3d vec3d = otherEntity.getVelocity();
                    Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply(2.0*j*distanceModifier);
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
        if (!(user.isOnFire() || user.isInLava() || magmaBlockState.isOf(Blocks.MAGMA_BLOCK) || (user.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) && !(user.isWet() || ((EntityInterface)user).isBeingSnowedOn()))) && EnchantmentHelper.getLevel(ModEnchantments.FLARE, itemStack) > 0) {
            return TypedActionResult.fail(itemStack);
        }

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (state.isOf(Blocks.COBWEB)) {
            return 20.0f;
        }
        Material material = state.getMaterial();
        if (material == Material.PLANT || material == Material.REPLACEABLE_PLANT || state.isIn(BlockTags.LEAVES) || material == Material.GOURD) {
            return 2.0f;
        }
        return 1.0f;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int j = EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack);
        if (!target.isOnFire()) {
            target.setOnFireFor(8*(j + 1));
        }
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if ((double)state.getHardness(world, pos) != 0.0 && this.getMiningSpeedMultiplier(stack, state) > 1.0f) {
            stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    public float getAttackDamage() {
        return this.ATTACK_DAMAGE;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.BLAZE_ROD);
    }


}
