package solipingen.sassot.mixin.entity.passive;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.InteractionObserver;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.CelebrateRaidWinTask;
import net.minecraft.entity.ai.brain.task.EndRaidTask;
import net.minecraft.entity.ai.brain.task.FindWalkTargetTask;
import net.minecraft.entity.ai.brain.task.LookAtMobTask;
import net.minecraft.entity.ai.brain.task.RandomTask;
import net.minecraft.entity.ai.brain.task.SeekSkyTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.ai.brain.task.Tasks;
import net.minecraft.entity.ai.brain.task.VillagerTaskListProvider;
import net.minecraft.entity.ai.brain.task.VillagerWalkTowardsTask;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.DefaultAttributeContainer.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.village.TradeOffers.Factory;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.entity.ai.goal.VillagerMeleeAttackGoal;
import solipingen.sassot.entity.ai.goal.VillagerSpearThrowAttackGoal;
import solipingen.sassot.entity.ai.goal.VillagerTrackTargetGoal;
import solipingen.sassot.entity.passive.AngerableFighterVillager;
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
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.SpearItem;
import solipingen.sassot.registry.tag.ModEntityTypeTags;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.village.ModVillagerProfessions;


@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements AngerableFighterVillager, InteractionObserver, SpearThrowingMob, VillagerDataContainer {
    @Shadow public abstract VillagerData getVillagerData();

    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(60, 90);
    private int angerTime;
    @Nullable private UUID angryAt;
    private final VillagerTrackTargetGoal villagerTrackTargetGoal = new VillagerTrackTargetGoal((VillagerEntity)(Object)this);
    private final VillagerMeleeAttackGoal meleeAttackGoal = new VillagerMeleeAttackGoal((VillagerEntity)(Object)this);
    private final VillagerSpearThrowAttackGoal spearThrowAttackGoal = new VillagerSpearThrowAttackGoal(this, 0.67, 40 - 5*(this.getWorld().getDifficulty().getId() - 1), 10.0f);


    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "reinitializeBrain", at = @At("TAIL"))
    private void injectedReinitializeBrain(ServerWorld world, CallbackInfo cbi) {
        VillagerProfession villagerProfession = this.getVillagerData().getProfession();
        if (villagerProfession == ModVillagerProfessions.SWORDSMAN) {
            this.initSwordsmanGoals();
            if (this.getMainHandStack().isEmpty()) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
            }
            if (this.getVillagerData().getLevel() > 1) {
                this.initFighterAttackDamageAddition();
            }
        }
        else if (villagerProfession == ModVillagerProfessions.SPEARMAN) {
            this.initSpearmanGoals();
            if (this.getMainHandStack().isEmpty()) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WOODEN_SPEAR));
                if (this.getVillagerData().getType() == VillagerType.JUNGLE && this.random.nextInt(5) == 0) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.BAMBOO_SPEAR));
                }
            }
            if (this.getVillagerData().getLevel() > 1) {
                this.initFighterAttackDamageAddition();
            }
        }
    }

    @Inject(method = "initBrain", at = @At("HEAD"), cancellable = true)
    private void injectedSetTaskList(Brain<VillagerEntity> brain, CallbackInfo cbi) {
        VillagerProfession villagerProfession = this.getVillagerData().getProfession();
        if (villagerProfession == ModVillagerProfessions.SWORDSMAN || villagerProfession == ModVillagerProfessions.SPEARMAN) {
            brain.setSchedule(ModVillagerProfessions.VILLAGER_SCHEDULE_FIGHTER);
            brain.setTaskList(Activity.WORK, VillagerTaskListProvider.createWorkTasks(villagerProfession, 0.5f), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT)));
            brain.setTaskList(Activity.CORE, VillagerTaskListProvider.createCoreTasks(villagerProfession, 0.5f));
            brain.setTaskList(Activity.MEET, VillagerTaskListProvider.createMeetTasks(villagerProfession, 0.5f), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryModuleState.VALUE_PRESENT)));
            if (this.getFighterTarget() == null && (this.random.nextInt(4) > this.getWorld().getDifficulty().getId() - 1 || this.getHealth() <= this.getMaxHealth()/2.0f)) {
                brain.setTaskList(Activity.REST, VillagerTaskListProvider.createRestTasks(villagerProfession, 0.5f));
            }
            if (this.getFighterTarget() == null || (this.getFighterTarget() != null && !this.getFighterTarget().isAlive())) {
                brain.setTaskList(Activity.IDLE, VillagerTaskListProvider.createIdleTasks(villagerProfession, 0.5f));
            }
            brain.setTaskList(Activity.PRE_RAID, VillagerTaskListProvider.createPreRaidTasks(villagerProfession, 0.5f));
            if (this.getWorld() instanceof ServerWorld) {
                brain.setTaskList(Activity.RAID, VillagerEntityMixin.createFighterRaidTasks((ServerWorld)this.getWorld(), ((VillagerEntity)(Object)this), 0.67f));
            }
            brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
            brain.setDefaultActivity(Activity.IDLE);
            brain.doExclusively(Activity.IDLE);
            brain.refreshActivities(this.getWorld().getTimeOfDay(), this.getWorld().getTime());
            cbi.cancel();
        }
    }

    @Unique
    private void initSwordsmanGoals() {
        super.initGoals();
        World world = this.getWorld();
        if (world == null || world.isClient) {
            return;
        }
        this.goalSelector.remove(this.meleeAttackGoal);
        this.goalSelector.remove(this.spearThrowAttackGoal);
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty()) {
            this.goalSelector.add(3, this.meleeAttackGoal);
        }
        this.targetSelector.add(1, this.villagerTrackTargetGoal);
        this.targetSelector.add(2, new RevengeGoal(this, MerchantEntity.class, IronGolemEntity.class).setGroupRevenge(new Class[0]));
        this.targetSelector.add(3, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, false, this::shouldFighterAngerAt));
        this.targetSelector.add(3, new ActiveTargetGoal<LivingEntity>((MobEntity)this, LivingEntity.class, true, this::shouldBeTargetedMob));
    }

    @Unique
    private void initSpearmanGoals() {
        super.initGoals();
        World world = this.getWorld();
        if (world == null || world.isClient) {
            return;
        }
        this.goalSelector.remove(this.meleeAttackGoal);
        this.goalSelector.remove(this.spearThrowAttackGoal);
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty()) {
            this.goalSelector.add(3, this.spearThrowAttackGoal);
        }
        this.targetSelector.add(1, this.villagerTrackTargetGoal);
        this.targetSelector.add(2, new RevengeGoal(this, MerchantEntity.class, IronGolemEntity.class).setGroupRevenge(new Class[0]));
        this.targetSelector.add(3, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, false, this::shouldFighterAngerAt));
        this.targetSelector.add(3, new ActiveTargetGoal<LivingEntity>((MobEntity)this, LivingEntity.class, true, this::shouldBeTargetedMob));
    }

    @Unique
    private static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> createFighterRaidTasks(ServerWorld world, VillagerEntity villager, float speed) {
        return ImmutableList.of(Pair.of(0, TaskTriggerer.runIf(TaskTriggerer.predicate(VillagerEntityMixin::wonRaid), Tasks.pickRandomly(ImmutableList.of(Pair.of(SeekSkyTask.create(speed), 5), Pair.of(FindWalkTargetTask.create(speed * 1.1f), 2))))), 
            Pair.of(0, new CelebrateRaidWinTask(600, 600)), 
            Pair.of(2, TaskTriggerer.runIf(TaskTriggerer.predicate(VillagerEntityMixin::hasActiveRaid), VillagerWalkTowardsTask.create(MemoryModuleType.JOB_SITE, speed, 1, 100, 1200))), 
            VillagerEntityMixin.createBusyFollowTask(), Pair.of(99, EndRaidTask.create()));
    }

    @Unique
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Pair<Integer, Task<LivingEntity>> createBusyFollowTask() {
        return Pair.of(5, new RandomTask(ImmutableList.of(Pair.of(LookAtMobTask.create(EntityType.VILLAGER, 8.0f), 2), Pair.of(LookAtMobTask.create(EntityType.PLAYER, 8.0f), 2), Pair.of(new WaitTask(30, 60), 8))));
    }

    @Unique
    private static boolean hasActiveRaid(ServerWorld world, LivingEntity entity) {
        Raid raid = world.getRaidAt(entity.getBlockPos());
        return raid != null && raid.isActive() && !raid.hasWon() && !raid.hasLost();
    }

    @Unique
    private static boolean wonRaid(ServerWorld world, LivingEntity livingEntity) {
        Raid raid = world.getRaidAt(livingEntity.getBlockPos());
        return raid != null && raid.hasWon();
    }
    
    @Unique
    private void initFighterAttackDamageAddition() {
        long levelledAttackDamageAddition = Math.round(Math.pow(1.5, this.getVillagerData().getLevel()));
        EntityAttributeModifier attackDamageModifier = new EntityAttributeModifier("Villager attack damage bonus", (double)levelledAttackDamageAddition, EntityAttributeModifier.Operation.ADD_VALUE);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).clearModifiers();
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(attackDamageModifier);
    }

    @Inject(method = "createVillagerAttributes", at = @At("HEAD"), cancellable = true)
    private static void injectedDefaultAttributeAdd(CallbackInfoReturnable<Builder> cbireturn) {
        cbireturn.setReturnValue(MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
        );
    }

    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/VillagerEntity;getOffers()Lnet/minecraft/village/TradeOfferList;"), cancellable = true)
    private void injectedInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        World world = this.getWorld();
        if (world instanceof ServerWorld && this.getVillagerData().getProfession() == ModVillagerProfessions.SWORDSMAN) {
            ItemStack itemStack = player.getStackInHand(hand);
            ItemStack villagerStack = this.getMainHandStack();
            if (itemStack.getItem() instanceof SwordItem && this.isAlive() && !this.hasCustomer() && !this.isSleeping()) {
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
                ItemUsage.exchangeStack(itemStack, player, villagerStack);
                ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)world);
                world.sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
                this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
                cbireturn.setReturnValue(ActionResult.success(world.isClient));
            }
        }
        else if (world instanceof ServerWorld && this.getVillagerData().getProfession() == ModVillagerProfessions.SPEARMAN) {
            ItemStack itemStack = player.getStackInHand(hand);
            ItemStack villagerStack = this.getMainHandStack();
            if (itemStack.getItem() instanceof SpearItem && this.isAlive() && !this.hasCustomer() && !this.isSleeping()) {
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
                ItemUsage.exchangeStack(itemStack, player, villagerStack);
                ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)world);
                world.sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
                this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
                cbireturn.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }

    @Inject(method = "onInteractionWith", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/VillagerEntity;gossip:Lnet/minecraft/village/VillagerGossips;", opcode = Opcodes.GETFIELD))
    private void onInteractionWith(EntityInteraction interaction, Entity entity, CallbackInfo cbi) {
        if (interaction == EntityInteraction.VILLAGER_HURT) {
            this.setFighterAngryAt(entity.getUuid());
            List<Entity> hurtNearbyEntities = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(this.isBaby() ? 16.0 : 8.0), (currentEntity) -> currentEntity.isAlive());
            for (Entity nearbyEntity : hurtNearbyEntities) {
                if (nearbyEntity instanceof VillagerEntity) {
                    ((AngerableFighterVillager)nearbyEntity).setFighterAngryAt(entity.getUuid());
                }
                else if (nearbyEntity instanceof IronGolemEntity) {
                    ((Angerable)nearbyEntity).setAngryAt(entity.getUuid());
                }
            }
        }
        else if (interaction == EntityInteraction.VILLAGER_KILLED) {
            List<Entity> killedNearbyEntities = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(this.isBaby() ? 32.0 : 16.0), (currentEntity) -> currentEntity.isAlive());
            for (Entity nearbyEntity : killedNearbyEntities) {
                if (nearbyEntity instanceof VillagerEntity) {
                    ((AngerableFighterVillager)nearbyEntity).setFighterAngryAt(entity.getUuid());
                }
                else if (nearbyEntity instanceof IronGolemEntity) {
                    ((Angerable)nearbyEntity).setAngryAt(entity.getUuid());
                }
            }
        }
    }

    @Inject(method = "mobTick", at = @At("TAIL"))
    private void injectedMobTick(CallbackInfo cbi) {
        World world = this.getWorld();
        if (!world.isClient) {
            this.tickFighterAngerLogic((ServerWorld)this.getWorld(), true);
        }
        VillagerProfession villagerProfession = this.getVillagerData().getProfession();
        long currentTimeOfDay = this.getWorld().getTimeOfDay();
        boolean timeOfDayBl = currentTimeOfDay > 60l && currentTimeOfDay%12000l >= 0 && currentTimeOfDay%12000l < 60l;
        if (timeOfDayBl && (villagerProfession == ModVillagerProfessions.SWORDSMAN || villagerProfession == ModVillagerProfessions.SPEARMAN)) {
            ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)this.getWorld());
        }
    }

    @Inject(method = "levelUp", at = @At("TAIL"))
    private void injectedLevelUp(CallbackInfo cbi) {
        VillagerProfession profession = this.getVillagerData().getProfession();
        if (this.getWorld() instanceof ServerWorld && (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN)) {
            ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)this.getWorld());
        }
    }

    @ModifyVariable(method = "fillRecipes", at = @At("STORE"), ordinal = 0)
    private Int2ObjectMap<TradeOffers.Factory[]> modifiedFilledRecipes(Int2ObjectMap<TradeOffers.Factory[]> int2ObjectMap) {
        VillagerData villagerData = this.getVillagerData();
        Map<VillagerProfession, Int2ObjectMap<Factory[]>> tradeOffers = TradeOffers.PROFESSION_TO_LEVELED_TRADE;
        if (villagerData.getProfession() == VillagerProfession.FISHERMAN) {
            ModVillagerProfessions.replaceFishermanProfessionToLeveledTrade(tradeOffers);
        }
        else if (villagerData.getProfession() == VillagerProfession.TOOLSMITH) {
            ModVillagerProfessions.replaceToolsmithProfessionToLeveledTrade(tradeOffers);
        }
        else if (villagerData.getProfession() == VillagerProfession.WEAPONSMITH) {
            ModVillagerProfessions.replaceWeaponsmithProfessionToLeveledTrade(tradeOffers);
            if (this.getWorld().getEnabledFeatures().contains(FeatureFlags.TRADE_REBALANCE)) {
                ModVillagerProfessions.replaceRebalancedWeaponsmithProfessionToLeveledTrade(tradeOffers);
            }
        }
        return tradeOffers.get(villagerData.getProfession());
    }

    @Override
    public void spearAttack(LivingEntity target, float pullProgress) {
        int level = this.getVillagerData().getLevel();
        ItemStack spearStack = this.getMainHandStack();
        SpearEntity spearEntity = new WoodenSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.WOODEN_SPEAR));
        int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        float speed = WoodenSpearEntity.SPEED + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        if (spearStack.isOf(ModItems.BAMBOO_SPEAR)) {
            spearEntity = new BambooSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.BAMBOO_SPEAR));
            speed = BambooSpearEntity.SPEED + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.STONE_SPEAR)) {
            spearEntity = new StoneSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.STONE_SPEAR));
            speed = StoneSpearEntity.SPEED + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.FLINT_SPEAR)) {
            spearEntity = new FlintSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.FLINT_SPEAR));
            speed = FlintSpearEntity.SPEED + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.COPPER_SPEAR)) {
            spearEntity = new CopperSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.COPPER_SPEAR));
            speed = CopperSpearEntity.SPEED + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.GOLDEN_SPEAR)) {
            spearEntity = new GoldenSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.GOLDEN_SPEAR));
            speed = GoldenSpearEntity.SPEED + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.IRON_SPEAR)) {
            spearEntity = new IronSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.IRON_SPEAR));
            speed = IronSpearEntity.SPEED + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.DIAMOND_SPEAR)) {
            spearEntity = new DiamondSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.DIAMOND_SPEAR));
            speed = DiamondSpearEntity.SPEED + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.NETHERITE_SPEAR)) {
            spearEntity = new NetheriteSpearEntity(this.getWorld(), (LivingEntity)this, new ItemStack(ModItems.NETHERITE_SPEAR));
            speed = NetheriteSpearEntity.SPEED + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - spearEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        spearEntity.setVelocity(d, e + g * 0.15, f, speed, 14.0f - this.getWorld().getDifficulty().getId() * 4.0f - 0.15f*level);
        this.playSound(ModSoundEvents.VILLAGER_SPEAR_THROW, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.getWorld().spawnEntity(spearEntity);
    }

    @Inject(method = "talkWithVillager", at = @At("TAIL"), cancellable = true)
    private void injectedTalkWithVillager(ServerWorld world, VillagerEntity villager, long time, CallbackInfo cbi) {
        if (villager.getVillagerData().getProfession() == VillagerProfession.WEAPONSMITH) {
            if (this.getMainHandStack().hasEnchantments()) {
                cbi.cancel();
            }
            int level = this.getVillagerData().getLevel();
            int weaponsmithLevel = villager.getVillagerData().getLevel();
            if (this.getVillagerData().getProfession() == ModVillagerProfessions.SWORDSMAN
                    && !(this.getMainHandStack().hasEnchantments() || this.getMainHandStack().get(DataComponentTypes.CUSTOM_NAME) != null)) {
                if (level >= 1 && weaponsmithLevel >= 1) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
                if (level >= 2 && weaponsmithLevel >= 2) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SWORD));
                }
                if (level >= 3 && weaponsmithLevel >= 3) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                }
                if (level >= 4 && weaponsmithLevel >= 4) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                }
                if (level == 5 && weaponsmithLevel == 5) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                }
            }
            else if (this.getVillagerData().getProfession() == ModVillagerProfessions.SPEARMAN
                    && !(this.getMainHandStack().hasEnchantments() || this.getMainHandStack().get(DataComponentTypes.CUSTOM_NAME) != null)) {
                if (level >= 1 && weaponsmithLevel >= 1) {
                    int randomInt = this.random.nextInt(3);
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
                    if (randomInt == 0) {
                        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FLINT_SPEAR));
                    }
                }
                if (level >= 2 && weaponsmithLevel >= 2) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SPEAR));
                }
                if (level >= 3 && weaponsmithLevel >= 3) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLDEN_SPEAR));
                }
                if (level >= 4 && weaponsmithLevel >= 4) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR));
                }
                if (level == 5 && weaponsmithLevel == 5) {
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.DIAMOND_SPEAR));
                }
            }
        }
    }

    @Override
    public void chooseRandomFighterAngerTime() {
        this.setFighterAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    @Override
    public void setFighterAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    public int getFighterAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setFighterAngryAt(@Nullable UUID angryAt) {
        VillagerProfession profession = this.getVillagerData().getProfession();
        if (this.getWorld() instanceof ServerWorld && (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN)) {
            if (angryAt != null && this.getFighterAngryAt() == angryAt) return;
            this.angryAt = angryAt;
            ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)this.getWorld());
        }
    }

    @Override
    @Nullable
    public UUID getFighterAngryAt() {
        return this.angryAt;
    }

    @Override
    public boolean canFighterTarget(LivingEntity target) {
        return this.shouldBeTargetedMob(target);
    }

    @Override
    @Nullable
    public LivingEntity getFighterTarget() {
        return this.getTarget();
    }

    @Override
    public void setFighterTarget(@Nullable LivingEntity target) {
        this.setTarget(target);
    }

    @Override
    @Nullable
    public LivingEntity getFighterAttacker() {
        return this.getAttacker();
    }

    @Override
    public void setFighterAttacker(@Nullable LivingEntity attacker) {
        this.setAttacker(attacker);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.writeFighterAngerToNbt(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.readFighterAngerFromNbt(this.getWorld(), nbt);
    }

    @Unique
    private boolean shouldBeTargetedMob(LivingEntity livingEntity) {
        VillagerData villagerData = this.getVillagerData();
        VillagerProfession profession = villagerData.getProfession();
        if (profession == ModVillagerProfessions.SPEARMAN) {
            if (livingEntity instanceof VexEntity vexEntity && vexEntity.getOwner() != null) {
                return vexEntity.getOwner().getType().isIn(ModEntityTypeTags.SPEARMAN_VILLAGER_TARGETS);
            }
            return livingEntity.getType().isIn(ModEntityTypeTags.SPEARMAN_VILLAGER_TARGETS);
        }
        else if (profession == ModVillagerProfessions.SWORDSMAN) {
            if (livingEntity instanceof VexEntity vexEntity && vexEntity.getOwner() != null) {
                return vexEntity.getOwner().getType().isIn(ModEntityTypeTags.SWORDSMAN_VILLAGER_TARGETS);
            }
            return livingEntity.getType().isIn(ModEntityTypeTags.SWORDSMAN_VILLAGER_TARGETS);
        }
        return false;
    }


    
}
