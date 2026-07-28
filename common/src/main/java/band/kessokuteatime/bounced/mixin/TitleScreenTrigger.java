package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenTrigger {
	@Shadow
	private boolean fading;

	@Inject(method = "init", at = @At("RETURN"))
	private void bounced$startIntro(CallbackInfo ci) {
		Bounced.startTitleIntro();
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
