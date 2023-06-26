package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;


public class CopperSpearEntity extends SpearEntity {
    private static final float DAMAGE_AMOUNT = 7.0f;
    public static final float SPEED = 2.5f;

    
    public CopperSpearEntity(EntityType<? extends CopperSpearEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, SPEED, world);
    }

    public CopperSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.COPPER_SPEAR_ENTITY, owner, DAMAGE_AMOUNT, SPEED, stack, world);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }


}
