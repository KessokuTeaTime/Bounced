package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AccessibilityOnboardingScreen.class)
public abstract class AccessibilityOnboardingTrigger {
	@Shadow
	private boolean fadingIn;

	@Inject(method = "init", at = @At("RETURN"))
	private void bounced$startIntro(CallbackInfo ci) {
		Bounced.init(true);
	}

	@Inject(method = "extractRenderState", at = @At("HEAD"))
	private void bounced$update(
			GuiGraphicsExtractor graphics,
			int mouseX,
			int mouseY,
			float partialTick,
			CallbackInfo ci
	) {
		Bounced.resetWhen(true);
		Bounced.update();
	}

}
