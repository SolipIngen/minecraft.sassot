package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class BambooSpearEntity extends SpearEntity {
    private static final float DAMAGE_AMOUNT = 4.0f;
    public static final float SPEED = 2.7f;
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.BAMBOO_SPEAR);

    
    public BambooSpearEntity(EntityType<? extends BambooSpearEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, SPEED, DEFAULT_STACK, world);
    }

    public BambooSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.BAMBOO_SPEAR_ENTITY, owner, DAMAGE_AMOUNT, SPEED, stack, world);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }


}
