package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class NetheriteSpearEntity extends SpearEntity {
    private static final float DAMAGE_AMOUNT = 11.0f;
    public static final float SPEED = 2.5f;
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.NETHERITE_SPEAR);

    
    public NetheriteSpearEntity(EntityType<? extends NetheriteSpearEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, SPEED, DEFAULT_STACK, world);
    }

    public NetheriteSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.NETHERITE_SPEAR_ENTITY, owner, DAMAGE_AMOUNT, SPEED, stack, world);
    }

    public NetheriteSpearEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntityTypes.NETHERITE_SPEAR_ENTITY, world, x, y, z, DAMAGE_AMOUNT, SPEED, stack);
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
