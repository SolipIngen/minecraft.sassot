package solipingen.sassot.mixin.client;

import net.minecraft.client.gui.screen.TitleScreen;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TitleScreen.class)
public class WelcomeMixin {
	
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {

		SpearsAxesSwordsShieldsAndOtherTools.LOGGER.info("Welcome to Spears, Axes, Swords, Shields, and Other Tools! Steadfast stance, life is a dance.");
		
	}
}
