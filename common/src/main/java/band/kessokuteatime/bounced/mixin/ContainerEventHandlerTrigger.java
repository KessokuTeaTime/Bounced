package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEventHandler.class)
public interface ContainerEventHandlerTrigger {
	@Inject(method = "mouseClicked", at = @At("RETURN"))
	private void bounced$handleOnboardingLogoClick(
			MouseButtonEvent event,
			boolean doubleClick,
			CallbackInfoReturnable<Boolean> cir
	) {
		if ((Object) this instanceof AccessibilityOnboardingScreen screen
				&& !cir.getReturnValue()
				&& !Bounced.isIntro()
				&& Bounced.isLogoHovered(screen.width, event.x(), event.y())) {
			Bounced.push();
		}
	}
}
