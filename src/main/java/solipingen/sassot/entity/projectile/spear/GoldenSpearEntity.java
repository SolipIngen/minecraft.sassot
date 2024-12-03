package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class GoldenSpearEntity extends SpearEntity {
    public static final float SPEED = 2.5f;
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.GOLDEN_SPEAR);

    
    public GoldenSpearEntity(EntityType<? extends GoldenSpearEntity> entityType, World world) {
        super(entityType, world, ToolMaterials.GOLD, SPEED);
    }

    public GoldenSpearEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.GOLDEN_SPEAR_ENTITY, owner, world, stack, ToolMaterials.GOLD, SPEED);
    }

    public GoldenSpearEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.GOLDEN_SPEAR_ENTITY, x, y, z, world, stack, ToolMaterials.GOLD, SPEED);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.GOLDEN_SPEAR);
    }


}
