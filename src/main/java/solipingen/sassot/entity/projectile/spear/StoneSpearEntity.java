package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class StoneSpearEntity extends SpearEntity {
    public static final float SPEED = 2.1f;

    
    public StoneSpearEntity(EntityType<? extends StoneSpearEntity> entityType, World world) {
        super(entityType, world, ToolMaterials.STONE, SPEED);
    }

    public StoneSpearEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.STONE_SPEAR_ENTITY, owner, world, stack, ToolMaterials.STONE, SPEED);
    }

    public StoneSpearEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.STONE_SPEAR_ENTITY,x, y, z,  world, stack, ToolMaterials.STONE, SPEED);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.STONE_SPEAR);
    }


}
