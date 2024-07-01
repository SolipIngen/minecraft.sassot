package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.item.ModToolMaterials;


public class CopperSpearEntity extends SpearEntity {
    public static final float SPEED = 2.5f;


    public CopperSpearEntity(EntityType<? extends CopperSpearEntity> entityType, World world) {
        super(entityType, world, ModToolMaterials.COPPER, SPEED);
    }

    public CopperSpearEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.COPPER_SPEAR_ENTITY, owner, world, stack, ModToolMaterials.COPPER, SPEED);
    }

    public CopperSpearEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.COPPER_SPEAR_ENTITY,x, y, z,  world, stack, ModToolMaterials.COPPER, SPEED);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.COPPER_SPEAR);
    }


}
