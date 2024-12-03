package solipingen.sassot.mixin.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.sound.ModSoundEvents;

import java.util.List;


@Mixin(MaceItem.class)
public abstract class MaceItemMixin extends Item {
    @Unique private static final double ATTACK_DAMAGE = 9.0;
    @Unique private static final float ATTACK_SPEED_MODIFIER = -3.4f;


    public MaceItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static Item.Settings modifiedInit(Item.Settings settings) {
        return settings.maxDamage(1095);
    }

    @Inject(method = "createAttributeModifiers", at = @At("HEAD"), cancellable = true)
    private static void injectedAttributeModifiers(CallbackInfoReturnable<AttributeModifiersComponent> cbireturn) {
        cbireturn.setReturnValue(AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, ATTACK_DAMAGE, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, ATTACK_SPEED_MODIFIER, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build());
    }

    @ModifyConstant(method = "createToolComponent", constant = @Constant(intValue = 2))
    private static int modifiedDamagePerBlock(int originalInt) {
        return 1;
    }

    @Inject(method = "postHit",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/MaceItem;shouldDealAdditionalDamage(Lnet/minecraft/entity/LivingEntity;)Z", shift = At.Shift.AFTER), cancellable = true)
    private void injectedPostHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cbireturn) {
        if (attacker instanceof ServerPlayerEntity serverPlayerEntity && MaceItem.shouldDealAdditionalDamage(attacker)) {
            ServerWorld world = serverPlayerEntity.getServerWorld();
            RegistryEntryLookup<Enchantment> enchantmentLookup = attacker.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            if (EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.GROUNDSHAKING), stack) > 0) {
                BlockPos inBlockPos = target.getBlockPos();
                float strength = this.getBonusAttackDamage(target, (float)ATTACK_DAMAGE, serverPlayerEntity.getDamageSources().playerAttack(serverPlayerEntity));
                int range = Math.round(strength);
                Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(inBlockPos, range, range, range);
                this.shakeGround(serverPlayerEntity, target, inBlockPos, inBlockPos, strength, stack);
                for (BlockPos currentBlockPos : blockPosIterable) {
                    if (currentBlockPos.getSquaredDistance(inBlockPos) > MathHelper.square(range)) continue;
                    this.shakeGround(serverPlayerEntity, target, currentBlockPos, inBlockPos, strength, stack);
                }
                if (world instanceof ServerWorld) {
                    world.spawnParticles(ParticleTypes.EXPLOSION, target.getX(), target.getY(), target.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                }
                target.playSound(ModSoundEvents.GROUNDSHAKING, 20.0f*strength, 0.8f + 0.4f*target.getRandom().nextFloat());
            }
        }
    }

    @Inject(method = "getBonusAttackDamage", at = @At("HEAD"), cancellable = true)
    private void injectedGetBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource, CallbackInfoReturnable<Float> cbireturn) {
        Entity attacker = damageSource.getAttacker();
        if (attacker != null && attacker instanceof PlayerEntity player) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = player.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            int i = EnchantmentHelper.getEquipmentLevel(enchantmentLookup.getOrThrow(Enchantments.DENSITY), player);
            float f = (i/2.0f + 1)*(float)player.getVelocity().length()*baseAttackDamage;
            float h = baseAttackDamage/(float)player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            f *= h;
            cbireturn.setReturnValue(MaceItem.shouldDealAdditionalDamage(player) ? f : 0.0f);
        }
        else {
            cbireturn.setReturnValue(0.0f);
        }
    }

    @Inject(method = "shouldDealAdditionalDamage", at = @At("HEAD"), cancellable = true)
    private static void injectedShouldDealAdditionalDamage(LivingEntity attacker, CallbackInfoReturnable<Boolean> cbireturn) {
        RegistryEntryLookup<Enchantment> enchantmentLookup = attacker.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        int i = EnchantmentHelper.getEquipmentLevel(enchantmentLookup.getOrThrow(Enchantments.DENSITY), attacker);
        float f = (i/2.0f + 1)*(float)attacker.getVelocity().length();
        cbireturn.setReturnValue(f > 1.0f);
    }

    @Unique
    private void shakeGround(LivingEntity attacker, LivingEntity target, BlockPos currentBlockPos, BlockPos sourceBlockPos, float strength, ItemStack stack) {
        World world = attacker.getWorld();
        BlockState blockState = world.getBlockState(currentBlockPos);
        if (blockState.isAir() || blockState.getBlock() instanceof FluidBlock) return;
        float strengthOnBlock = strength/Math.max(MathHelper.sqrt(blockState.getBlock().getBlastResistance()), 1.0f);
        int blocksBroken = 0;
        int fluidBlocksNumber = 0;
        for (Direction direction : Direction.values()) {
            BlockState neighborBlockState = world.getBlockState(currentBlockPos.offset(direction));
            if (neighborBlockState.isAir() || neighborBlockState.getBlock() instanceof FluidBlock) {
                fluidBlocksNumber += fluidBlocksNumber >= Direction.values().length ? 0 : 1;
            }
        }
        double currentSquaredDistance = currentBlockPos.getSquaredDistance(sourceBlockPos);
        float reductionFactor = currentBlockPos == sourceBlockPos ? 1.0f : (float)(Math.log(Direction.values().length - Math.max(fluidBlocksNumber - 1, 0))/Math.log((Direction.values().length))/Math.max(currentSquaredDistance, 1.0));
        strengthOnBlock *= reductionFactor;
        List<Entity> entityList = world.getOtherEntities(target, new Box(currentBlockPos).expand(1.0));
        for (Entity entity : entityList) {
            if (strengthOnBlock <= 0.0f) continue;
            Vec3d diffVecNorm = entity.getPos().subtract(target.getPos()).normalize();
            if (entity instanceof LivingEntity && (entity.isOnGround() || entity.isInsideWall() || ((LivingEntity)entity).isClimbing()) && !(entity instanceof PlayerEntity && ((PlayerEntity)entity).isCreative())) {
                LivingEntity livingEntity = (LivingEntity)entity;
                double knockbackFactor = 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
                livingEntity.damage(attacker.getDamageSources().explosion(attacker, attacker), strengthOnBlock);
                livingEntity.addVelocity(0.4*knockbackFactor*strengthOnBlock*diffVecNorm.getX(), 0.25*knockbackFactor*strengthOnBlock*diffVecNorm.getY(), 0.4*knockbackFactor*strengthOnBlock*diffVecNorm.getZ());
            }
            if (!(entity instanceof LivingEntity) && (entity.isOnGround() || entity.isInsideWall())) {
                entity.addVelocity(0.4*strengthOnBlock*diffVecNorm.getX(), 0.25*strengthOnBlock*diffVecNorm.getY(), 0.4*strengthOnBlock*diffVecNorm.getZ());
            }
        }
        if (blockState.getBlock().getBlastResistance() < strengthOnBlock) {
            if (blockState.isOf(Blocks.INFESTED_STONE)) {
                world.setBlockState(currentBlockPos, Blocks.STONE.getDefaultState());
                world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_COBBLESTONE)) {
                world.setBlockState(currentBlockPos, Blocks.COBBLESTONE.getDefaultState());
                world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_STONE_BRICKS)) {
                world.setBlockState(currentBlockPos, Blocks.STONE_BRICKS.getDefaultState());
                world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_CRACKED_STONE_BRICKS)) {
                world.setBlockState(currentBlockPos, Blocks.CRACKED_STONE_BRICKS.getDefaultState());
                world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_MOSSY_STONE_BRICKS)) {
                world.setBlockState(currentBlockPos, Blocks.MOSSY_STONE_BRICKS.getDefaultState());
                world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_CHISELED_STONE_BRICKS)) {
                world.setBlockState(currentBlockPos, Blocks.CHISELED_STONE_BRICKS.getDefaultState());
                world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_DEEPSLATE)) {
                world.setBlockState(currentBlockPos, Blocks.DEEPSLATE.getDefaultState());
                world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            if (world.breakBlock(currentBlockPos, true)) {
                blocksBroken++;
                world.emitGameEvent(null, GameEvent.BLOCK_DESTROY, currentBlockPos);
            }
        }
        if (attacker instanceof ServerPlayerEntity && blocksBroken > 0) {
            stack.damage(blocksBroken, attacker, EquipmentSlot.MAINHAND);
        }
    }



}
