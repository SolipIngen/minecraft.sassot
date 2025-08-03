package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class IronSpearEntity extends SpearEntity {
    public static final float SPEED = 2.5f;


    public IronSpearEntity(EntityType<? extends IronSpearEntity> entityType, World world) {
        super(entityType, world, ToolMaterial.IRON, SPEED);
    }

    public IronSpearEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.IRON_SPEAR_ENTITY, owner, world, stack, ToolMaterial.IRON, SPEED);
    }

    public IronSpearEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.IRON_SPEAR_ENTITY, x, y, z, world, stack, ToolMaterial.IRON, SPEED);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.IRON_SPEAR);
    }


}
