package solipingen.sassot.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.client.render.entity.projectile.BlazearmEntityRenderer;
import solipingen.sassot.client.render.entity.projectile.spear.BambooSpearEntityRenderer;
import solipingen.sassot.client.render.entity.projectile.spear.CopperSpearEntityRenderer;
import solipingen.sassot.client.render.entity.projectile.spear.DiamondSpearEntityRenderer;
import solipingen.sassot.client.render.entity.projectile.spear.FlintSpearEntityRenderer;
import solipingen.sassot.client.render.entity.projectile.spear.GoldenSpearEntityRenderer;
import solipingen.sassot.client.render.entity.projectile.spear.IronSpearEntityRenderer;
import solipingen.sassot.client.render.entity.projectile.spear.NetheriteSpearEntityRenderer;
import solipingen.sassot.client.render.entity.projectile.spear.StoneSpearEntityRenderer;
import solipingen.sassot.client.render.entity.projectile.spear.WoodenSpearEntityRenderer;
import solipingen.sassot.entity.ModEntityTypes;


@Environment(value=EnvType.CLIENT)
public class ModEntityRendererRegistry {

    
    public static void registerModEntityRenderers() {

        // Spears
        EntityRendererRegistry.register(ModEntityTypes.WOODEN_SPEAR_ENTITY, (context) -> new WoodenSpearEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.BAMBOO_SPEAR_ENTITY, (context) -> new BambooSpearEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.STONE_SPEAR_ENTITY, (context) -> new StoneSpearEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.FLINT_SPEAR_ENTITY, (context) -> new FlintSpearEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.COPPER_SPEAR_ENTITY, (context) -> new CopperSpearEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.IRON_SPEAR_ENTITY, (context) -> new IronSpearEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.GOLDEN_SPEAR_ENTITY, (context) -> new GoldenSpearEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.DIAMOND_SPEAR_ENTITY, (context) -> new DiamondSpearEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.NETHERITE_SPEAR_ENTITY, (context) -> new NetheriteSpearEntityRenderer(context));
        EntityRendererRegistry.register(ModEntityTypes.BLAZEARM_ENTITY, (context) -> new BlazearmEntityRenderer(context));

        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Entity Renderers for" + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);

    }

}
