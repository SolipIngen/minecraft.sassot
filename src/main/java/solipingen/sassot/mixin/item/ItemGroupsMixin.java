package solipingen.sassot.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import solipingen.sassot.registry.tag.ModItemTags;

import java.util.ArrayList;
import java.util.Set;


@Mixin(ItemGroups.class)
public abstract class ItemGroupsMixin {


    @ModifyVariable(method = "addMaxLevelEnchantedBooks", at = @At("HEAD"), index = 2)
    private static Set<TagKey<Item>> modifiedMaxLevelEnchantedBooks(Set<TagKey<Item>> originalSet) {
        ArrayList<TagKey<Item>> list = new ArrayList<>(originalSet);
        list.add(ModItemTags.KNOCKBACK_ENCHANTABLE);
        list.add(ModItemTags.HACKING_WEAPONS);
        list.add(ModItemTags.SPEAR_ENCHANTABLE);
        list.add(ModItemTags.THRUSTING_WEAPONS);
        list.add(ModItemTags.LOYALTY_ENCHANTABLE);
        list.add(ModItemTags.PIERCING_ENCHANTABLE);
        list.add(ModItemTags.BLAZEARM_ENCHANTABLE);
        list.add(ModItemTags.SHIELD_ENCHANTABLE);
        return Set.copyOf(list);
    }

    @ModifyVariable(method = "addAllLevelEnchantedBooks", at = @At("HEAD"), index = 2)
    private static Set<TagKey<Item>> modifiedAllLevelEnchantedBooks(Set<TagKey<Item>> originalSet) {
        ArrayList<TagKey<Item>> list = new ArrayList<>(originalSet);
        list.add(ModItemTags.KNOCKBACK_ENCHANTABLE);
        list.add(ModItemTags.HACKING_WEAPONS);
        list.add(ModItemTags.SPEAR_ENCHANTABLE);
        list.add(ModItemTags.THRUSTING_WEAPONS);
        list.add(ModItemTags.LOYALTY_ENCHANTABLE);
        list.add(ModItemTags.PIERCING_ENCHANTABLE);
        list.add(ModItemTags.BLAZEARM_ENCHANTABLE);
        list.add(ModItemTags.SHIELD_ENCHANTABLE);
        return Set.copyOf(list);
    }


}
