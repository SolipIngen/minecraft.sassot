package solipingen.sassot.mixin.entity.projectile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solipingen.sassot.advancement.ModCriteria;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;
import solipingen.sassot.util.interfaces.mixin.entity.projectile.TridentEntityInterface;


@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity implements TridentEntityInterface {
    private static final TrackedData<Byte> SKEWERING = DataTracker.registerData(TridentEntity.class, TrackedDataHandlerRegistry.BYTE);
    @Shadow @Final private static TrackedData<Byte> LOYALTY;
    @Shadow @Final private static TrackedData<Boolean> ENCHANTED;
    @Shadow private boolean dealtDamage;
    @Unique float impactFactor;

    @Invoker("isOwnerAlive")
    protected abstract boolean invokeIsOwnerAlive();


    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack) {
        super(type, x, y, z, world, stack);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    private void injectedInit(World world, LivingEntity owner, ItemStack stack, CallbackInfo cbi) {
        this.impactFactor = 1.0f;
        this.dataTracker.set(SKEWERING, (byte)EnchantmentHelper.getLevel(ModEnchantments.SKEWERING, stack));
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectedInitDataTracker(DataTracker.Builder builder, CallbackInfo cbi) {
        builder.add(SKEWERING, (byte)0);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectedSuperTick(CallbackInfo cbi) {
        super.tick();
    }
    
    @ModifyConstant(method = "tick", constant = @Constant(intValue = 4))
    private int modifiedInGroundTime(int inGroundTime) {
        return 2;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;getOwner()Lnet/minecraft/entity/Entity;"))
    private void injectedTick(CallbackInfo cbi) {
        this.impactFactor = Math.max(MathHelper.square((float)this.getVelocity().length()/2.5f), MathHelper.sqrt((float)this.getVelocity().length()/2.5f));
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;asItemStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack redirectedAsItemStack(TridentEntity tridentEntity) {
        return ItemStack.EMPTY;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;discard()V"))
    private void redirectedDiscard(TridentEntity tridentEntity) {
    }

    @ModifyConstant(method = "tick", constant = @Constant(doubleValue = 0.05))
    private double injectedReturnSpeed(double returnSpeed) {
        return 0.1;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;tick()V"))
    private void redirectedSuperTick(PersistentProjectileEntity persistentProjectileEntity) {
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        World world = this.getWorld();
        this.setSound(SoundEvents.ITEM_TRIDENT_HIT_GROUND);
        if (world.isClient) return;
        Entity owner = this.getOwner() != null ? this.getOwner() : this;
        BlockPos blockPos = this.getBlockPos();
        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld());
        if (world instanceof ServerWorld && this.getWorld().isRaining() && ((TridentEntity)(Object)this).hasChanneling() && world.isSkyVisible(blockPos) && lightningEntity != null) {
            world.setThunderGradient(1.0f);
            lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
            lightningEntity.setChanneler(owner instanceof ServerPlayerEntity ? (ServerPlayerEntity)owner : null);
            world.spawnEntity(lightningEntity);
            this.playSound(SoundEvents.ITEM_TRIDENT_THUNDER, 5.0f, 1.0f);
        }
    }

    @ModifyVariable(method = "onEntityHit", at = @At("STORE"), ordinal = 0)
    private float injectedAttackDamage(float f, EntityHitResult entityHitResult) {
        float entityImpactFactor = this.dealtDamage ? this.impactFactor : Math.max(this.impactFactor, 1.0f);
        float baseDamage = 9.0f*entityImpactFactor;
        Entity targetEntity = entityHitResult.getEntity();
        float additionalDamage = EnchantmentHelper.getAttackDamage(this.getItemStack(), targetEntity.getType());
        if (targetEntity.isWet() && !targetEntity.getType().isIn(EntityTypeTags.SENSITIVE_TO_IMPALING)) {
            additionalDamage += 1.0f*EnchantmentHelper.getLevel(Enchantments.IMPALING, this.getItemStack());
        }
        return baseDamage + additionalDamage;
    }

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;onHit(Lnet/minecraft/entity/LivingEntity;)V"))
    private void injectedOnEntityHit(EntityHitResult entityHitResult, CallbackInfo cbi) {
        Entity entity = entityHitResult.getEntity();
        if (entity!= null && this.hasSkewering()) {
            Vec3d vec3d = this.getPos();
            Vec3d velocity3d = this.getVelocity();
            Vec3d vec3d2 = vec3d.add(velocity3d);
            entity.setPos(vec3d2.x, entity.getY(), vec3d2.z);
            entity.setVelocity(velocity3d);
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
            if (this.hasSkewering() && iLivingEntity.canBeSkewered()) {
                iLivingEntity.setIsSkewered(true);
                iLivingEntity.setSkeweringEntity((TridentEntity)(Object)this);
                this.playSound(ModSoundEvents.SPEAR_SKEWERING, 1.0f, 1.0f);
                if (this.getOwner() instanceof ServerPlayerEntity) {
                    DamageSource damageSource = this.getDamageSources().trident(this, (this.getOwner() == null ? this : this.getOwner()));
                    float f = 9.0f + EnchantmentHelper.getAttackDamage(this.getItemStack(), entity.getType());
                    ModCriteria.PLAYER_SKEWERED_ENTITY.trigger((ServerPlayerEntity)this.getOwner(), entity, damageSource, f, f, false);
                }
            }
        }
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void redirectedSetVelocity(TridentEntity tridentEntity, Vec3d originalVelocity) {
        if (!this.hasSkewering() || this.getLoyalty() > 0) {
            this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        }
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isThundering()Z"))
    private boolean redirectedChannelingCondition(World world) {
        if (this.getWorld().isRaining()) {
            this.getWorld().setThunderGradient(1.0f);
        }
        return this.getWorld().isRaining();
    }

    public boolean hasSkewering() {
        return EnchantmentHelper.getLevel(ModEnchantments.SKEWERING, this.getItemStack()) > 0;
    }

    @Override
    public int getInGroundTime() {
        return this.inGroundTime;
    }

    @Override
    public byte getLoyalty() {
        return this.dataTracker.get(LOYALTY);
    }

    @Override
    protected void tickInVoid() {
        if (this.dataTracker.get(LOYALTY) > 0 && this.invokeIsOwnerAlive()) {
            this.inGroundTime = 10;
        }
        else {
            this.discard();
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.dataTracker.set(LOYALTY, nbt.getByte("Loyalty"));
        this.dataTracker.set(SKEWERING, nbt.getByte("Skewering"));
        this.dataTracker.set(ENCHANTED, nbt.getBoolean("Enchanted"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo cbi) {
        nbt.putByte("Loyalty", this.getLoyalty());
        nbt.putByte("Skewering", this.dataTracker.get(SKEWERING));
        nbt.putBoolean("Enchanted", ((TridentEntity)(Object)this).isEnchanted());
    }
    
    @Inject(method = "age", at = @At("TAIL"))
    private void injectedAge(CallbackInfo cbi) {
        byte i = this.dataTracker.get(LOYALTY);
        if (this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED || (i > 0 && this.inGround)) {
            if (this.random.nextInt((int)i + 1) == 0) {
                super.age();
            }
        }
    }

    @ModifyConstant(method = "getDragInWater", constant = @Constant(floatValue = 0.99f))
    private float modifiedDragInWater(float originalf) {
        return 0.999f;
    }


}
