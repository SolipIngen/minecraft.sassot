package solipingen.sassot.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.entity.projectile.BlazearmEntity;
import solipingen.sassot.entity.projectile.spear.BambooSpearEntity;
import solipingen.sassot.entity.projectile.spear.CopperSpearEntity;
import solipingen.sassot.entity.projectile.spear.DiamondSpearEntity;
import solipingen.sassot.entity.projectile.spear.FlintSpearEntity;
import solipingen.sassot.entity.projectile.spear.GoldenSpearEntity;
import solipingen.sassot.entity.projectile.spear.IronSpearEntity;
import solipingen.sassot.entity.projectile.spear.NetheriteSpearEntity;
import solipingen.sassot.entity.projectile.spear.StoneSpearEntity;
import solipingen.sassot.entity.projectile.spear.WoodenSpearEntity;


public class ModEntityTypes {

    public static final EntityType<WoodenSpearEntity> WOODEN_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "wooden_spear"),
        FabricEntityTypeBuilder.<WoodenSpearEntity>create(SpawnGroup.MISC, WoodenSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());
    
    public static final EntityType<BambooSpearEntity> BAMBOO_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "bamboo_spear"),
        FabricEntityTypeBuilder.<BambooSpearEntity>create(SpawnGroup.MISC, BambooSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());
    
    public static final EntityType<StoneSpearEntity> STONE_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "stone_spear"),
        FabricEntityTypeBuilder.<StoneSpearEntity>create(SpawnGroup.MISC, StoneSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());

    public static final EntityType<FlintSpearEntity> FLINT_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "flint_spear"),
        FabricEntityTypeBuilder.<FlintSpearEntity>create(SpawnGroup.MISC, FlintSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());

    public static final EntityType<CopperSpearEntity> COPPER_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "copper_spear"),
        FabricEntityTypeBuilder.<CopperSpearEntity>create(SpawnGroup.MISC, CopperSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());

    public static final EntityType<GoldenSpearEntity> GOLDEN_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "golden_spear"),
        FabricEntityTypeBuilder.<GoldenSpearEntity>create(SpawnGroup.MISC, GoldenSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());

    public static final EntityType<IronSpearEntity> IRON_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "iron_spear"),
        FabricEntityTypeBuilder.<IronSpearEntity>create(SpawnGroup.MISC, IronSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());
    
    public static final EntityType<DiamondSpearEntity> DIAMOND_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "diamond_spear"),
        FabricEntityTypeBuilder.<DiamondSpearEntity>create(SpawnGroup.MISC, DiamondSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());

    public static final EntityType<NetheriteSpearEntity> NETHERITE_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "netherite_spear"),
        FabricEntityTypeBuilder.<NetheriteSpearEntity>create(SpawnGroup.MISC, NetheriteSpearEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());

    public static final EntityType<BlazearmEntity> BLAZEARM_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "blazearm"),
        FabricEntityTypeBuilder.<BlazearmEntity>create(SpawnGroup.MISC, BlazearmEntity::new)
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .build());


    public static void registerModEntityTypes() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Items for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

}
