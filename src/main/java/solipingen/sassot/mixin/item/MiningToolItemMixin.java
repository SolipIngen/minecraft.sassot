package solipingen.sassot.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;


@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem {


    public MiningToolItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @ModifyConstant(method = "postDamageEntity", constant = @Constant(intValue = 2))
    private int modifiedPostDamageEntity(int oldDamage) {
        return 1;
    }


}
