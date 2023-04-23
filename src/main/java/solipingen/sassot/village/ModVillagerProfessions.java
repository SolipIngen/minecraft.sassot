package solipingen.sassot.village;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.village.TradeOffers.BuyForOneEmeraldFactory;
import net.minecraft.village.TradeOffers.Factory;
import net.minecraft.village.TradeOffers.SellEnchantedToolFactory;
import net.minecraft.village.TradeOffers.SellItemFactory;
import net.minecraft.village.TradeOffers.TypeAwareBuyForOneEmeraldFactory;
import net.minecraft.world.poi.PointOfInterestType;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.block.ModBlocks;
import solipingen.sassot.item.ModItems;

import org.jetbrains.annotations.Nullable;


public class ModVillagerProfessions {

    public static final VillagerProfession SWORDSMAN = ModVillagerProfessions.registerVillagerProfession("swordsman",
        RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "swordsman_marker_poi")), null, null, null);
    public static final VillagerProfession SPEARMAN = ModVillagerProfessions.registerVillagerProfession("spearman",
        RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "spearman_marker_poi")), null, null, null);

    public static final PointOfInterestType SWORDSMAN_MARKER_POI = ModVillagerProfessions.registerPOI("swordsman_marker_poi", ModBlocks.SWORDSMAN_MARKER);
    public static final PointOfInterestType SPEARMAN_MARKER_POI = ModVillagerProfessions.registerPOI("spearman_marker_poi", ModBlocks.SPEARMAN_MARKER);


    // Trade Offer Replacements
    public static void replaceFishermanProfessionToLeveledTrade(Map<VillagerProfession, Int2ObjectMap<Factory[]>> originalTradeOffers) {
        ImmutableMap.Builder<VillagerType, Item> sellFishMapBuilder = ImmutableMap.builder();
        ImmutableMap<VillagerType, Item> sellFishMap = sellFishMapBuilder.put(VillagerType.PLAINS, Items.COD)
            .put(VillagerType.TAIGA, Items.SALMON)
            .put(VillagerType.SNOW, Items.SALMON)
            .put(VillagerType.DESERT, Items.TROPICAL_FISH)
            .put(VillagerType.JUNGLE, Items.TROPICAL_FISH)
            .put(VillagerType.SAVANNA, Items.COD)
            .put(VillagerType.SWAMP, Items.SALMON).build();
        ImmutableMap.Builder<VillagerType, Item> sellCookedFishMapBuilder = ImmutableMap.builder();
        ImmutableMap<VillagerType, Item> sellCookedFishMap = sellCookedFishMapBuilder.put(VillagerType.PLAINS, Items.COOKED_COD)
            .put(VillagerType.TAIGA, Items.COOKED_SALMON)
            .put(VillagerType.SNOW, Items.COOKED_SALMON)
            .put(VillagerType.DESERT, Items.COOKED_COD)
            .put(VillagerType.JUNGLE, Items.COOKED_SALMON)
            .put(VillagerType.SAVANNA, Items.COOKED_COD)
            .put(VillagerType.SWAMP, Items.COOKED_SALMON).build();
        ImmutableMap.Builder<VillagerType, Item> boatMapBuilder = ImmutableMap.builder();
        ImmutableMap<VillagerType, Item> boatMap = boatMapBuilder.put(VillagerType.PLAINS, Items.OAK_BOAT)
            .put(VillagerType.TAIGA, Items.SPRUCE_BOAT)
            .put(VillagerType.SNOW, Items.SPRUCE_BOAT)
            .put(VillagerType.DESERT, Items.JUNGLE_BOAT)
            .put(VillagerType.JUNGLE, Items.BAMBOO_RAFT)
            .put(VillagerType.SAVANNA, Items.ACACIA_BOAT)
            .put(VillagerType.SWAMP, Items.MANGROVE_BOAT).build();
        ImmutableMap.Builder<VillagerType, Item> sellLiveFishMapBuilder = ImmutableMap.builder();
        ImmutableMap<VillagerType, Item> sellLiveFishMap = sellLiveFishMapBuilder.put(VillagerType.PLAINS, Items.COD_BUCKET)
            .put(VillagerType.TAIGA, Items.SALMON_BUCKET)
            .put(VillagerType.SNOW, Items.SALMON_BUCKET)
            .put(VillagerType.DESERT, Items.TROPICAL_FISH_BUCKET)
            .put(VillagerType.JUNGLE, Items.TROPICAL_FISH_BUCKET)
            .put(VillagerType.SAVANNA, Items.COD_BUCKET)
            .put(VillagerType.SWAMP, Items.TROPICAL_FISH_BUCKET).build();
        originalTradeOffers.replace(VillagerProfession.FISHERMAN, ModVillagerProfessions.copyToFastUtilMap(
            ImmutableMap.of(
                1, new Factory[]{new BuyForOneEmeraldFactory(Items.STRING, 20, 16, 2), new BuyForOneEmeraldFactory(Items.STICK, 32, 16, 2),  
                    new TypeAwareSellForOneEmeraldFactory(4, 16, 1, sellFishMap), 
                    new SellItemFactory(Items.FISHING_ROD, 2, 1, 1)}, 
                2, new Factory[]{new BuyForOneEmeraldFactory(Items.COAL, 20, 16, 10), new BuyForOneEmeraldFactory(Items.CHARCOAL, 20, 16, 10), 
                    new SellItemFactory(Items.CAMPFIRE, 2, 1, 5), 
                    new TypeAwareSellForOneEmeraldFactory(4, 16, 5, sellCookedFishMap), 
                    new SellItemFactory(ModItems.COPPER_FUSED_FISHING_ROD, 4, 1, 5)}, 
                3, new Factory[]{new TypeAwareBuyForOneEmeraldFactory(1, 12, 20, boatMap), 
                    new BuyForOneEmeraldFactory(Items.KELP, 20, 16, 20), 
                    new SellItemFactory(Items.PUFFERFISH, 2, 1, 10), 
                    new SellItemFactory(Items.INK_SAC, 1, 2, 10), 
                    new SellItemFactory(ModItems.GOLD_FUSED_FISHING_ROD, 3, 1, 10)}, 
                4, new Factory[]{new BuyForOneEmeraldFactory(Items.BUCKET, 2, 12, 30), 
                    new SellItemFactory(Items.GLOW_INK_SAC, 2, 2, 15), 
                    new TypeAwareSellForOneEmeraldFactory(1, 16, 15, sellLiveFishMap), 
                    new SellItemFactory(ModItems.IRON_FUSED_FISHING_ROD, 3, 1, 15)}, 
                5, new Factory[]{new SellItemFactory(Items.PUFFERFISH_BUCKET, 4, 1, 15), 
                    new SellEnchantedToolFactory(ModItems.DIAMOND_FUSED_FISHING_ROD, 10, 3, 15)}
            )));
    }

    public static void replaceToolsmithProfessionToLeveledTrade(Map<VillagerProfession, Int2ObjectMap<Factory[]>> originalTradeOffers) {
        originalTradeOffers.replace(VillagerProfession.TOOLSMITH, ModVillagerProfessions.copyToFastUtilMap(
            ImmutableMap.of(
                1, new Factory[]{new BuyForOneEmeraldFactory(Items.COAL, 15, 16, 2), new BuyForOneEmeraldFactory(Items.CHARCOAL, 15, 16, 2),  
                    new SellItemFactory(Items.STONE_PICKAXE, 1, 1, 1), 
                    new SellItemFactory(Items.STONE_SHOVEL, 1, 1, 1), 
                    new SellItemFactory(Items.STONE_HOE, 1, 1, 1), 
                    new SellItemFactory(Items.STONE_AXE, 1, 1, 1)}, 
                2, new Factory[]{new BuyForOneEmeraldFactory(Items.COPPER_INGOT, 8, 12, 10), 
                    new SellItemFactory(ModItems.COPPER_PICKAXE, 2, 1, 5), 
                    new SellItemFactory(ModItems.COPPER_SHOVEL, 2, 1, 5), 
                    new SellItemFactory(ModItems.COPPER_HOE, 2, 1, 5), 
                    new SellItemFactory(ModItems.COPPER_AXE, 2, 1, 5)}, 
                3, new Factory[]{new BuyForOneEmeraldFactory(Items.GOLD_INGOT, 2, 12, 20), 
                    new SellItemFactory(Items.BELL, 18, 1, 10), 
                    new SellItemFactory(Items.GOLDEN_PICKAXE, 3, 1, 10), 
                    new SellItemFactory(Items.GOLDEN_SHOVEL, 3, 1, 10), 
                    new SellItemFactory(Items.GOLDEN_HOE, 3, 1, 10), 
                    new SellItemFactory(Items.GOLDEN_AXE, 3, 1, 10)}, 
                4, new Factory[]{new BuyForOneEmeraldFactory(Items.IRON_INGOT, 4, 12, 30), new BuyForOneEmeraldFactory(Items.LAVA_BUCKET, 1, 12, 30), 
                    new SellItemFactory(Items.IRON_PICKAXE, 3, 1, 15), 
                    new SellItemFactory(Items.IRON_SHOVEL, 3, 1, 15), 
                    new SellItemFactory(Items.IRON_HOE, 3, 1, 15), 
                    new SellItemFactory(Items.IRON_AXE, 3, 1, 15)}, 
                5, new Factory[]{new BuyForOneEmeraldFactory(Items.DIAMOND, 1, 12, 30), 
                    new SellEnchantedToolFactory(Items.DIAMOND_PICKAXE, 18, 3, 15), 
                    new SellEnchantedToolFactory(Items.DIAMOND_SHOVEL, 10, 3, 15), 
                    new SellEnchantedToolFactory(Items.DIAMOND_HOE, 8, 3, 15), 
                    new SellEnchantedToolFactory(Items.DIAMOND_AXE, 17, 3, 15)}
            )));
    }

    public static void replaceWeaponsmithProfessionToLeveledTrade(Map<VillagerProfession, Int2ObjectMap<Factory[]>> originalTradeOffers) {
        originalTradeOffers.replace(VillagerProfession.WEAPONSMITH, ModVillagerProfessions.copyToFastUtilMap(
            ImmutableMap.of(
                1, new Factory[]{new BuyForOneEmeraldFactory(Items.COAL, 15, 16, 2), new BuyForOneEmeraldFactory(Items.CHARCOAL, 15, 16, 2), new BuyForOneEmeraldFactory(Items.FLINT, 24, 16, 2), 
                    new SellItemFactory(Items.STONE_SWORD, 1, 1, 1), 
                    new SellItemFactory(ModItems.STONE_SPEAR, 1, 1, 1), new SellItemFactory(ModItems.FLINT_SPEAR, 1, 1, 1), 
                    new SellItemFactory(Items.STONE_AXE, 1, 1, 1), 
                    new SellItemFactory(ModItems.WOODEN_SHIELD, 2, 1, 1)}, 
                2, new Factory[]{new BuyForOneEmeraldFactory(Items.COPPER_INGOT, 8, 12, 10), 
                    new SellItemFactory(ModItems.COPPER_SWORD, 2, 1, 5), 
                    new SellItemFactory(ModItems.COPPER_SPEAR, 2, 1, 5), 
                    new SellItemFactory(ModItems.COPPER_AXE, 2, 1, 5), 
                    new SellItemFactory(ModItems.COPPER_FRAMED_WOODEN_SHIELD, 3, 1, 5), new SellItemFactory(ModItems.COPPER_SHIELD, 4, 1, 5)}, 
                3, new Factory[]{new BuyForOneEmeraldFactory(Items.GOLD_INGOT, 2, 12, 20),  
                    new SellItemFactory(Items.GOLDEN_SWORD, 3, 1, 10), 
                    new SellItemFactory(ModItems.GOLDEN_SPEAR, 3, 1, 10), 
                    new SellItemFactory(Items.GOLDEN_AXE, 3, 1, 10), 
                    new SellItemFactory(ModItems.GOLD_FRAMED_WOODEN_SHIELD, 6, 1, 10), new SellItemFactory(ModItems.GOLDEN_SHIELD, 8, 1, 10)}, 
                4, new Factory[]{new BuyForOneEmeraldFactory(Items.IRON_INGOT, 4, 12, 30), new BuyForOneEmeraldFactory(Items.LAVA_BUCKET, 1, 12, 30), 
                    new SellItemFactory(Items.IRON_SWORD, 3, 1, 15), 
                    new SellItemFactory(ModItems.IRON_SPEAR, 3, 1, 15), 
                    new SellItemFactory(Items.IRON_AXE, 3, 1, 15), 
                    new SellItemFactory(ModItems.IRON_FRAMED_WOODEN_SHIELD, 5, 1, 15), new SellItemFactory(ModItems.IRON_SHIELD, 12, 1, 15)}, 
                5, new Factory[]{new BuyForOneEmeraldFactory(Items.DIAMOND, 1, 12, 30), 
                    new SellEnchantedToolFactory(Items.DIAMOND_SWORD, 17, 3, 15), 
                    new SellEnchantedToolFactory(ModItems.DIAMOND_SPEAR, 17, 3, 15), 
                    new SellEnchantedToolFactory(Items.DIAMOND_AXE, 17, 3, 15), 
                    new SellEnchantedToolFactory(ModItems.DIAMOND_FRAMED_WOODEN_SHIELD, 15, 3, 15), new SellEnchantedToolFactory(ModItems.EMERALD_FRAMED_WOODEN_SHIELD, 10, 3, 15), new SellEnchantedToolFactory(ModItems.DIAMOND_SHIELD, 24, 3, 15)}
            )));
    }


    private static VillagerProfession registerVillagerProfession(String name, RegistryKey<PointOfInterestType> poiType, @Nullable ImmutableSet<Item> gatherableItems, @Nullable ImmutableSet<Block> secondaryJobSites, @Nullable SoundEvent workSound) {
        ImmutableSet<Item> gatherableItemsSet = ImmutableSet.of();
        if (gatherableItems != null) {
            gatherableItemsSet = gatherableItems;
        }
        ImmutableSet<Block> secondaryJobSitesSet = ImmutableSet.of();
        if (secondaryJobSites != null) {
            secondaryJobSitesSet = secondaryJobSites;
        }
        return Registry.register(Registries.VILLAGER_PROFESSION, new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name),
            new VillagerProfession(name, entry -> entry.matchesKey(poiType), entry -> entry.matchesKey(poiType), gatherableItemsSet, secondaryJobSitesSet, workSound));
    }

    private static PointOfInterestType registerPOI(String name, Block block) {
        return PointOfInterestHelper.register(new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name),
            1, 1, ImmutableSet.copyOf(block.getStateManager().getStates()));
    }

    public static void registerModVillagerProfessions() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Villager Professions for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

    private static Int2ObjectMap<Factory[]> copyToFastUtilMap(ImmutableMap<Integer, Factory[]> map) {
        return new Int2ObjectOpenHashMap<Factory[]>(map);
    }


    public static class TypeAwareSellForOneEmeraldFactory implements Factory {
        private final Map<VillagerType, Item> map;
        private final int count;
        private final int maxUses;
        private final int experience;


        public TypeAwareSellForOneEmeraldFactory(int count, int maxUses, int experience, Map<VillagerType, Item> map) {
            Registries.VILLAGER_TYPE.stream().filter(villagerType -> !map.containsKey(villagerType)).findAny().ifPresent(villagerType -> {
                throw new IllegalStateException("Missing trade for villager type: " + Registries.VILLAGER_TYPE.getId((VillagerType)villagerType));
            });
            this.map = map;
            this.count = count;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        @Override
        @Nullable
        public TradeOffer create(Entity entity, Random random) {
            if (entity instanceof VillagerDataContainer) {
                ItemStack itemStack = new ItemStack(this.map.get(((VillagerDataContainer)((Object)entity)).getVillagerData().getType()), this.count);
                if (itemStack.isOf(Items.TROPICAL_FISH_BUCKET)) {
                    TropicalFishEntity tropicalFishEntity = EntityType.TROPICAL_FISH.create(entity.world);
                    tropicalFishEntity.setVariant(TropicalFishEntity.Variety.fromId(random.nextInt(12)));
                    tropicalFishEntity.copyDataToStack(itemStack);
                }
                return new TradeOffer(new ItemStack(Items.EMERALD), itemStack, this.maxUses, this.experience, 0.05f);
            }
            return null;
        }
    }



}
