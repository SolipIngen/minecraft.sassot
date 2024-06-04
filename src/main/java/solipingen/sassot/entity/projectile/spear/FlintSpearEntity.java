package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class FlintSpearEntity extends SpearEntity {    
    private static final float DAMAGE_AMOUNT = 5.0f;
    public static final float SPEED = 2.3f;
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.FLINT_SPEAR);

    
    public FlintSpearEntity(EntityType<? extends FlintSpearEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, SPEED, DEFAULT_STACK, world);
    }

    public FlintSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.FLINT_SPEAR_ENTITY, owner, DAMAGE_AMOUNT, SPEED, stack, world);
    }

    public FlintSpearEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntityTypes.FLINT_SPEAR_ENTITY, world, x, y, z, DAMAGE_AMOUNT, SPEED, stack);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return DEFAULT_STACK;
    }


}
