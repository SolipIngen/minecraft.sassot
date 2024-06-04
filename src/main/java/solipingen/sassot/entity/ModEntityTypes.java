package solipingen.sassot.entity;

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
            EntityType.Builder.<WoodenSpearEntity>create(WoodenSpearEntity::new, SpawnGroup.MISC)
                .dimensions(0.5f, 0.5f)
            .build());
    
    public static final EntityType<BambooSpearEntity> BAMBOO_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "bamboo_spear"),
            EntityType.Builder.<BambooSpearEntity>create( BambooSpearEntity::new, SpawnGroup.MISC)
                .dimensions(0.5f, 0.5f)
            .build());
    
    public static final EntityType<StoneSpearEntity> STONE_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "stone_spear"),
            EntityType.Builder.<StoneSpearEntity>create(StoneSpearEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
            .build());

    public static final EntityType<FlintSpearEntity> FLINT_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "flint_spear"),
            EntityType.Builder.<FlintSpearEntity>create(FlintSpearEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
            .build());

    public static final EntityType<CopperSpearEntity> COPPER_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "copper_spear"),
            EntityType.Builder.<CopperSpearEntity>create(CopperSpearEntity::new,SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
            .build());

    public static final EntityType<GoldenSpearEntity> GOLDEN_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "golden_spear"),
            EntityType.Builder.<GoldenSpearEntity>create(GoldenSpearEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
            .build());

    public static final EntityType<IronSpearEntity> IRON_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "iron_spear"),
            EntityType.Builder.<IronSpearEntity>create(IronSpearEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
            .build());
    
    public static final EntityType<DiamondSpearEntity> DIAMOND_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "diamond_spear"),
            EntityType.Builder.<DiamondSpearEntity>create(DiamondSpearEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
            .build());

    public static final EntityType<NetheriteSpearEntity> NETHERITE_SPEAR_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "netherite_spear"),
            EntityType.Builder.<NetheriteSpearEntity>create(NetheriteSpearEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
            .build());

    public static final EntityType<BlazearmEntity> BLAZEARM_ENTITY = Registry.register(Registries.ENTITY_TYPE,
        new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "blazearm"), EntityType.Builder.<BlazearmEntity>create(BlazearmEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
            .build());


    public static void registerModEntityTypes() {
        SpearsAxesSwordsShieldsAndOtherTools.LOGGER.debug("Registering Mod Entity Types for " + SpearsAxesSwordsShieldsAndOtherTools.MOD_ID);
    }

}
