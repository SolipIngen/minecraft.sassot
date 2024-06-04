package solipingen.sassot.mixin.item;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


@Mixin(SmithingTemplateItem.class)
public abstract class SmithingTemplateItemMixin extends Item {
    private static final Identifier EMPTY_SLOT_SPEAR_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "item/empty_slot_spear");
    private static final Identifier EMPTY_SLOT_SHIELD_TEXTURE = new Identifier("item/empty_armor_slot_shield"); 
    private static final Identifier EMPTY_SLOT_FISHING_ROD_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "item/empty_slot_fishing_rod");
    

    public SmithingTemplateItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static List<Identifier> modifiedEmptyBaseSlotTextures(List<Identifier> originalList) {
        ArrayList<Identifier> textures = new ArrayList<Identifier>(originalList);
        textures.add(EMPTY_SLOT_SPEAR_TEXTURE);
        textures.add(EMPTY_SLOT_SHIELD_TEXTURE);
        textures.add(EMPTY_SLOT_FISHING_ROD_TEXTURE);
        return List.copyOf(textures);
    }



    
}
