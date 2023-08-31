package net.krlite.bounced.mixin;

import net.krlite.bounced.Bounced;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenAnimator {
	/**
	 * Applies the animation transformation to the 'MINECRAFT' logo.
	 */
	@Inject(
			method = "render",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V", ordinal = 1)
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
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", shift = At.Shift.BEFORE),
			slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/TitleScreen;isMinceraft:Z"))
	)
	private void animateLogoPost(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.pop();
	}

	/**
	 * Applies the animation transformation to the 'EDITION' banner.
	 */
	@Inject(
			method = "render",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", shift = At.Shift.BEFORE),
			slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/TitleScreen;isMinceraft:Z"))
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
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V", shift = At.Shift.AFTER)
	)
	private void animateBannerPost(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.pop();
	}
}
