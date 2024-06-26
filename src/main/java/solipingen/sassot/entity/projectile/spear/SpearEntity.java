package solipingen.sassot.entity.projectile.spear;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
import solipingen.sassot.mixin.entity.accessors.PersistentProjectileEntityInvoker;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


public abstract class SpearEntity extends PersistentProjectileEntity {
    private static final TrackedData<Byte> LOYALTY = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> SKEWERING = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Byte> GROUNDSHAKING = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(SpearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final ToolMaterial material;
    private final float damageAmount;
    private final float launchSpeed;
    private boolean dealtDamage;
    private boolean brokeBlock;
    public int returnTimer;
    private float impactFactor;


    public SpearEntity(EntityType<? extends SpearEntity> entityType, World world, ToolMaterial material, float launchSpeed) {
        super(entityType, world);
        this.material = material;
        this.damageAmount = material.getAttackDamage() + 4.0f;
        this.launchSpeed = launchSpeed;
    }

    public SpearEntity(EntityType<? extends SpearEntity> entityType, LivingEntity owner, World world, ItemStack stack, ToolMaterial material, float launchSpeed) {
        super(entityType, owner, world, stack, null);
        this.material = material;
        this.damageAmount = material.getAttackDamage() + 4.0f;
        this.launchSpeed = launchSpeed;
        this.dealtDamage = false;
        this.brokeBlock = false;
        this.returnTimer = 0;
        this.impactFactor = 1.0f;
        this.dataTracker.set(LOYALTY, this.getLoyalty(stack));
        this.dataTracker.set(SKEWERING, this.getSkewering(stack));
        this.dataTracker.set(GROUNDSHAKING, this.getGroundshaking(stack));
        this.dataTracker.set(ENCHANTED, this.getItemStack().hasGlint());
        ((PersistentProjectileEntityInvoker)this).invokeSetPierceLevel((byte)0);
    }

    public SpearEntity(EntityType<? extends SpearEntity> entityType, double x, double y, double z, World world, ItemStack stack, ToolMaterial material, float launchSpeed) {
        super(entityType,x, y, z, world, stack, stack);
        this.material = material;
        this.damageAmount = material.getAttackDamage() + 4.0f;
        this.launchSpeed = launchSpeed;
        this.dealtDamage = false;
        this.brokeBlock = false;
        this.returnTimer = 0;
        this.impactFactor = 1.0f;
        this.dataTracker.set(LOYALTY, this.getLoyalty(stack));
        this.dataTracker.set(SKEWERING, this.getSkewering(stack));
        this.dataTracker.set(GROUNDSHAKING, this.getGroundshaking(stack));
        this.dataTracker.set(ENCHANTED, this.getItemStack().hasGlint());
        ((PersistentProjectileEntityInvoker)this).invokeSetPierceLevel((byte)0);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(LOYALTY, (byte)0);
        builder.add(SKEWERING, (byte)0);
        builder.add(GROUNDSHAKING, (byte)0);
        builder.add(ENCHANTED, false);
    }

    @Override
    public void tick() {
        super.tick();
        this.attemptTickInVoid();
        if (this.inGroundTime > 2) {
            this.dealtDamage = true;
        }
        this.impactFactor = Math.max(MathHelper.square((float)this.getVelocity().length()/this.launchSpeed), MathHelper.sqrt((float)this.getVelocity().length()/this.launchSpeed));
        Entity entity = this.getOwner();
        byte i = this.dataTracker.get(LOYALTY);
        if (i > 0 && entity != null && (this.dealtDamage || this.brokeBlock || this.inGroundTime > 0 || this.isNoClip())) {
            this.setNoClip(true);
            Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
            this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double)i, this.getZ());
            World world = this.getWorld();
            if (world.isClient) {
                this.lastRenderY = this.getY();
            }
            double d = 0.1 * (double)i;
            this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
            if (this.returnTimer == 0) {
                this.playSound(ModSoundEvents.SPEAR_RETURN, 10.0f, 1.0f);
            }
            ++this.returnTimer;
        }
    }

    public ToolMaterial getToolMaterial() {
        return this.material;
    }

    public float getLaunchSpeed() {
        return this.launchSpeed;
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
        float f = this.damageAmount*entityImpactFactor;
        this.dealtDamage = entity != null;
        if (entity != null && this.hasSkewering()) {
            Vec3d vec3d = this.getPos();
            Vec3d velocity3d = this.getVelocity();
            Vec3d vec3d2 = vec3d.add(velocity3d);
            entity.setPos(vec3d2.x, entity.getY(), vec3d2.z);
            entity.setVelocity(velocity3d);
        }
        DamageSource damageSource = this.getDamageSources().trident(this, entity2 == null ? this : entity2);
        if (entity instanceof LivingEntity livingEntity && world instanceof ServerWorld serverWorld) {
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            f = EnchantmentHelper.getDamage(serverWorld, this.getItemStack(), livingEntity, damageSource, f);
            if (this.hasSkewering() && iLivingEntity.canBeSkewered()) {
                iLivingEntity.setIsSkewered(true);
                iLivingEntity.setSkeweringEntity(this);
            }
        }
        if (entity != null && entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingEntity2) {
                this.knockback(livingEntity2, damageSource);
                this.onHit(livingEntity2);
            }
        }
        if (!this.hasSkewering() || this.getLoyalty() > 0) {
            this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        }
        this.playSound(ModSoundEvents.SPEAR_HIT_ENTITY, 1.0f, 0.8f + 0.4f*this.random.nextFloat());
        if (this.hasSkewering()) {
            this.playSound(ModSoundEvents.SPEAR_SKEWERING, 1.0f, 1.0f);
            if (entity2 instanceof ServerPlayerEntity) {
                ModCriteria.PLAYER_SKEWERED_ENTITY.trigger((ServerPlayerEntity)entity2, entity, damageSource, f, f, false);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        World world = this.getWorld();
        this.setSound(ModSoundEvents.SPEAR_HIT_BLOCK);
        if (world.isClient()) return;
        if (this.hasGroundshaking() && !(this.brokeBlock || this.dealtDamage)) {
            BlockPos inBlockPos = blockHitResult.getBlockPos();
            float strength = this.damageAmount*this.impactFactor;
            int range = MathHelper.ceil(this.damageAmount*this.impactFactor);
            Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(inBlockPos, range, range, range);
            this.shakeGround(inBlockPos, inBlockPos, strength);
            this.brokeBlock = true;
            for (BlockPos currentBlockPos : blockPosIterable) {
                if (currentBlockPos.getSquaredDistance(inBlockPos) > MathHelper.square(range)) continue;
                this.shakeGround(currentBlockPos, inBlockPos, strength);
            }
            if (world instanceof ServerWorld) {
                ((ServerWorld)world).spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            }
            this.playSound(ModSoundEvents.GROUNDSHAKING, 20.0f*strength, 0.8f + 0.4f*this.random.nextFloat());
        }
    }

    private void shakeGround(BlockPos currentBlockPos, BlockPos sourceBlockPos, float strength) {
        World world = this.getWorld();
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
        Entity attacker = this.getOwner() != null ? this.getOwner() : this;
        List<Entity> entityList = world.getOtherEntities(this, new Box(currentBlockPos).expand(1.0));
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
        if (world instanceof ServerWorld serverWorld) {
            Vec3d vec3d = currentBlockPos.toCenterPos().add(0.0, 0.5, 0.0);
            int i = (int)(50.0f*this.impactFactor*this.fallDistance);
            serverWorld.spawnParticles((ParticleEffect)new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), vec3d.x, vec3d.y, vec3d.z, i, 0.3f, 0.3f, 0.3f, 0.15f);
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
        if (this.getOwner() != null && this.getOwner() instanceof ServerPlayerEntity && blocksBroken > 0) {
            this.getItemStack().damage(blocksBroken, (ServerPlayerEntity)this.getOwner(), EquipmentSlot.MAINHAND);
        }
        if (!world.isClient() && !this.isNoClip() && this.isInsideWall()) {
            this.setVelocity(0.0, 0.0, 0.0);
        }
    }

    public boolean hasSkewering() {
        return this.dataTracker.get(SKEWERING) > 0;
    }

    public boolean hasGroundshaking() {
        return this.dataTracker.get(GROUNDSHAKING) > 0;
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

    private byte getGroundshaking(ItemStack stack) {
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            RegistryEntryLookup<Enchantment> enchantmentLookup = serverWorld.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
            return (byte)EnchantmentHelper.getLevel(enchantmentLookup.getOrThrow(ModEnchantments.GROUNDSHAKING), stack);
        }
        return 0;
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
        return 2.0f/3.0f;
    }



}
