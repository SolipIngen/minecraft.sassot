package solipingen.sassot.entity.passive;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;


public interface AngerableFighterVillager {
    public static final String ANGER_TIME_KEY = "FighterAngerTime";
    public static final String ANGRY_AT_KEY = "FighterAngryAt";

    
    public int getFighterAngerTime();

    public void setFighterAngerTime(int var1);

    @Nullable
    public UUID getFighterAngryAt();

    public void setFighterAngryAt(@Nullable UUID var1);

    public void chooseRandomFighterAngerTime();

    default public void writeFighterAngerToData(WriteView view) {
        view.putInt(ANGER_TIME_KEY, this.getFighterAngerTime());
        view.putNullable(ANGRY_AT_KEY, Uuids.INT_STREAM_CODEC, this.getFighterAngryAt());
    }

    default public void readFighterAngerFromData(World world, ReadView view) {
        this.setFighterAngerTime(view.getInt(ANGER_TIME_KEY, 0));
        if (!(world instanceof ServerWorld)) {
            return;
        }
        if (this.getFighterAngerTime() <= 0) {
            this.setFighterAngryAt(null);
            return;
        }
        UUID uUID = view.read(ANGRY_AT_KEY, Uuids.INT_STREAM_CODEC).orElse(null);
        this.setFighterAngryAt(uUID);
        Entity entity = world.getEntity(uUID);
        if (entity == null) {
            return;
        }
        if (entity instanceof MobEntity) {
            this.setFighterAttacker((MobEntity)entity);
        }
        if (entity instanceof PlayerEntity) {
            this.setFighterAttacking((PlayerEntity)entity);
        }
    }

    /**
     * @param angerPersistent if {@code true}, the anger time will not decrease for a player target
     */
    default public void tickFighterAngerLogic(ServerWorld world, boolean angerPersistent) {
        LivingEntity livingEntity = this.getFighterTarget();
        UUID uUID = this.getFighterAngryAt();
        if ((livingEntity == null || livingEntity.isDead()) && uUID != null && world.getEntity(uUID) instanceof MobEntity) {
            this.stopFighterAnger();
            return;
        }
        if (livingEntity != null && !Objects.equals(uUID, livingEntity.getUuid())) {
            this.setFighterAngryAt(livingEntity.getUuid());
            this.chooseRandomFighterAngerTime();
        }
        if (!(this.getFighterAngerTime() <= 0 || livingEntity != null && livingEntity.getType() == EntityType.PLAYER && angerPersistent)) {
            this.setFighterAngerTime(this.getFighterAngerTime() - 1);
            if (this.getFighterAngerTime() == 0) {
                this.stopFighterAnger();
            }
        }
        if (livingEntity != null && livingEntity instanceof PlayerEntity && (livingEntity.isSpectator() || ((PlayerEntity)livingEntity).isCreative())) {
            this.stopFighterAnger();
        }
    }

    default public boolean shouldFighterAngerAt(LivingEntity entity, ServerWorld world) {
        if (!this.canFighterTarget(entity)) {
            return false;
        }
        if (entity.getType() == EntityType.PLAYER && this.isUniversallyFighterAngry(world)) {
            return true;
        }
        return entity.getUuid().equals(this.getFighterAngryAt());
    }

    default public boolean isUniversallyFighterAngry(ServerWorld world) {
        return world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER) && this.hasFighterAngerTime() && this.getFighterAngryAt() == null;
    }

    default public boolean hasFighterAngerTime() {
        return this.getFighterAngerTime() > 0;
    }

    default public void forgive(PlayerEntity player) {
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            if (!serverWorld.getGameRules().getBoolean(GameRules.FORGIVE_DEAD_PLAYERS)) {
                return;
            }
            if (!player.getUuid().equals(this.getFighterAngryAt())) {
                return;
            }
            this.stopFighterAnger();
        }
    }

    default public void universallyFighterAnger() {
        this.stopFighterAnger();
        this.chooseRandomFighterAngerTime();
    }

    default public void stopFighterAnger() {
        this.setFighterAttacker(null);
        this.setFighterAngryAt(null);
        this.setFighterTarget(null);
        this.setFighterAngerTime(0);
    }

    @Nullable
    public LivingEntity getFighterAttacker();

    public void setFighterAttacker(@Nullable LivingEntity var1);

    public void setFighterAttacking(@Nullable PlayerEntity var1);

    public void setFighterTarget(@Nullable LivingEntity var1);

    public boolean canFighterTarget(LivingEntity var1);

    @Nullable
    public LivingEntity getFighterTarget();
}
