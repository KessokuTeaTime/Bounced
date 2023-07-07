package net.krlite.bounced.mixin;

import net.krlite.bounced.Bounced;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This class is responsible for animating the logo.
 */
@Mixin(LogoDrawer.class)
public abstract class LogoDrawerAnimator {
	/**
	 * Applies the animation transformation to the 'MINECRAFT' logo.
	 */
	@Inject(
			method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V",
					shift = At.Shift.BEFORE,
					ordinal = 0
			)
	)
	private void animateLogoPre(DrawContext context, int screenWidth, float alpha, int y, CallbackInfo ci) {
		context.getMatrices().push();
		context.getMatrices().translate(0, Bounced.primaryPos(), 0);
	}

	/**
	 * Pops the matrix stack after rendering the 'MINECRAFT' logo.
	 */
	@Inject(
			method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V",
					shift = At.Shift.AFTER,
					ordinal = 0
			)
	)
	private void animateLogoPost(DrawContext context, int screenWidth, float alpha, int y, CallbackInfo ci) {
		context.getMatrices().pop();
	}

	/**
	 * Applies the animation transformation to the 'EDITION' banner.
	 */
	@Inject(
			method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V",
					shift = At.Shift.BEFORE,
					ordinal = 1
			)
	)
	private void animateBannerPre(DrawContext context, int screenWidth, float alpha, int y, CallbackInfo ci) {
		context.getMatrices().push();
		context.getMatrices().translate(0, Bounced.secondaryPos(), 0);
	}

	/**
	 * Pops the matrix stack after rendering the 'EDITION' banner.
	 */
	@Inject(
			method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V",
					shift = At.Shift.AFTER,
					ordinal = 1
			)
	)
	private void animateBannerPost(DrawContext context, int screenWidth, float alpha, int y, CallbackInfo ci) {
		context.getMatrices().pop();
	}
}
