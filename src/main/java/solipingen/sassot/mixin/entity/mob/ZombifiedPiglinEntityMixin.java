package solipingen.sassot.mixin.entity.mob;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;


@Mixin(ZombifiedPiglinEntity.class)
public abstract class ZombifiedPiglinEntityMixin extends ZombieEntity implements Angerable {


    public ZombifiedPiglinEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "initEquipment", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;GOLDEN_SWORD:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC))
    private Item redirectedGoldenSword(Random random, LocalDifficulty localDifficulty) {
        if (random.nextFloat() < 0.33f) {
            float spearRandomf = this.random.nextFloat()*this.getWorld().getDifficulty().getId() + 0.1f*localDifficulty.getClampedLocalDifficulty();
            if (spearRandomf < 0.15f) {
                return ModItems.WOODEN_SPEAR;
            }
            else if (spearRandomf >= 0.15f && spearRandomf < 0.25f) {
                return ModItems.STONE_SPEAR;
            }
            else if (spearRandomf >= 0.25f && spearRandomf < 0.5f) {
                return ModItems.FLINT_SPEAR;
            }
            return ModItems.GOLDEN_SPEAR;
        }
        else {
            float swordRandomf = random.nextFloat()*this.getWorld().getDifficulty().getId() + 0.1f*localDifficulty.getClampedLocalDifficulty();
            if (swordRandomf < 0.15f) {
                return Items.WOODEN_SWORD;
            }
            else if (swordRandomf >= 0.15f && swordRandomf < 0.5f) {
                return Items.STONE_SWORD;
            }
            return Items.GOLDEN_SWORD;
        }
    }


    
}
