package solipingen.sassot.mixin.entity.passive;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;
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
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.DefaultAttributeContainer.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
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
import net.minecraft.world.World;
import solipingen.sassot.entity.ai.SpearThrowingMob;
import solipingen.sassot.entity.ai.goal.SpearThrowAttackGoal;
import solipingen.sassot.entity.ai.goal.VillagerTrackTargetGoal;
import solipingen.sassot.entity.passive.AngerableVillager;
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
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.village.ModVillagerProfessions;


@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements AngerableVillager, InteractionObserver, SpearThrowingMob, VillagerDataContainer {
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(60, 90);
    private int angerTime;
    @Nullable private UUID angryAt;
    private final VillagerTrackTargetGoal villagerTrackTargetGoal = new VillagerTrackTargetGoal((VillagerEntity)(Object)this);
    private final VillagerMeleeAttackGoal meleeAttackGoal = new VillagerMeleeAttackGoal((VillagerEntity)(Object)this);
    private final VillagerSpearThrowAttackGoal spearThrowAttackGoal = new VillagerSpearThrowAttackGoal(this, 0.67, 40 - 5*(this.world.getDifficulty().getId() - 1), 10.0f);


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

    @Redirect(method = "initBrain", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/Brain;setTaskList(Lnet/minecraft/entity/ai/brain/Activity;Lcom/google/common/collect/ImmutableList;)V"))
    private void redirectedSetTaskList(Brain<VillagerEntity> brain, Activity activity, ImmutableList<? extends Pair<Integer, ? extends Task<? super VillagerEntity>>> indexedTasks) {
        VillagerProfession villagerProfession = this.getVillagerData().getProfession();
        if (villagerProfession == ModVillagerProfessions.SWORDSMAN || villagerProfession == ModVillagerProfessions.SPEARMAN) {
            if (activity == Activity.REST) {
                if (this.getTarget() == null && this.random.nextInt(4) > this.world.getDifficulty().getId() - 1) {
                    brain.setTaskList(activity, indexedTasks);
                }
            }
            if (!(activity == Activity.PANIC || activity == Activity.HIDE || activity == Activity.REST)) {
                if (activity == Activity.IDLE) {
                    if (this.getTarget() == null || (this.getTarget() != null && !this.getTarget().isAlive())) {
                        brain.setTaskList(activity, indexedTasks);
                    }
                }
                else {
                    brain.setTaskList(activity, indexedTasks);
                }
            }
        }
        else {
           brain.setTaskList(activity, indexedTasks);
        }
    }

    private void initSwordsmanGoals() {
        super.initGoals();
        if (this.world == null || this.world.isClient) {
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
        this.targetSelector.add(3, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, false, this::shouldAngerAt));
        this.targetSelector.add(3, new ActiveTargetGoal<RaiderEntity>((MobEntity)this, RaiderEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<ZombieEntity>((MobEntity)this, ZombieEntity.class, true, this::shouldBeTargetedMob));
        this.targetSelector.add(3, new ActiveTargetGoal<VexEntity>((MobEntity)this, VexEntity.class, false, this::shouldBeTargetedMob));
    }

    private void initSpearmanGoals() {
        super.initGoals();
        if (this.world == null || this.world.isClient) {
            return;
        }
        this.goalSelector.remove(this.meleeAttackGoal);
        this.goalSelector.remove(this.spearThrowAttackGoal);
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty()) {
            this.goalSelector.add(3, this.spearThrowAttackGoal);
        }
        else {
            this.goalSelector.add(3, this.meleeAttackGoal);
        }
        this.targetSelector.add(1, this.villagerTrackTargetGoal);
        this.targetSelector.add(2, new RevengeGoal(this, MerchantEntity.class, IronGolemEntity.class).setGroupRevenge(new Class[0]));
        this.targetSelector.add(3, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, false, this::shouldAngerAt));
        this.targetSelector.add(3, new ActiveTargetGoal<RaiderEntity>((MobEntity)this, RaiderEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<ZombieEntity>((MobEntity)this, ZombieEntity.class, true, this::shouldBeTargetedMob));
        this.targetSelector.add(3, new ActiveTargetGoal<VexEntity>((MobEntity)this, VexEntity.class, false, this::shouldBeTargetedMob));
    }
    
    private void initFighterAttackDamageAddition() {
        long levelledAttackDamageAddition = Math.round(Math.pow(1.5, this.getVillagerData().getLevel()));
        EntityAttributeModifier attackDamageModifier = new EntityAttributeModifier("levelled_attack_damage", (double)levelledAttackDamageAddition, EntityAttributeModifier.Operation.ADDITION);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).clearModifiers();
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(attackDamageModifier);
    }

    @Redirect(method = "createVillagerAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;add(Lnet/minecraft/entity/attribute/EntityAttribute;D)Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;"))
    private static Builder redirectedDefaultAttributeAdd(Builder builder, EntityAttribute attribute, double baseValue) {
        if (attribute == EntityAttributes.GENERIC_FOLLOW_RANGE) {
            return builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0).add(attribute, baseValue);
        }
        return builder.add(attribute, baseValue);
    }

    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/VillagerEntity;getOffers()Lnet/minecraft/village/TradeOfferList;"), cancellable = true)
    private void injectedInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cbireturn) {
        if (this.world instanceof ServerWorld && this.getVillagerData().getProfession() == ModVillagerProfessions.SWORDSMAN) {
            ItemStack itemStack = player.getStackInHand(hand);
            ItemStack villagerStack = this.getMainHandStack();
            if (itemStack.getItem() instanceof SwordItem && this.isAlive() && !this.hasCustomer() && !this.isSleeping()) {
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
                ItemUsage.exchangeStack(itemStack, player, villagerStack);
                ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)this.world);
                this.world.sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
                this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
                cbireturn.setReturnValue(ActionResult.success(this.world.isClient));
            }
        }
        else if (this.world instanceof ServerWorld && this.getVillagerData().getProfession() == ModVillagerProfessions.SPEARMAN) {
            ItemStack itemStack = player.getStackInHand(hand);
            ItemStack villagerStack = this.getMainHandStack();
            if (itemStack.getItem() instanceof SpearItem && this.isAlive() && !this.hasCustomer() && !this.isSleeping()) {
                this.equipStack(EquipmentSlot.MAINHAND, itemStack);
                ItemUsage.exchangeStack(itemStack, player, villagerStack);
                ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)this.world);
                this.world.sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
                this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
                cbireturn.setReturnValue(ActionResult.success(this.world.isClient));
            }
        }
    }

    @Inject(method = "onInteractionWith", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/VillagerEntity;gossip:Lnet/minecraft/village/VillagerGossips;", opcode = Opcodes.GETFIELD))
    private void onInteractionWith(EntityInteraction interaction, Entity entity, CallbackInfo cbi) {
        if (interaction == EntityInteraction.VILLAGER_HURT) {
            this.setAngryAt(entity.getUuid());
            List<Entity> hurtNearbyEntities = this.world.getOtherEntities(this, this.getBoundingBox().expand(8.0));
            for (Entity nearbyEntity : hurtNearbyEntities) {
                if (nearbyEntity instanceof VillagerEntity) {
                    ((AngerableVillager)nearbyEntity).setAngryAt(entity.getUuid());
                }
                else if (nearbyEntity instanceof IronGolemEntity) {
                    ((Angerable)nearbyEntity).setAngryAt(entity.getUuid());
                }
            }
        }
        else if (interaction == EntityInteraction.VILLAGER_KILLED) {
            List<Entity> killedNearbyEntities = this.world.getOtherEntities(this, this.getBoundingBox().expand(16.0));
            for (Entity nearbyEntity : killedNearbyEntities) {
                if (nearbyEntity instanceof VillagerEntity) {
                    ((AngerableVillager)nearbyEntity).setAngryAt(entity.getUuid());
                }
                else if (nearbyEntity instanceof IronGolemEntity) {
                    ((Angerable)nearbyEntity).setAngryAt(entity.getUuid());
                }
            }
        }
    }

    @Inject(method = "mobTick", at = @At("TAIL"))
    private void injectedMobTick(CallbackInfo cbi) {
        if (!this.world.isClient) {
            this.tickAngerLogic((ServerWorld)this.world, true);
        }
        VillagerProfession villagerProfession = this.getVillagerData().getProfession();
        long currentTimeOfDay = this.world.getTimeOfDay();
        boolean timeOfDayBl = currentTimeOfDay > 60l && currentTimeOfDay%12000l >= 0 && currentTimeOfDay%12000l < 60l;
        if (timeOfDayBl && (villagerProfession == ModVillagerProfessions.SWORDSMAN || villagerProfession == ModVillagerProfessions.SPEARMAN)) {
            ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)this.world);
        }
    }

    @Inject(method = "levelUp", at = @At("TAIL"))
    private void injectedLevelUp(CallbackInfo cbi) {
        VillagerProfession profession = this.getVillagerData().getProfession();
        if (this.world instanceof ServerWorld && (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN)) {
            ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)this.world);
        }
    }

    @ModifyVariable(method = "fillRecipes", at = @At("STORE"), ordinal = 0)
    private Int2ObjectMap<TradeOffers.Factory[]> modifiedFilledRecipes(Int2ObjectMap<TradeOffers.Factory[]> int2ObjectMap) {
        VillagerData villagerData = this.getVillagerData();
        Map<VillagerProfession, Int2ObjectMap<Factory[]>> tradeOffers = TradeOffers.PROFESSION_TO_LEVELED_TRADE;
        if (villagerData.getProfession() == VillagerProfession.TOOLSMITH) {
            ModVillagerProfessions.replaceToolsmithProfessionToLeveledTrade(tradeOffers);
        }
        else if (villagerData.getProfession() == VillagerProfession.WEAPONSMITH) {
            ModVillagerProfessions.replaceWeaponsmithProfessionToLeveledTrade(tradeOffers);
        }
        return tradeOffers.get(villagerData.getProfession());
    }

    @Override
    public void spearAttack(LivingEntity target, float pullProgress) {
        int level = this.getVillagerData().getLevel();
        ItemStack spearStack = this.getMainHandStack();
        SpearEntity spearEntity = new WoodenSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.WOODEN_SPEAR));
        int strengthLevel = this.hasStatusEffect(StatusEffects.STRENGTH) ? this.getStatusEffect(StatusEffects.STRENGTH).getAmplifier() + 1 : 0;
        int weaknessLevel = this.hasStatusEffect(StatusEffects.WEAKNESS) ? this.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier() + 1 : 0;
        float speed = 1.3f + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        if (spearStack.isOf(ModItems.BAMBOO_SPEAR)) {
            spearEntity = new BambooSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.BAMBOO_SPEAR));
            speed = 1.4f + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.STONE_SPEAR)) {
            spearEntity = new StoneSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.STONE_SPEAR));
            speed = 1.1f + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.FLINT_SPEAR)) {
            spearEntity = new FlintSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.FLINT_SPEAR));
            speed = 1.2f + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.COPPER_SPEAR)) {
            spearEntity = new CopperSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.COPPER_SPEAR));
            speed = 1.3f + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.GOLDEN_SPEAR)) {
            spearEntity = new GoldenSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.GOLDEN_SPEAR));
            speed = 1.3f + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.IRON_SPEAR)) {
            spearEntity = new IronSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.IRON_SPEAR));
            speed = 1.3f + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.DIAMOND_SPEAR)) {
            spearEntity = new DiamondSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.DIAMOND_SPEAR));
            speed = 1.3f + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        else if (spearStack.isOf(ModItems.NETHERITE_SPEAR)) {
            spearEntity = new NetheriteSpearEntity(this.world, (LivingEntity)this, new ItemStack(ModItems.NETHERITE_SPEAR));
            speed = 1.3f + 0.2f*level + 0.2f*strengthLevel - 0.2f*weaknessLevel;
        }
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - spearEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        spearEntity.setVelocity(d, e + g * 0.15, f, speed, 14.0f - this.world.getDifficulty().getId() * 4.0f - 0.15f*level);
        this.playSound(ModSoundEvents.VILLAGER_SPEAR_THROW, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntity(spearEntity);
    }

    @Inject(method = "talkWithVillager", at = @At("TAIL"))
    private void injectedTalkWithVillager(ServerWorld world, VillagerEntity villager, long time, CallbackInfo cbi) {
        if (villager.getVillagerData().getProfession() == VillagerProfession.WEAPONSMITH) {
            if (this.getVillagerData().getProfession() == ModVillagerProfessions.SWORDSMAN) {
                ItemStack mainHandStack = this.getMainHandStack();
                if (mainHandStack.getItem() instanceof SwordItem) {
                    SwordItem swordItem = (SwordItem)mainHandStack.getItem();
                    int level = this.getVillagerData().getLevel();
                    int weaponsmithLevel = villager.getVillagerData().getLevel();
                    int materialLevel = swordItem.getMaterial().getMiningLevel();
                    if (materialLevel < level) {
                        if (level >= 1 && weaponsmithLevel >= 1) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                        }
                    }
                    if (materialLevel < level - 1) {
                        if (level == 2 && weaponsmithLevel >= 2) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SWORD));
                        }
                        if (level == 3 && weaponsmithLevel >= 3) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                        }
                    }
                    if (materialLevel < level - 2) {
                        if (level == 4 && weaponsmithLevel >= 4) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                        }
                        if (level == 5 && weaponsmithLevel == 5) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                        }
                    }
                }
            }
            else if (this.getVillagerData().getProfession() == ModVillagerProfessions.SPEARMAN) {
                ItemStack mainHandStack = this.getMainHandStack();
                if (mainHandStack.getItem() instanceof SpearItem) {
                    SpearItem spearItem = (SpearItem)mainHandStack.getItem();
                    int level = this.getVillagerData().getLevel();
                    int weaponsmithLevel = villager.getVillagerData().getLevel();
                    int materialLevel = spearItem.getMaterial().getMiningLevel();
                    if (materialLevel < level) {
                        if (level >= 1 && weaponsmithLevel >= 1) {
                            int randomInt = this.random.nextInt(3);
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STONE_SPEAR));
                            if (randomInt == 0) {
                                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FLINT_SPEAR));
                            }
                        }
                    }
                    if (materialLevel < level - 1) {
                        if (level >= 2 && weaponsmithLevel >= 2) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COPPER_SPEAR));
                        }
                        if (level >= 3 && weaponsmithLevel >= 3) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLDEN_SPEAR));
                        }
                    }
                    if (materialLevel < level - 2) {
                        if (level >= 4 && weaponsmithLevel >= 4) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR));
                        }
                        if (level >= 5 && weaponsmithLevel >= 5) {
                            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.DIAMOND_SPEAR));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    public int getAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        VillagerProfession profession = this.getVillagerData().getProfession();
        if (this.world instanceof ServerWorld && (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN)) {
            if (angryAt != null && this.getAngryAt() == angryAt) return;
            this.angryAt = angryAt;
            ((VillagerEntity)(Object)this).reinitializeBrain((ServerWorld)this.world);
        }
    }

    @Override
    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    private boolean shouldBeTargetedMob(LivingEntity livingEntity) {
        if (livingEntity instanceof ZombieEntity) {
            return !(livingEntity instanceof ZombifiedPiglinEntity);
        }
        else if (livingEntity instanceof VexEntity) {
            return ((VexEntity)livingEntity).getOwner() instanceof RaiderEntity;
        }
        return false;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectedWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.writeAngerToNbt(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectedReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo cbi) {
        this.readAngerFromNbt(this.world, nbt);
    }


    static class VillagerSpearThrowAttackGoal extends SpearThrowAttackGoal {
        private final VillagerEntity villager;

        public VillagerSpearThrowAttackGoal(SpearThrowingMob spearThrowingMob, double d, int i, float f) {
            super(spearThrowingMob, d, i, f);
            this.villager = (VillagerEntity)spearThrowingMob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && this.villager.getMainHandStack().getItem() instanceof SpearItem;
        }

        @Override
        public void start() {
            super.start();
            this.villager.setAttacking(true);
            this.villager.setCurrentHand(Hand.MAIN_HAND);
            if (this.villager.world instanceof ServerWorld) {
                this.villager.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1.0f, this.villager.getSoundPitch());
            }
        }

        @Override
        public void stop() {
            super.stop();
            VillagerProfession profession = this.villager.getVillagerData().getProfession();
            if (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN) {
                int i = villager.getVillagerData().getLevel();
                boolean levelUpBl = VillagerData.canLevelUp(i) && villager.getExperience() >= VillagerData.getUpperLevelExperience(i);
                if (levelUpBl) {
                    this.villager.setVillagerData(villager.getVillagerData().withLevel(i + 1));
                    this.villager.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1));
                }
            }
            this.villager.clearActiveItem();
            this.villager.setAttacking(false);
        }
    }

    class VillagerMeleeAttackGoal extends MeleeAttackGoal {
        private final VillagerEntity villager;

        public VillagerMeleeAttackGoal(VillagerEntity villager) {
            super(villager, 0.67, false);
            this.villager = villager;
        }

        @Override
        public void start() {
            super.start();
            if (this.villager.world instanceof ServerWorld) {
                this.villager.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1.0f, this.villager.getSoundPitch());
            }
        }

        @Override
        public void stop() {
            super.stop();
            VillagerEntity villager = (VillagerEntity)this.mob;
            VillagerProfession profession = villager.getVillagerData().getProfession();
            if (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN) {
                int i = villager.getVillagerData().getLevel();
                boolean levelUpBl = VillagerData.canLevelUp(i) && villager.getExperience() >= VillagerData.getUpperLevelExperience(i);
                if (levelUpBl) {
                    villager.setVillagerData(villager.getVillagerData().withLevel(i + 1));
                    villager.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1));
                }
            }
            villager.reinitializeBrain((ServerWorld)villager.world);
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return super.getSquaredMaxAttackDistance(entity);
        }
    }

    
}
