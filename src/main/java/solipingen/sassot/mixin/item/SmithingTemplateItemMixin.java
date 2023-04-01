package solipingen.sassot.mixin.item;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


@Mixin(SmithingTemplateItem.class)
public abstract class SmithingTemplateItemMixin extends Item {
    private static final Identifier EMPTY_SLOT_SPEAR_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "item/empty_slot_spear");
    private static final Identifier EMPTY_SLOT_SHIELD_TEXTURE = new Identifier("item/empty_armor_slot_shield"); 
    

    public SmithingTemplateItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyArg(method = "createNetheriteUpgrade", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/SmithingTemplateItem;<init>(Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Ljava/util/List;Ljava/util/List;)V"), index = 5)
    private static List<Identifier> redirectedNetheriteUpgradeEmptyBaseSlotTextures(List<Identifier> originalList) {
        ArrayList<Identifier> textures = new ArrayList<Identifier>();
        for (Identifier texture : originalList) {
            textures.add(texture);
        }
        textures.add(EMPTY_SLOT_SPEAR_TEXTURE);
        textures.add(EMPTY_SLOT_SHIELD_TEXTURE);
        return List.copyOf(textures);
    }



    
}
