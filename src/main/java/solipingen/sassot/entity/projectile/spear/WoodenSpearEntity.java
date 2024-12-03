package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class WoodenSpearEntity extends SpearEntity {
    public static final float SPEED = 2.5f;


    public WoodenSpearEntity(EntityType<? extends WoodenSpearEntity> entityType, World world) {
        super(entityType, world, ToolMaterials.WOOD, SPEED);
    }

    public WoodenSpearEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.WOODEN_SPEAR_ENTITY, owner, world, stack, ToolMaterials.WOOD, SPEED);
    }

    public WoodenSpearEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.WOODEN_SPEAR_ENTITY, x, y, z, world, stack, ToolMaterials.WOOD, SPEED);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.WOODEN_SPEAR);
    }

}
