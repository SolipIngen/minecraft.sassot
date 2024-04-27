package solipingen.sassot.entity.projectile.spear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import solipingen.sassot.entity.ModEntityTypes;
import solipingen.sassot.item.ModItems;


public class StoneSpearEntity extends SpearEntity {
    private static final float DAMAGE_AMOUNT = 5.0f;
    public static final float SPEED = 2.1f;
    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.STONE_SPEAR);

    
    public StoneSpearEntity(EntityType<? extends StoneSpearEntity> entityType, World world) {
        super(entityType, DAMAGE_AMOUNT, SPEED, DEFAULT_STACK, world);
    }

    public StoneSpearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntityTypes.STONE_SPEAR_ENTITY, owner, DAMAGE_AMOUNT, SPEED, stack, world);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }


}
