package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
		Bounced.resetWhen(!fadingIn);
		Bounced.update();
	}

	@Redirect(
			method = "extractRenderState",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/Mth;clampedMap(FFFFF)F",
					ordinal = 0
			)
	)
	private float bounced$startAfterFade(
			float value,
			float oldStart,
			float oldEnd,
			float newStart,
			float newEnd
	) {
		float mapped = Mth.clampedMap(value, oldStart, oldEnd, newStart, newEnd);
		Bounced.resetWhen(mapped > 0.9F);
		return mapped;
	}

}
