package solipingen.sassot.mixin.item;

import net.minecraft.item.BrushItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(BrushItem.class)
public abstract class BrushItemMixin extends Item {


    public BrushItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static Item.Settings modifiedInit(Item.Settings settings) {
        return settings.maxDamage(96);
    }


}
