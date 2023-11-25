package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This class is responsible for triggering the title animation before the title screen is rendered.
 */
@Mixin(MinecraftClient.class)
class Trigger {
	@Inject(method = "setScreen", at = @At("TAIL"))
	private void trigger(Screen screen, CallbackInfo ci) {
		if (!(screen instanceof TitleScreen)) Bounced.push();
	}
}

/**
 * This class is responsible for triggering the title animation when the game starts for the first time.
 */
@Mixin(AccessibilityOnboardingScreen.class)
class AccessibilityOnboardingTrigger {
	@Inject(method = "init", at = @At("RETURN"))
	private void init(CallbackInfo ci) {
		Bounced.init();
	}

	/**
	 * Triggers and restarts the animation.
	 */
	@ModifyArg(
			method = "renderBackground",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V"
			), index = 1
	)
	private float trigger(float progress) {
		Bounced.resetWhen(progress > 0.9);
		Bounced.update();
		return progress;
	}
}

/**
 * This class is responsible for triggering the animation and animating the splash text.
 */
@Mixin(TitleScreen.class)
public class SplashTextAnimator {
	@Inject(method = "init", at = @At("RETURN"))
	private void init(CallbackInfo ci) {
		Bounced.init();
	}

	/**
	 * Triggers and restarts the animation.
	 */
	@ModifyArg(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V"
			), index = 1
	)
	private float trigger(float progress) {
		Bounced.resetWhen(progress > 0.9);
		Bounced.update();
		return progress;
	}
}

@Mixin(SplashTextRenderer.class)
class SplashTextTranslator {
	/**
	 * Applies the animation transformation to the splash text.
	 */
	@ModifyArg(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V",
					ordinal = 0
			), index = 1
	)
	private float animateSplashText(float y) {
		return (float) (y + Bounced.secondaryPos());
	}
}
