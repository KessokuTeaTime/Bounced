package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenTrigger {
	@Shadow
	private boolean fading;

	@Inject(method = "init", at = @At("RETURN"))
	private void bounced$startIntro(CallbackInfo ci) {
		Bounced.startIntro();
	}

	@Inject(method = "extractRenderState", at = @At("HEAD"))
	private void bounced$update(
			GuiGraphicsExtractor graphics,
			int mouseX,
			int mouseY,
			float partialTick,
			CallbackInfo ci
	) {
		Bounced.resetWhen(!fading);
		Bounced.update();
	}

	@Redirect(
			method = "extractRenderState",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/Mth;clampedMap(FFFFF)F"
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

	@Inject(method = "mouseClicked", at = @At("RETURN"))
	private void bounced$handleLogoClick(
			MouseButtonEvent event,
			boolean doubleClick,
			CallbackInfoReturnable<Boolean> cir
	) {
		TitleScreen screen = (TitleScreen) (Object) this;
		if (!cir.getReturnValue() && !Bounced.isIntro()
				&& Bounced.isLogoHovered(screen.width, event.x(), event.y())) {
			Bounced.push();
		}
	}
}
