package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class ScreenTrigger {
	@Inject(method = "setScreen", at = @At("TAIL"))
	private void bounced$armTitleAnimation(Screen screen, CallbackInfo ci) {
		if (!(screen instanceof TitleScreen || screen instanceof AccessibilityOnboardingScreen)) {
			Bounced.push();
		}
	}
}
