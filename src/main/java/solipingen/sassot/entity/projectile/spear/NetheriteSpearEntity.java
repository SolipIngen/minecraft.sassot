package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class NetheriteSpearEntity extends SpearEntity {
    public static final float SPEED = 2.5f;


    public NetheriteSpearEntity(EntityType<? extends NetheriteSpearEntity> entityType, World world) {
        super(entityType, world, ToolMaterials.NETHERITE, SPEED);
    }

    public NetheriteSpearEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntityTypes.NETHERITE_SPEAR_ENTITY, owner, world, stack, ToolMaterials.NETHERITE, SPEED);
    }

    public NetheriteSpearEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntityTypes.NETHERITE_SPEAR_ENTITY, x, y, z, world, stack, ToolMaterials.NETHERITE, SPEED);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.NETHERITE_SPEAR);
    }


}
