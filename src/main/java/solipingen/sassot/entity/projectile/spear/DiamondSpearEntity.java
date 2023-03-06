package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;


public class DiamondSpearEntity extends SpearEntity {
    private static final float DAMAGE_AMOUNT = 10.0f;
    public static final float SPEED = 2.5f;

    
    public DiamondSpearEntity(EntityType<? extends DiamondSpearEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, SPEED, world);
    }

    public DiamondSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.DIAMOND_SPEAR_ENTITY, owner, DAMAGE_AMOUNT, SPEED, stack, world);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }


}
