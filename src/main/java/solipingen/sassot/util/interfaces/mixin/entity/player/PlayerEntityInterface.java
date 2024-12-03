package solipingen.sassot.util.interfaces.mixin.entity.player;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;


public interface PlayerEntityInterface {


    @Nullable public ItemStack getFishingRodStack();
    public void setFishingRodStack(ItemStack itemStack);

}
