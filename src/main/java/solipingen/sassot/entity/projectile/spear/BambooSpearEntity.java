package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class BambooSpearEntity extends SpearEntity {
    public static final float SPEED = 2.7f;

    
    public BambooSpearEntity(EntityType<? extends BambooSpearEntity> entityType, World world) {
        super(entityType, world, ToolMaterial.WOOD, SPEED);
    }

    public BambooSpearEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.BAMBOO_SPEAR_ENTITY, owner, world, stack, ToolMaterial.WOOD, SPEED);
    }

    public BambooSpearEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.BAMBOO_SPEAR_ENTITY, x, y, z, world, stack, ToolMaterial.WOOD, SPEED);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.BAMBOO_SPEAR);
    }

}
