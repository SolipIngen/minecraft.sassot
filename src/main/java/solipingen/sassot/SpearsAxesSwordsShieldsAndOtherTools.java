package solipingen.sassot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import solipingen.sassot.advancement.ModCriteria;
import solipingen.sassot.block.ModBlocks;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItemGroups;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.ModifyItemComponentHandler;
import solipingen.sassot.loot.ModifyLootTableHandler;
import solipingen.sassot.loot.ReplaceLootTableHandler;
import solipingen.sassot.recipe.ModRecipes;
import solipingen.sassot.sound.ModSoundEvents;
import solipingen.sassot.village.ModVillagerProfessions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SpearsAxesSwordsShieldsAndOtherTools implements ModInitializer {

	public static final String MOD_ID = "sassot";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	
	@Override
	public void onInitialize() {

		ModBlocks.registerModBlocks();
		ModCriteria.registerModCriteria();
		ModEnchantments.registerModEnchantments();
		ModEntityTypes.registerModEntityTypes();
		ModItems.registerModItems();
		ModItemGroups.registerModItemsToVanillaGroups();
		ModRecipes.registerModRecipes();
		ModSoundEvents.registerModSoundEvents();
		ModVillagerProfessions.registerModVillagerProfessions();

		DefaultItemComponentEvents.MODIFY.register(new ModifyItemComponentHandler());

		LootTableEvents.REPLACE.register(new ReplaceLootTableHandler());
		LootTableEvents.MODIFY.register(new ModifyLootTableHandler());

	}

}
