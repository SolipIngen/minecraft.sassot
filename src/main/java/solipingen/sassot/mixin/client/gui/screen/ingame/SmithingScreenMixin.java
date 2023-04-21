package solipingen.sassot.mixin.client.gui.screen.ingame;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


@Mixin(SmithingScreen.class)
@Environment(value=EnvType.CLIENT)
public abstract class SmithingScreenMixin extends ForgingScreen<SmithingScreenHandler> {
    @Shadow @Final private static List<Identifier> EMPTY_SLOT_TEXTURES;
    @Shadow @Final private CyclingSlotIcon templateSlotIcon;
    @Shadow @Final private CyclingSlotIcon baseSlotIcon;
    private static final Identifier EMPTY_SLOT_SHIELD_FRAMING_UPGRADE_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "item/empty_slot_shield_framing_upgrade");
    private static final Identifier EMPTY_SLOT_FISHING_ROD_FUSION_UPGRADE_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "item/empty_slot_fishing_rod_fusion_upgrade");


    public SmithingScreenMixin(SmithingScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }
    
    @Redirect(method = "handledScreenTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CyclingSlotIcon;updateTexture(Ljava/util/List;)V"))
    private void redirectedEmptySlotTextures(CyclingSlotIcon slotIcon, List<Identifier> originalTextures) {
        ArrayList<Identifier> textures = new ArrayList<Identifier>();
        for (Identifier originalTexture : originalTextures) {
            textures.add(originalTexture);
        }
        if (slotIcon == this.templateSlotIcon) {
            textures.add(EMPTY_SLOT_SHIELD_FRAMING_UPGRADE_TEXTURE);
            textures.add(EMPTY_SLOT_FISHING_ROD_FUSION_UPGRADE_TEXTURE);
        }
        List<Identifier> textureList = List.copyOf(textures);
        slotIcon.updateTexture(textureList);
    }

    
}
