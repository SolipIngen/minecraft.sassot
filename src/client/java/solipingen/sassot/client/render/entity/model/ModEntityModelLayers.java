package solipingen.sassot.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.client.render.entity.model.projectile.BlazearmEntityModel;
import solipingen.sassot.client.render.entity.model.projectile.spear.*;


@Environment(value = EnvType.CLIENT)
public class ModEntityModelLayers {
    public static final EntityModelLayer WOODEN_SPEAR_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "wooden_spear"), "main");
    public static final EntityModelLayer BAMBOO_SPEAR_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "bamboo_spear"), "main");
    public static final EntityModelLayer STONE_SPEAR_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "stone_spear"), "main");
    public static final EntityModelLayer FLINT_SPEAR_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "flint_spear"), "main");
    public static final EntityModelLayer COPPER_SPEAR_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "copper_spear"), "main");
    public static final EntityModelLayer GOLDEN_SPEAR_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "golden_spear"), "main");
    public static final EntityModelLayer IRON_SPEAR_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "iron_spear"), "main");
    public static final EntityModelLayer DIAMOND_SPEAR_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "diamond_spear"), "main");
    public static final EntityModelLayer NETHERITE_SPEAR_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "netherite_spear"), "main");
    public static final EntityModelLayer BLAZEARM_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "blazearm"), "main");


    public static void registerModEntityModelLayers() {

        EntityModelLayerRegistry.registerModelLayer(WOODEN_SPEAR_ENTITY_MODEL_LAYER, WoodenSpearEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BAMBOO_SPEAR_ENTITY_MODEL_LAYER, BambooSpearEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(STONE_SPEAR_ENTITY_MODEL_LAYER, StoneSpearEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(FLINT_SPEAR_ENTITY_MODEL_LAYER, FlintSpearEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(COPPER_SPEAR_ENTITY_MODEL_LAYER, CopperSpearEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(GOLDEN_SPEAR_ENTITY_MODEL_LAYER, GoldenSpearEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(IRON_SPEAR_ENTITY_MODEL_LAYER, IronSpearEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DIAMOND_SPEAR_ENTITY_MODEL_LAYER, DiamondSpearEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(NETHERITE_SPEAR_ENTITY_MODEL_LAYER, NetheriteSpearEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BLAZEARM_ENTITY_MODEL_LAYER, BlazearmEntityModel::getTexturedModelData);

        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Entity Model Layers for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);

    }
    
    
}
