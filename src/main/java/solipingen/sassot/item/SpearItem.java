package solipingen.sassot.item;

import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BreezeEntity;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.ItemTags;
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
import solipingen.sassot.entity.projectile.spear.BambooSpearEntity;
import solipingen.sassot.entity.projectile.spear.CopperSpearEntity;
import solipingen.sassot.entity.projectile.spear.DiamondSpearEntity;
import solipingen.sassot.entity.projectile.spear.FlintSpearEntity;
import solipingen.sassot.entity.projectile.spear.GoldenSpearEntity;
import solipingen.sassot.entity.projectile.spear.IronSpearEntity;
import solipingen.sassot.entity.projectile.spear.NetheriteSpearEntity;
import solipingen.sassot.entity.projectile.spear.SpearEntity;
import solipingen.sassot.entity.projectile.spear.StoneSpearEntity;
import solipingen.sassot.entity.projectile.spear.WoodenSpearEntity;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.EntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


public class SpearItem extends Item implements Vanishable {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    private final ToolMaterial material;
    private final float attackDamage;
    private final double attackSpeedModifier;
    public int returnTimer;


    public SpearItem(ToolMaterial material, float attackDamage, double attackSpeedModifier, Settings settings) {
        super(settings.maxDamageIfAbsent(material.getDurability()));
        this.material = material;
        this.attackDamage = attackDamage;
        this.attackSpeedModifier = attackSpeedModifier;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", this.attackSpeedModifier, EntityAttributeModifier.Operation.ADDITION));
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
        int j = EnchantmentHelper.getLevel(ModEnchantments.WHIRLWIND, stack);
        if (!world.isClient) {
            stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));
            if (j == 0) {
                int strengthLevel = user.hasStatusEffect(StatusEffects.STRENGTH) ? user.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
                int weaknessLevel = user.hasStatusEffect(StatusEffects.WEAKNESS) ? user.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
                SpearEntity spearEntity = new WoodenSpearEntity(world, (LivingEntity)playerEntity, stack);
                spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, WoodenSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                if (stack.isOf(ModItems.BAMBOO_SPEAR)) {
                    spearEntity = new BambooSpearEntity(world, (LivingEntity)playerEntity, stack);
                    spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, BambooSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                }
                else if (stack.isOf(ModItems.STONE_SPEAR)) {
                    spearEntity = new StoneSpearEntity(world, (LivingEntity)playerEntity, stack);
                    spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, StoneSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                }
                else if (stack.isOf(ModItems.FLINT_SPEAR)) {
                    spearEntity = new FlintSpearEntity(world, (LivingEntity)playerEntity, stack);
                    spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, FlintSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                }
                else if (stack.isOf(ModItems.COPPER_SPEAR)) {
                    spearEntity = new CopperSpearEntity(world, (LivingEntity)playerEntity, stack);
                    spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, CopperSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                }
                else if (stack.isOf(ModItems.GOLDEN_SPEAR)) {
                    spearEntity = new GoldenSpearEntity(world, (LivingEntity)playerEntity, stack);
                    spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, GoldenSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                }
                else if (stack.isOf(ModItems.IRON_SPEAR)) {
                    spearEntity = new IronSpearEntity(world, (LivingEntity)playerEntity, stack);
                    spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, IronSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                }
                else if (stack.isOf(ModItems.DIAMOND_SPEAR)) {
                    spearEntity = new DiamondSpearEntity(world, (LivingEntity)playerEntity, stack);
                    spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, DiamondSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                }
                else if (stack.isOf(ModItems.NETHERITE_SPEAR)) {
                    spearEntity = new NetheriteSpearEntity(world, (LivingEntity)playerEntity, stack);
                    spearEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, NetheriteSpearEntity.SPEED + 0.2f*strengthLevel - 0.2f*weaknessLevel, 0.8f);
                }
                spearEntity.addVelocity(playerEntity.getVelocity());
                if (playerEntity.getAbilities().creativeMode) {
                    spearEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
                world.spawnEntity(spearEntity);
                world.playSoundFromEntity(null, spearEntity, ModSoundEvents.SPEAR_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f + 0.1f*user.getRandom().nextFloat());
                if (!playerEntity.getAbilities().creativeMode) {
                    playerEntity.getInventory().removeOne(stack);
                }
            }
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        if (j > 0 && !(playerEntity.isSubmergedInWater() || playerEntity.isSubmergedIn(FluidTags.LAVA))) {
            SoundEvent soundEvent = j >= 3 ? ModSoundEvents.SPEAR_WHIRLWIND_3 : (j == 2 ? ModSoundEvents.SPEAR_WHIRLWIND_2 : ModSoundEvents.SPEAR_WHIRLWIND_1);
            world.playSoundFromEntity(null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
            Iterable<BlockPos> blockPosList = BlockPos.iterateOutwards(playerEntity.getBlockPos(), MathHelper.ceil(playerEntity.getWidth()), MathHelper.ceil(playerEntity.getHeight()), MathHelper.ceil(playerEntity.getWidth()));
            for (BlockPos currentBlockPos : blockPosList) {
                if (world.getBlockState(currentBlockPos).isOf(Blocks.POWDER_SNOW)) {
                    world.breakBlock(currentBlockPos, false);
                }
            }
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)playerEntity;
            iLivingEntity.setIsUsingWhirlwind(true);
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
            Box whirlwindBox = playerEntity.getBoundingBox().expand(4.0*j, 2.0*j, 4.0*j);
            List<Entity> entityList = world.getOtherEntities(playerEntity, whirlwindBox);
            for (Entity otherEntity : entityList) {
                if (playerEntity.squaredDistanceTo(otherEntity) > (4.0*j)*(4.0*j)) continue;
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
                if (otherEntity instanceof LivingEntity) {
                    if (otherEntity instanceof BreezeEntity) return;
                    LivingEntity livingOtherEntity = (LivingEntity)otherEntity;
                    float attackAddition = EnchantmentHelper.getAttackDamage(stack, livingOtherEntity.getGroup());
                    damageAmount += attackAddition;
                    damageAmount *= 0.25f*j*(float)distanceModifier;
                    damageAmount *= livingOtherEntity instanceof FlyingEntity || ((EntityInterface)livingOtherEntity).isBeingSnowedOn() ? 2.0f : 1.0f;
                    damageAmount *= playerEntity.getWorld().isThundering() ? 1.5f : 1.0f;
                    if (!(livingOtherEntity.isSubmergedInWater() || livingOtherEntity.isSubmergedIn(FluidTags.LAVA))) {
                        livingOtherEntity.damage(playerEntity.getDamageSources().playerAttack(playerEntity), damageAmount);
                        if (((EntityInterface)livingOtherEntity).isBeingSnowedOn()) {
                            livingOtherEntity.setFrozenTicks(livingOtherEntity.getMinFreezeDamageTicks()*(j + 1));
                        }
                        if (!((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*1.5*j*distanceModifier <= 0.0)) {
                            Vec3d vec3d = livingOtherEntity.getVelocity();
                            Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply((1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*1.5*j*distanceModifier);
                            otherEntity.setVelocity(vec3d.x - vec3d2.x, livingOtherEntity.isOnGround() ? Math.min(vec3d.y + (1.0 - livingOtherEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))*1.5*j*distanceModifier, 0.4) : vec3d.y, vec3d.z - vec3d2.z);
                        }
                    }
                }
                else if (!(otherEntity instanceof LivingEntity) && !(otherEntity.isSubmergedInWater() || otherEntity.isSubmergedIn(FluidTags.LAVA))) {
                    Vec3d vec3d = otherEntity.getVelocity();
                    Vec3d vec3d2 = new Vec3d(xDiff, 0.0, zDiff).normalize().multiply(1.5*j*distanceModifier);
                    otherEntity.setVelocity(vec3d.x - vec3d2.x, otherEntity.isOnGround() ? Math.min(vec3d.y + 1.5*j*distanceModifier, 0.4) : vec3d.y, vec3d.z - vec3d2.z);
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
        if ((user.isSubmergedInWater() || user.isSubmergedIn(FluidTags.LAVA)) && EnchantmentHelper.getLevel(ModEnchantments.WHIRLWIND, itemStack) > 0) {
            return TypedActionResult.fail(itemStack);
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if ((double)state.getHardness(world, pos) != 0.0) {
            stack.damage(0, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
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
        return this.material.getEnchantability();
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isIn(ItemTags.PLANKS);
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }


}
