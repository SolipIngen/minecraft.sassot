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


@Environment(value = EnvType.CLIENT)
public class ModEntityRendererRegistry {

    
    public static void registerModEntityRenderers() {

        // Spears
        EntityRendererRegistry.register(ModEntityTypes.WOODEN_SPEAR_ENTITY, WoodenSpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.BAMBOO_SPEAR_ENTITY, BambooSpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.STONE_SPEAR_ENTITY, StoneSpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.FLINT_SPEAR_ENTITY, FlintSpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.COPPER_SPEAR_ENTITY, CopperSpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.IRON_SPEAR_ENTITY, IronSpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.GOLDEN_SPEAR_ENTITY, GoldenSpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.DIAMOND_SPEAR_ENTITY, DiamondSpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.NETHERITE_SPEAR_ENTITY, NetheriteSpearEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.BLAZEARM_ENTITY, BlazearmEntityRenderer::new);

        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Entity Renderers for" + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);

    }

}
