package solipingen.sassot.mixin.item.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;


@Mixin(HoeItem.class)
public interface HoeItemInvoker {


    @Invoker("<init>")
    public static HoeItem invokeHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Item.Settings settings) {
        throw new AssertionError();
    }

    
}
