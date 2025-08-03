package solipingen.sassot.client.render.item.property;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinActivity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;


@Environment(EnvType.CLIENT)
public record UsingSpearProperty() implements BooleanProperty {
    public static final MapCodec<UsingSpearProperty> CODEC = MapCodec.unit(new UsingSpearProperty());


    @Override
    public boolean test(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof PiglinEntity piglinEntity) {
            return piglinEntity.getActivity() == PiglinActivity.ATTACKING_WITH_MELEE_WEAPON;
        }
        else {
            return entity.isUsingItem() && entity.getActiveItem() == stack && !(entity instanceof MerchantEntity);
        }
    }

    @Override
    public MapCodec<UsingSpearProperty> getCodec() {
        return CODEC;
    }
}
