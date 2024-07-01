package solipingen.sassot.entity.projectile;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
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
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
import solipingen.sassot.mixin.entity.accessors.PersistentProjectileEntityInvoker;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


public class BlazearmEntity extends PersistentProjectileEntity {
    private static final TrackedData<Byte> LOYALTY = DataTracker.registerData(BlazearmEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> SKEWERING = DataTracker.registerData(BlazearmEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(BlazearmEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.BLAZEARM);
    private boolean dealtDamage;
    public int returnTimer;
    private float impactFactor;

    
    public BlazearmEntity(EntityType<? extends BlazearmEntity> entityType, World world) {
        super(entityType, world);
    }

    public BlazearmEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.BLAZEARM_ENTITY, owner, world, stack, null);
        this.dealtDamage = false;
        this.returnTimer = 0;
        this.impactFactor = 1.0f;
        this.dataTracker.set(LOYALTY, this.getLoyalty(stack));
        this.dataTracker.set(SKEWERING, this.getSkewering(stack));
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
        ((PersistentProjectileEntityInvoker)this).invokeSetPierceLevel((byte)0);
    }

    public BlazearmEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.BLAZEARM_ENTITY, x, y, z, world, stack, stack);
        this.dealtDamage = false;
        this.returnTimer = 0;
        this.impactFactor = 1.0f;
        this.dataTracker.set(LOYALTY, this.getLoyalty(stack));
        this.dataTracker.set(SKEWERING, this.getSkewering(stack));
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
        ((PersistentProjectileEntityInvoker)this).invokeSetPierceLevel((byte)0);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(LOYALTY, (byte)0);
        builder.add(SKEWERING, (byte)0);
        builder.add(ENCHANTED, false);
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.getWorld();
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
            if (world.isClient) {
                this.lastRenderY = this.getY();
            }
            double d = 0.1 * (double)i;
            this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
            if (this.returnTimer == 0) {
                this.playSound(ModSoundEvents.BLAZEARM_RETURN, 10.0f, 1.0f);
            }
            ++this.returnTimer;
        }
        if (!this.isTouchingWater()) {
            Vec3d particleVelocity = this.inGround ? new Vec3d(0.0, 0.0, 0.0) : this.getVelocity();
            world.addParticle(ParticleTypes.FLAME, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), 0.5*(this.random.nextDouble() - 0.5) + particleVelocity.getX(), 0.5*(this.random.nextDouble() - 0.5) + particleVelocity.getY(), 0.5*(this.random.nextDouble() - 0.5) + particleVelocity.getZ());
        }
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
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
        World world = this.getWorld();
        if (world.isClient()) return;
        Entity entity2 = this.getOwner();
        Entity entity = entityHitResult.getEntity();
        float entityImpactFactor = this.dealtDamage ? this.impactFactor : Math.max(this.impactFactor, 1.0f);
        float f = 9.0f*entityImpactFactor;
        this.dealtDamage = entity != null;
        if (entity != null) {
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
            DamageSource damageSource = this.getDamageSources().trident(this, entity2 == null ? this : entity2);
            if (entity instanceof LivingEntity livingEntity && world instanceof ServerWorld serverWorld) {
                LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
                f = EnchantmentHelper.getDamage(serverWorld, this.getItemStack(), entity, damageSource, f);
                f += livingEntity.isOnFire() || livingEntity.isInLava() || entityMagmaBlockState.isOf(Blocks.MAGMA_BLOCK)
                        || (livingEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) && !livingEntity.isWet()) ?
                        1.0f*this.getLeaning(this.getItemStack()) : 0.0f;
                if (this.hasSkewering() && iLivingEntity.canBeSkewered()) {
                    iLivingEntity.setIsSkewered(true);
                    iLivingEntity.setSkeweringEntity(this);
                }
            }
            if (entity.damage(damageSource, f)) {
                if (entity.getType() == EntityType.ENDERMAN) {
                    return;
                }
                if (entity instanceof LivingEntity livingEntity2) {
                    this.knockback(livingEntity2, damageSource);
                    this.onHit(livingEntity2);
                    if (!livingEntity2.isOnFire()) {
                        livingEntity2.setOnFireFor(8*(1 + this.getFireAspect(this.getItemStack()) + this.random.nextBetween(0, 8)));
                    }
                }
            }
            if (!this.hasSkewering() || this.getLoyalty() > 0) {
                this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
            }
            this.playSound(ModSoundEvents.BLAZEARM_HIT_ENTITY, 1.0f, 0.8f + 0.4f*this.random.nextFloat());
            if (this.hasSkewering()) {
                this.playSound(ModSoundEvents.SPEAR_SKEWERING, 1.0f, 1.0f);
                if (entity2 instanceof ServerPlayerEntity) {
                    ModCriteria.PLAYER_SKEWERED_ENTITY.trigger((ServerPlayerEntity)entity2, entity, damageSource, f, f, false);
                }
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        World world = this.getWorld();
        this.setSound(ModSoundEvents.BLAZEARM_HIT_BLOCK);
        if (world.isClient() || this.isWet()) return;
        float impactSpeed = (float)this.getVelocity().length();
        BlockPos inBlockPos = blockHitResult.getBlockPos();
        Iterable<BlockPos> blockPosList = BlockPos.iterateOutwards(inBlockPos, Math.round((float)Math.pow(1.0f + impactSpeed/2.7f, impactSpeed/2.7f)), Math.round((float)Math.pow(1.0f + impactSpeed/2.7f, impactSpeed/2.7f)), Math.round((float)Math.pow(1.0f + impactSpeed/2.7f, impactSpeed/2.7f)));
        for (BlockPos blockPos : blockPosList) {
            if (blockPos.getSquaredDistance(inBlockPos) > MathHelper.square(Math.round((float)Math.pow(1.0f + impactSpeed/2.7f, impactSpeed/2.7f))) || this.random.nextDouble() > 1.0/Math.max(blockPos.getSquaredDistance(inBlockPos), 1.0)) continue;
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isAir() && !(world.getBlockState(blockPos.down()).isOf(Blocks.FIRE) || world.getBlockState(blockPos.down()).isOf(Blocks.SOUL_FIRE))) {
                BlockState blockState2 = AbstractFireBlock.getState(world, blockPos);
                world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState) || CandleCakeBlock.canBeLit(blockState)) {
                world.setBlockState(blockPos, (BlockState)blockState.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SNOW) || blockState.isOf(Blocks.SNOW_BLOCK) || blockState.isOf(Blocks.POWDER_SNOW)) {
                world.breakBlock(blockPos, true);
                world.addParticle(ParticleTypes.LARGE_SMOKE, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.0, 0.0, 0.0);
                world.emitGameEvent(null, GameEvent.BLOCK_DESTROY, blockPos);
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.ICE) || blockState.isOf(Blocks.PACKED_ICE)) {
                if (world.getDimension().ultrawarm()) {
                    world.breakBlock(blockPos, false);
                    world.emitGameEvent(null, GameEvent.BLOCK_DESTROY, blockPos);
                }
                else {
                    world.breakBlock(blockPos, false);
                    world.setBlockState(blockPos, Blocks.WATER.getDefaultState());
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                }
                world.addParticle(ParticleTypes.LARGE_SMOKE, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.0, 0.0, 0.0);
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SAND) || blockState.isOf(Blocks.RED_SAND)) {
                world.setBlockState(blockPos, Blocks.GLASS.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RAW_COPPER_BLOCK)) {
                world.setBlockState(blockPos, Blocks.COPPER_BLOCK.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RAW_IRON_BLOCK)) {
                world.setBlockState(blockPos, Blocks.IRON_BLOCK.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RAW_GOLD_BLOCK)) {
                world.setBlockState(blockPos, Blocks.GOLD_BLOCK.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.COBBLESTONE)) {
                world.setBlockState(blockPos, Blocks.STONE.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.COBBLESTONE_SLAB)) {
                world.setBlockState(blockPos, Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.COBBLESTONE_STAIRS)) {
                world.setBlockState(blockPos, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, blockState.get(StairsBlock.FACING)).with(StairsBlock.HALF, blockState.get(StairsBlock.HALF)).with(StairsBlock.SHAPE, blockState.get(StairsBlock.SHAPE))
                    .with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.STONE)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_STONE.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.STONE_SLAB)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SANDSTONE)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_SANDSTONE.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SANDSTONE_SLAB)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_SANDSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.SANDSTONE_STAIRS)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, blockState.get(StairsBlock.FACING)).with(StairsBlock.HALF, blockState.get(StairsBlock.HALF)).with(StairsBlock.SHAPE, blockState.get(StairsBlock.SHAPE))
                    .with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RED_SANDSTONE)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_RED_SANDSTONE.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RED_SANDSTONE_SLAB)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_RED_SANDSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.RED_SANDSTONE_STAIRS)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_RED_SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, blockState.get(StairsBlock.FACING)).with(StairsBlock.HALF, blockState.get(StairsBlock.HALF)).with(StairsBlock.SHAPE, blockState.get(StairsBlock.SHAPE))
                    .with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.QUARTZ_BLOCK)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_QUARTZ.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.QUARTZ_SLAB)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_QUARTZ_SLAB.getDefaultState().with(SlabBlock.TYPE, blockState.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.QUARTZ_STAIRS)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_QUARTZ_STAIRS.getDefaultState().with(StairsBlock.FACING, blockState.get(StairsBlock.FACING)).with(StairsBlock.HALF, blockState.get(StairsBlock.HALF)).with(StairsBlock.SHAPE, blockState.get(StairsBlock.SHAPE))
                    .with(SlabBlock.WATERLOGGED, blockState.get(SlabBlock.WATERLOGGED)));
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.BASALT)) {
                world.setBlockState(blockPos, Blocks.SMOOTH_BASALT.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.CLAY)) {
                world.setBlockState(blockPos, Blocks.TERRACOTTA.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.COBBLED_DEEPSLATE)) {
                world.setBlockState(blockPos, Blocks.DEEPSLATE.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.NETHER_BRICKS)) {
                world.setBlockState(blockPos, Blocks.CRACKED_NETHER_BRICKS.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.STONE_BRICKS)) {
                world.setBlockState(blockPos, Blocks.CRACKED_STONE_BRICKS.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.POLISHED_BLACKSTONE_BRICKS)) {
                world.setBlockState(blockPos, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.DEEPSLATE_BRICKS)) {
                world.setBlockState(blockPos, Blocks.CRACKED_DEEPSLATE_BRICKS.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
            else if (blockState.isOf(Blocks.DEEPSLATE_TILES)) {
                world.setBlockState(blockPos, Blocks.CRACKED_DEEPSLATE_TILES.getDefaultState());
                world.addParticle(ParticleTypes.FLAME, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f), 0.5f*(this.random.nextFloat() - 0.5f));
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.8f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            }
        }
    }

    public boolean hasSkewering() {
        return this.dataTracker.get(SKEWERING) > 0;
    }

    private byte getLoyalty(ItemStack stack) {
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            return (byte)MathHelper.clamp(EnchantmentHelper.getTridentReturnAcceleration(serverWorld, stack, this), 0, 127);
        }
        return 0;
    }

    private byte getSkewering(ItemStack stack) {
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = serverWorld.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            return (byte)EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.SKEWERING), stack);
        }
        return 0;
    }

    private int getLeaning(ItemStack stack) {
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = serverWorld.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            return EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.LEANING), stack);
        }
        return 0;
    }

    private int getFireAspect(ItemStack stack) {
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = serverWorld.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            return EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(Enchantments.FIRE_ASPECT), stack);
        }
        return 0;
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
        this.dealtDamage = nbt.getBoolean("DealtDamage");
        this.dataTracker.set(LOYALTY, nbt.getByte("Loyalty"));
        this.dataTracker.set(SKEWERING, nbt.getByte("Skewering"));
        this.dataTracker.set(ENCHANTED, nbt.getBoolean("Enchanted"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
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

    @Override
    public ItemStack getDefaultItemStack() {
        return DEFAULT_STACK;
    }


}
