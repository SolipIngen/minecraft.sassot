package solipingen.sassot.entity.projectile.spear;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import solipingen.sassot.advancement.ModCriteria;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


public abstract class SpearEntity extends PersistentProjectileEntity {
    private static final TrackedData<Byte> LOYALTY = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> SKEWERING = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> GROUNDSHAKING = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final float damageAmount;
    private final float launchSpeed;
    private ItemStack spearStack;
    private boolean dealtDamage;
    private boolean brokeBlock;
    public int returnTimer;
    private float impactFactor;


    public SpearEntity(EntityType<? extends SpearEntity> entityType, float damageAmount, float launchSpeed, World world) {
        super(entityType, world);
        this.damageAmount = damageAmount;
        this.launchSpeed = launchSpeed;
        this.brokeBlock = false;
    }

    public SpearEntity(EntityType<? extends SpearEntity> entityType, LivingEntity owner, float damageAmount, float launchSpeed, ItemStack stack, World world) {
        super(entityType, owner, world);
        this.damageAmount = damageAmount;
        this.launchSpeed = launchSpeed;
        this.spearStack = stack.copy();
        this.brokeBlock = false;
        this.dataTracker.set(LOYALTY, (byte)EnchantmentHelper.getLevel(Enchantments.LOYALTY, spearStack));
        this.dataTracker.set(SKEWERING, (byte)EnchantmentHelper.getLevel(ModEnchantments.SKEWERING, spearStack));
        this.dataTracker.set(GROUNDSHAKING, (byte)EnchantmentHelper.getLevel(ModEnchantments.GROUNDSHAKING, spearStack));
        this.dataTracker.set(ENCHANTED, spearStack.hasGlint());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LOYALTY, (byte)0);
        this.dataTracker.startTracking(SKEWERING, (byte)0);
        this.dataTracker.startTracking(GROUNDSHAKING, (byte)0);
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    @Override
    public void tick() {
        this.attemptTickInVoid();
        if (this.inGroundTime > 2) {
            this.dealtDamage = true;
        }
        this.impactFactor = Math.max(MathHelper.square((float)this.getVelocity().length()/this.launchSpeed), MathHelper.sqrt((float)this.getVelocity().length()/this.launchSpeed));
        Entity entity = this.getOwner();
        byte i = this.dataTracker.get(LOYALTY);
        if (i > 0 && (this.dealtDamage || this.brokeBlock || this.isNoClip()) && entity != null) {
            this.setNoClip(true);
            Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
            this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double)i, this.getZ());
            if (this.world.isClient) {
                this.lastRenderY = this.getY();
            }
            double d = 0.1 * (double)i;
            this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
            if (this.returnTimer == 0) {
                this.playSound(ModSoundEvents.SPEAR_RETURN, 10.0f, 1.0f);
            }
            ++this.returnTimer;
        }
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
        return this.spearStack.copy();
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
        float f = this.damageAmount*entityImpactFactor;
        if (this.hasSkewering()) {
            Vec3d vec3d = this.getPos();
            Vec3d velocity3d = this.getVelocity();
            Vec3d vec3d2 = vec3d.add(velocity3d);
            entity.setPos(vec3d2.x, entity.getY(), vec3d2.z);
            entity.setVelocity(velocity3d);
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            f += EnchantmentHelper.getAttackDamage(this.spearStack, livingEntity.getGroup());
            if (this.hasSkewering()) {
                iLivingEntity.setIsSkewered(true);
            }
        }
        DamageSource damageSource = this.getDamageSources().thrown(this, entity2 == null ? this : entity2);
        this.dealtDamage = true;
        SoundEvent soundEvent = ModSoundEvents.SPEAR_HIT_ENTITY;
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
        if (this.world.isClient) return;
        if (this.hasGroundshaking()) {
            BlockPos inBlockPos = blockHitResult.getBlockPos();
            float strength = this.damageAmount*this.impactFactor;
            int range = MathHelper.ceil(this.damageAmount*this.impactFactor);
            Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(inBlockPos, range, range, range);
            this.shakeGround(inBlockPos, inBlockPos, strength);
            for (BlockPos currentBlockPos : blockPosIterable) {
                if (currentBlockPos.getSquaredDistance(inBlockPos) > MathHelper.square(range)) continue;
                this.shakeGround(currentBlockPos, inBlockPos, strength);
            }
            if (this.world instanceof ServerWorld) {
                ((ServerWorld)this.world).spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            }
            this.playSound(ModSoundEvents.GROUNDSHAKING, 20.0f*strength, 0.8f + 0.4f*this.random.nextFloat());
        }
    }

    private void shakeGround(BlockPos currentBlockPos, BlockPos sourceBlockPos, float strength) {
        BlockState blockState = this.world.getBlockState(currentBlockPos);
        if (blockState.isAir() || blockState.getBlock() instanceof FluidBlock) return;
        float strengthOnBlock = strength/Math.max(MathHelper.sqrt(blockState.getBlock().getBlastResistance()), 1.0f);
        int blocksBroken = 0;
        int fluidBlocksNumber = 0;
        for (Direction direction : Direction.values()) {
            BlockState neighborBlockState = this.world.getBlockState(currentBlockPos.offset(direction));
            if (neighborBlockState.isAir() || neighborBlockState.getBlock() instanceof FluidBlock) {
                fluidBlocksNumber += fluidBlocksNumber >= Direction.values().length ? 0 : 1;
            }
        }
        double currentSquaredDistance = currentBlockPos.getSquaredDistance(sourceBlockPos);
        float reductionFactor = currentBlockPos == sourceBlockPos ? 1.0f : (float)(Math.log(Direction.values().length - Math.max(fluidBlocksNumber - 1, 0))/Math.log((Direction.values().length))/Math.max(currentSquaredDistance, 1.0));
        strengthOnBlock *= reductionFactor;
        Entity attacker = this.getOwner() != null ? this.getOwner() : this;
        List<Entity> entityList = this.world.getOtherEntities(this, new Box(currentBlockPos).expand(1.0));
        for (Entity entity : entityList) {
            if (strengthOnBlock <= 0.0f) continue;
            Vec3d diffVecNorm = entity.getPos().subtract(this.getPos()).normalize();
            if (entity instanceof LivingEntity && (entity.isOnGround() || entity.isInsideWall() || ((LivingEntity)entity).isClimbing()) && !(entity instanceof PlayerEntity && ((PlayerEntity)entity).isCreative())) {
                LivingEntity livingEntity = (LivingEntity)entity;
                double knockbackFactor = 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
                livingEntity.damage(this.getDamageSources().explosion(this, attacker), strengthOnBlock);
                livingEntity.addVelocity(0.4*knockbackFactor*strengthOnBlock*diffVecNorm.getX(), 0.25*knockbackFactor*strengthOnBlock*diffVecNorm.getY(), 0.4*knockbackFactor*strengthOnBlock*diffVecNorm.getZ());
            }
            if (!(entity instanceof LivingEntity) && (entity.isOnGround() || entity.isInsideWall())) {
                entity.addVelocity(0.4*strengthOnBlock*diffVecNorm.getX(), 0.25*strengthOnBlock*diffVecNorm.getY(), 0.4*strengthOnBlock*diffVecNorm.getZ());
            }
        }
        if (blockState.getBlock().getBlastResistance() < strengthOnBlock) {
            if (blockState.isOf(Blocks.INFESTED_STONE)) {
                this.world.setBlockState(currentBlockPos, Blocks.STONE.getDefaultState());
                this.world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                this.world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_COBBLESTONE)) {
                this.world.setBlockState(currentBlockPos, Blocks.COBBLESTONE.getDefaultState());
                this.world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                this.world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_STONE_BRICKS)) {
                this.world.setBlockState(currentBlockPos, Blocks.STONE_BRICKS.getDefaultState());
                this.world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                this.world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_CRACKED_STONE_BRICKS)) {
                this.world.setBlockState(currentBlockPos, Blocks.CRACKED_STONE_BRICKS.getDefaultState());
                this.world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                this.world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_MOSSY_STONE_BRICKS)) {
                this.world.setBlockState(currentBlockPos, Blocks.MOSSY_STONE_BRICKS.getDefaultState());
                this.world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                this.world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_CHISELED_STONE_BRICKS)) {
                this.world.setBlockState(currentBlockPos, Blocks.CHISELED_STONE_BRICKS.getDefaultState());
                this.world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                this.world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            else if (blockState.isOf(Blocks.INFESTED_DEEPSLATE)) {
                this.world.setBlockState(currentBlockPos, Blocks.DEEPSLATE.getDefaultState());
                this.world.playSound(null, currentBlockPos, SoundEvents.ENTITY_SILVERFISH_DEATH, SoundCategory.HOSTILE);
                this.world.emitGameEvent(null, GameEvent.ENTITY_DIE, currentBlockPos);
            }
            if (this.world.breakBlock(currentBlockPos, true)) {
                if (!this.brokeBlock) {
                    this.brokeBlock = true;
                }
                blocksBroken++;
                this.world.emitGameEvent(null, GameEvent.BLOCK_DESTROY, currentBlockPos);
            }
        }
        if (this.getOwner() != null && this.getOwner() instanceof ServerPlayerEntity && blocksBroken > 0) {
            this.spearStack.damage(blocksBroken, this.random, (ServerPlayerEntity)this.getOwner());
        }
        if (!this.world.isClient && !this.isNoClip() && this.isInsideWall()) {
            this.setVelocity(0.0, 0.0, 0.0);
        }
    }

    public boolean hasSkewering() {
        return this.dataTracker.get(SKEWERING) > 0;
    }

    public boolean hasGroundshaking() {
        return this.dataTracker.get(GROUNDSHAKING) > 0;
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    @Override
    protected SoundEvent getHitSound() {
        return ModSoundEvents.SPEAR_HIT_BLOCK;
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
        if (nbt.contains("Spear", NbtElement.COMPOUND_TYPE)) {
            this.spearStack = ItemStack.fromNbt(nbt.getCompound("Spear"));
        }
        this.dealtDamage = nbt.getBoolean("DealtDamage");
        this.brokeBlock = nbt.getBoolean("BrokeBlock");
        this.dataTracker.set(LOYALTY, nbt.getByte("Loyalty"));
        this.dataTracker.set(SKEWERING, nbt.getByte("Skewering"));
        this.dataTracker.set(GROUNDSHAKING, nbt.getByte("Groundshaking"));
        this.dataTracker.set(ENCHANTED, nbt.getBoolean("Enchanted"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Spear", this.spearStack.writeNbt(new NbtCompound()));
        nbt.putBoolean("DealtDamage", this.dealtDamage);
        nbt.putBoolean("BrokeBlock", this.brokeBlock);
        nbt.putByte("Loyalty", this.getLoyalty());
        nbt.putByte("Skewering", this.dataTracker.get(SKEWERING));
        nbt.putByte("Groundshaking", this.dataTracker.get(GROUNDSHAKING));
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
        return 0.6f;
    }

}
