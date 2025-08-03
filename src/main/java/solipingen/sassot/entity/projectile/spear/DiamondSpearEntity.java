package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class DiamondSpearEntity extends SpearEntity {
    public static final float SPEED = 2.5f;


    public DiamondSpearEntity(EntityType<? extends DiamondSpearEntity> entityType, World world) {
        super(entityType, world, ToolMaterial.DIAMOND, SPEED);
    }

    public DiamondSpearEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.DIAMOND_SPEAR_ENTITY, owner, world, stack, ToolMaterial.DIAMOND, SPEED);
    }

    public DiamondSpearEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.DIAMOND_SPEAR_ENTITY, x, y, z, world, stack, ToolMaterial.DIAMOND, SPEED);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.DIAMOND_SPEAR);
    }


}
