package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class GoldenSpearEntity extends SpearEntity {
    private static final float DAMAGE_AMOUNT = 6.0f;
    public static final float SPEED = 2.5f;
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.GOLDEN_SPEAR);

    
    public GoldenSpearEntity(EntityType<? extends GoldenSpearEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, SPEED, DEFAULT_STACK, world);
    }

    public GoldenSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.GOLDEN_SPEAR_ENTITY, owner, DAMAGE_AMOUNT, SPEED, stack, world);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }


}
