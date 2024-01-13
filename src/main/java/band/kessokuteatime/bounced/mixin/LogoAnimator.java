package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This class is responsible for animating the logo.
 */
@Mixin(TitleScreen.class)
public abstract class LogoAnimator {
	/**
	 * Applies the animation transformation to the 'MINECRAFT' logo.
	 */
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawWithOutline(IILjava/util/function/BiConsumer;)V",
					shift = At.Shift.BEFORE
			)
	)
	private void animateLogoPre(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, Bounced.primaryPos(), 0);
	}

	/**
	 * Pops the matrix stack after rendering the 'MINECRAFT' logo.
	 */
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawWithOutline(IILjava/util/function/BiConsumer;)V",
					shift = At.Shift.AFTER
			)
	)
	private void animateLogoPost(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.pop();
	}

	/**
	 * Applies the animation transformation to the 'EDITION' banner.
	 */
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V",
					shift = At.Shift.BEFORE
			)
	)
	private void animateBannerPre(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, Bounced.secondaryPos(), 0);
	}

	/**
	 * Pops the matrix stack after rendering the 'EDITION' banner.
	 */
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V",
					shift = At.Shift.AFTER
			)
	)
	private void animateBannerPost(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.pop();
	}
}
