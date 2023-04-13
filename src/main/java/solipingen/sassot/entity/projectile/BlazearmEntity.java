package solipingen.sassot.entity.projectile;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import solipingen.sassot.advancement.ModCriteria;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


public class BlazearmEntity extends PersistentProjectileEntity {
    private static final TrackedData<Byte> LOYALTY = DataTracker.registerData(BlazearmEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> SKEWERING = DataTracker.registerData(BlazearmEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(BlazearmEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private ItemStack blazearmStack = new ItemStack(ModItems.BLAZEARM);
    private boolean dealtDamage;
    public int returnTimer;
    private float impactFactor;

    
    public BlazearmEntity(EntityType<? extends BlazearmEntity> entityType, World world) {
        super((EntityType<? extends PersistentProjectileEntity>)entityType, world);
    }

    public BlazearmEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.BLAZEARM_ENTITY, owner, world);
        this.blazearmStack = stack.copy();
        this.dataTracker.set(LOYALTY, (byte)EnchantmentHelper.getLevel(Enchantments.LOYALTY, stack));
        this.dataTracker.set(SKEWERING, (byte)EnchantmentHelper.getLevel(ModEnchantments.SKEWERING, stack));
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LOYALTY, (byte)0);
        this.dataTracker.startTracking(SKEWERING, (byte)0);
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    @Override
    public void tick() {
        this.attemptTickInVoid();
        if (this.inGroundTime > 2) {
            this.dealtDamage = true;
        }
        this.impactFactor = Math.max(MathHelper.square((float)this.getVelocity().length()/2.7f), MathHelper.sqrt((float)this.getVelocity().length()/2.7f));
        Entity entity = this.getOwner();
        byte i = this.dataTracker.get(LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoClip()) && entity != null) {
            this.setNoClip(true);
            Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
            this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double)i, this.getZ());
            if (this.world.isClient) {
                this.lastRenderY = this.getY();
            }
            double d = 0.1 * (double)i;
            this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
            if (this.returnTimer == 0) {
                this.playSound(ModSoundEvents.BLAZEARM_RETURN, 10.0f, 1.0f);
            }
            ++this.returnTimer;
        }
        this.world.addParticle(ParticleTypes.FLAME, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), 0.6*(this.random.nextDouble() - 0.5) + this.getVelocity().x, 0.6*(this.random.nextDouble() - 0.5) + this.getVelocity().y, 0.6*(this.random.nextDouble() - 0.5) + this.getVelocity().z);
        super.tick();
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
    }

    @Override
    protected ItemStack asItemStack() {
        return this.blazearmStack.copy();
    }

    public boolean isEnchanted() {
        return this.dataTracker.get(ENCHANTED);
    }

    public int getInGroundTime() {
        return this.inGroundTime;
    }

    public byte getLoyalty() {
        return this.dataTracker.get(LOYALTY);
    }

    @Override
    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        if (this.dealtDamage) {
            return null;
        }
        return super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (this.world.isClient) return;
        Entity entity2 = this.getOwner();
        Entity entity = entityHitResult.getEntity();
        float entityImpactFactor = this.dealtDamage ? this.impactFactor : (float)Math.max(this.impactFactor, 1.0f);
        float f = 9.0f*entityImpactFactor;
        if (this.hasSkewering()) {
            Vec3d vec3d = this.getPos();
            Vec3d velocity3d = this.getVelocity();
            Vec3d vec3d2 = vec3d.add(velocity3d);
            entity.setPos(vec3d2.x, entity.getY(), vec3d2.z);
            entity.setVelocity(velocity3d);
        }
        BlockState entityMagmaBlockState = Blocks.AIR.getDefaultState();
        Iterable<BlockPos> entityBlockPosIterable = BlockPos.iterateOutwards(entity.getBlockPos(), 1, 1, 1);
        for (BlockPos entityBlockPos : entityBlockPosIterable) {
            if (entityBlockPos.getManhattanDistance(entity.getBlockPos()) > 1) continue;
            entityMagmaBlockState = world.getBlockState(entityBlockPos);
            if (entityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK)) break;
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            f += EnchantmentHelper.getAttackDamage(this.blazearmStack, livingEntity.getGroup());
            f += (livingEntity.isOnFire() || livingEntity.isInLava() || livingEntity.isFireImmune() || entityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK) || (livingEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) && !livingEntity.isWet())) && !(entity instanceof BlazeEntity) ? 1.25f*EnchantmentHelper.getLevel(ModEnchantments.LEANING, this.blazearmStack) : 0.0f;
            if (this.hasSkewering()) {
                iLivingEntity.setIsSkewered(true);
            }
        }
        DamageSource damageSource = this.getDamageSources().trident(this, entity2 == null ? this : entity2);
        this.dealtDamage = true;
        SoundEvent soundEvent = ModSoundEvents.BLAZEARM_HIT_ENTITY;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity)entity;
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity2);
                }
                if (!livingEntity2.isOnFire()) {
                    livingEntity2.setOnFireFor(8*(1 + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, this.blazearmStack)) + this.random.nextBetween(0, 8));
                }
                this.onHit(livingEntity2);
            }
        }
        if (entity2 instanceof WitherSkeletonEntity && entity instanceof LivingEntity) {
            if (!(entity instanceof PlayerEntity && ((PlayerEntity)entity).isCreative())) {
                ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200), entity2);
            }
        }
        if (!this.hasSkewering() || this.getLoyalty() > 0) {
            this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        }
        float g = 1.0f;
        this.playSound(soundEvent, g, 0.8f + 0.4f*this.random.nextFloat());
        if (this.hasSkewering()) {
            this.playSound(ModSoundEvents.SPEAR_SKEWERING, g, 1.0f);
            if (entity2 instanceof ServerPlayerEntity) {
                ModCriteria.PLAYER_SKEWERED_ENTITY.trigger((ServerPlayerEntity)entity2, entity, damageSource, f, f, false);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (this.world.isClient || this.isWet()) return;
        float impactSpeed = (float)this.getVelocity().length();
        BlockPos inBlockPos = blockHitResult.getBlockPos();
        Iterable<BlockPos> blockPosList = BlockPos.iterateOutwards(inBlockPos, Math.round((float)Math.pow(1.0f + impactSpeed/2.7f, impactSpeed/2.7f)), Math.round((float)Math.pow(1.0f + impactSpeed/2.7f, impactSpeed/2.7f)), Math.round((float)Math.pow(1.0f + impactSpeed/2.7f, impactSpeed/2.7f)));
        for (BlockPos blockPos : blockPosList) {
            if (blockPos.getSquaredDistance(inBlockPos) > MathHelper.square(Math.round((float)Math.pow(1.0f + impactSpeed/2.7f, impactSpeed/2.7f))) || this.random.nextDouble() > 1.0/Math.max(blockPos.getSquaredDistance(inBlockPos), 1.0)) continue;
            BlockState blockState = this.world.getBlockState(blockPos);
            if (blockState.isAir() && !(this.world.getBlockState(blockPos.down()).isOf(Blocks.FIRE) || this.world.getBlockState(blockPos.down()).isOf(Blocks.SOUL_FIRE))) {
                BlockState blockState2 = AbstractFireBlock.getState(this.world, blockPos);
                this.world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState) || CandleCakeBlock.canBeLit(blockState)) {
                this.world.setBlockState(blockPos, (BlockState)blockState.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                this.world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SNOW) || blockState.isOf(Blocks.SNOW_BLOCK) || blockState.isOf(Blocks.POWDER_SNOW)) {
                this.world.breakBlock(blockPos, true);
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.0, 0.0, 0.0);
                this.world.emitGameEvent(null, GameEvent.BLOCK_DESTROY, blockPos);
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1.0f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.ICE) || blockState.isOf(Blocks.PACKED_ICE)) {
                if (this.world.getDimension().ultrawarm()) {
                    this.world.breakBlock(blockPos, false);
                    this.world.emitGameEvent(null, GameEvent.BLOCK_DESTROY, blockPos);
                }
                else {
                    this.world.breakBlock(blockPos, false);
                    this.world.setBlockState(blockPos, Blocks.WATER.getDefaultState());
                    this.world.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                }
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.0, 0.0, 0.0);
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1.0f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SAND) || blockState.isOf(Blocks.RED_SAND)) {
                this.world.setBlockState(blockPos, Blocks.GLASS.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RAW_COPPER_BLOCK)) {
                this.world.setBlockState(blockPos, Blocks.COPPER_BLOCK.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RAW_IRON_BLOCK)) {
                this.world.setBlockState(blockPos, Blocks.IRON_BLOCK.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RAW_GOLD_BLOCK)) {
                this.world.setBlockState(blockPos, Blocks.GOLD_BLOCK.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.COBBLESTONE)) {
                this.world.setBlockState(blockPos, Blocks.STONE.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.COBBLESTONE_SLAB)) {
                this.world.setBlockState(blockPos, Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.COBBLESTONE_STAIRS)) {
                this.world.setBlockState(blockPos, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, blockState.get(StairsBlock.FACING)).with(StairsBlock.HALF, blockState.get(StairsBlock.HALF)).with(StairsBlock.SHAPE, blockState.get(StairsBlock.SHAPE))
                    .with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.STONE)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_STONE.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.STONE_SLAB)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SANDSTONE)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_SANDSTONE.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SANDSTONE_SLAB)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_SANDSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SANDSTONE_STAIRS)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, blockState.get(StairsBlock.FACING)).with(StairsBlock.HALF, blockState.get(StairsBlock.HALF)).with(StairsBlock.SHAPE, blockState.get(StairsBlock.SHAPE))
                    .with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RED_SANDSTONE)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_RED_SANDSTONE.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RED_SANDSTONE_SLAB)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_RED_SANDSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RED_SANDSTONE_STAIRS)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_RED_SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, blockState.get(StairsBlock.FACING)).with(StairsBlock.HALF, blockState.get(StairsBlock.HALF)).with(StairsBlock.SHAPE, blockState.get(StairsBlock.SHAPE))
                    .with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.QUARTZ_BLOCK)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_QUARTZ.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.QUARTZ_SLAB)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_QUARTZ_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.QUARTZ_STAIRS)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_QUARTZ_STAIRS.getDefaultState().with(StairsBlock.FACING, blockState.get(StairsBlock.FACING)).with(StairsBlock.HALF, blockState.get(StairsBlock.HALF)).with(StairsBlock.SHAPE, blockState.get(StairsBlock.SHAPE))
                    .with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.BASALT)) {
                this.world.setBlockState(blockPos, Blocks.SMOOTH_BASALT.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.CLAY)) {
                this.world.setBlockState(blockPos, Blocks.TERRACOTTA.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.COBBLED_DEEPSLATE)) {
                this.world.setBlockState(blockPos, Blocks.DEEPSLATE.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.NETHER_BRICKS)) {
                this.world.setBlockState(blockPos, Blocks.CRACKED_NETHER_BRICKS.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.STONE_BRICKS)) {
                this.world.setBlockState(blockPos, Blocks.CRACKED_STONE_BRICKS.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.POLISHED_BLACKSTONE_BRICKS)) {
                this.world.setBlockState(blockPos, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.DEEPSLATE_BRICKS)) {
                this.world.setBlockState(blockPos, Blocks.CRACKED_DEEPSLATE_BRICKS.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.DEEPSLATE_TILES)) {
                this.world.setBlockState(blockPos, Blocks.CRACKED_DEEPSLATE_TILES.getDefaultState());
                this.world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                this.world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, this.world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
        }
    }

    public boolean hasSkewering() {
        return this.dataTracker.get(SKEWERING) > 0;
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    @Override
    protected SoundEvent getHitSound() {
        return ModSoundEvents.BLAZEARM_HIT_BLOCK;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }
    }

    @Override
    protected void tickInVoid() {
        if (this.getLoyalty() > 0 && this.isOwnerAlive()) {
            this.inGroundTime = 10;
        }
        else {
            this.discard();
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Blazearm", NbtElement.COMPOUND_TYPE)) {
            this.blazearmStack = ItemStack.fromNbt(nbt.getCompound("Blazearm"));
        }
        this.dealtDamage = nbt.getBoolean("DealtDamage");
        this.dataTracker.set(LOYALTY, nbt.getByte("Loyalty"));
        this.dataTracker.set(SKEWERING, nbt.getByte("Skewering"));
        this.dataTracker.set(ENCHANTED, nbt.getBoolean("Enchanted"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Blazearm", this.blazearmStack.writeNbt(new NbtCompound()));
        nbt.putBoolean("DealtDamage", this.dealtDamage);
        nbt.putByte("Loyalty", this.getLoyalty());
        nbt.putByte("Skewering", this.dataTracker.get(SKEWERING));
        nbt.putBoolean("Enchanted", this.isEnchanted());
    }

    @Override
    public void age() {
        byte i = this.dataTracker.get(LOYALTY);
        if (this.pickupType != PersistentProjectileEntity.PickupPermission.ALLOWED || i <= 0) {
            super.age();
        }
        else if (this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED || (i > 0 && this.inGround)) {
            if (this.random.nextInt((int)i + 1) == 0) {
                super.age();
            }
        }
    }

    @Override
    protected float getDragInWater() {
        return 0.4444f;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }


}
