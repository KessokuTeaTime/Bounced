package net.krlite.bounced.mixin;

import net.krlite.bounced.Bounced;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This class is responsible for animating the logo.
 */
@Mixin(LogoDrawer.class)
public abstract class LogoDrawerAnimator {
	/**
	 * Applies the animation before rendering the logo.
	 */
	@Inject(
			method = "draw(Lnet/minecraft/client/util/math/MatrixStack;IFI)V",
			at = @At("HEAD")
	)
	private void animateLogoPre(MatrixStack matrixStack, int screenWidth, float alpha, int y, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, Bounced.getPos(), 0);
	}

	/**
	 * Applies the animation after rendering the logo.
	 */
	@Inject(
			method = "draw(Lnet/minecraft/client/util/math/MatrixStack;IFI)V",
			at = @At("RETURN")
	)
	private void animateLogoPost(MatrixStack matrixStack, int screenWidth, float alpha, int y, CallbackInfo ci) {
		matrixStack.pop();
	}
}
