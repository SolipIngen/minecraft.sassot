package solipingen.sassot.util.interfaces.mixin.entity.projectile;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;


public interface FishingBobberEntityInterface {

    @Nullable
    public ItemStack getFishingRodStack();
    public void setFishingRodStack(ItemStack itemStack);


}
