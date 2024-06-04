package solipingen.sassot.mixin.item;

import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(FlintAndSteelItem.class)
public abstract class FlintAndSteelItemMixin extends Item {


    public FlintAndSteelItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static Item.Settings modifiedInit(Item.Settings settings) {
        return settings.maxDamage(108);
    }

}
