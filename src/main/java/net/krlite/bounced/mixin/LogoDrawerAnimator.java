package net.krlite.bounced.mixin;

import net.krlite.bounced.Bounced;
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
			method = "draw(Lnet/minecraft/client/util/math/MatrixStack;IFI)V",
			at = @At("HEAD")
	)
	private void animateLogoPre(MatrixStack matrixStack, int screenWidth, float alpha, int y, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, Bounced.primaryPos(), 0);
	}

	/**
	 * Pops the matrix stack after rendering the 'MINECRAFT' logo.
	 */
	@Inject(
			method = "draw(Lnet/minecraft/client/util/math/MatrixStack;IFI)V",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", shift = At.Shift.BEFORE),
			slice = @Slice(from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"))
	)
	private void animateLogoPost(MatrixStack matrixStack, int screenWidth, float alpha, int y, CallbackInfo ci) {
		matrixStack.pop();
	}

	/**
	 * Applies the animation transformation to the 'EDITION' banner.
	 */
	@Inject(
			method = "draw(Lnet/minecraft/client/util/math/MatrixStack;IFI)V",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", shift = At.Shift.BEFORE),
			slice = @Slice(from = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"))
	)
	private void animateBannerPre(MatrixStack matrixStack, int screenWidth, float alpha, int y, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, Bounced.secondaryPos(), 0);
	}

	/**
	 * Pops the matrix stack after rendering the 'EDITION' banner.
	 */
	@Inject(
			method = "draw(Lnet/minecraft/client/util/math/MatrixStack;IFI)V",
			at = @At("RETURN")
	)
	private void animateBannerPost(MatrixStack matrixStack, int screenWidth, float alpha, int y, CallbackInfo ci) {
		matrixStack.pop();
	}
}
