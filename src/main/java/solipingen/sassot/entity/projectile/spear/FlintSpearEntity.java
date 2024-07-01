package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class FlintSpearEntity extends SpearEntity {
    public static final float SPEED = 2.3f;


    public FlintSpearEntity(EntityType<? extends FlintSpearEntity> entityType, World world) {
        super(entityType, world, ToolMaterials.STONE, SPEED);
    }

    public FlintSpearEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.FLINT_SPEAR_ENTITY, owner, world, stack, ToolMaterials.STONE, SPEED);
    }

    public FlintSpearEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.FLINT_SPEAR_ENTITY,x, y, z,  world, stack, ToolMaterials.STONE, SPEED);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.FLINT_SPEAR);
    }


}
