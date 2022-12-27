package net.krlite.bounced.mixin;

import net.krlite.bounced.Bounced;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.krlite.bounced.Bounced.PUSHER;

/**
 * This class is responsible for triggering the title animation before the title screen is rendered.
 */
@Mixin(MinecraftClient.class)
class Trigger {
	@Inject(method = "setScreen", at = @At("TAIL"))
	private void trigger(Screen screen, CallbackInfo ci) {
		if (!(screen instanceof TitleScreen)) PUSHER.let();
	}
}

/**
 * This class is responsible for animating the titles.
 */
@Mixin(TitleScreen.class)
public class MinecraftAnimator {
	private Bounced.Timer timer = new Bounced.Timer(853);
	private double yPos = 0;

	/**
	 * Triggers the animation when the title is first rendered.
	 */
	@ModifyArg(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"
			),
			slice = @Slice(
					from = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/TitleScreen;MINECRAFT_TITLE_TEXTURE:Lnet/minecraft/util/Identifier;")
			), index = 3
	)
	private float trigger(float alpha) {
		// Short-circuit manipulation
		if (alpha > 0 && PUSHER.access()) timer = timer.reset();
		return alpha;
	}

	/**
	 * Animates the titles' y position.
	 */
	@Inject(
			method = "render",
			at = @At(
					value = "FIELD", target = "Lnet/minecraft/client/gui/screen/TitleScreen;MINECRAFT_TITLE_TEXTURE:Lnet/minecraft/util/Identifier;",
					shift = At.Shift.AFTER
			)
	)
	private void animate(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		double offset = MinecraftClient.getInstance().getWindow().getScaledHeight() / 3.5;
		yPos = Bounced.easeOutBounce(-offset, offset, timer);
	}

	/**
	 * Applies the animation to <code>MINECRAFT</code>.
	 */
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawWithOutline(IILjava/util/function/BiConsumer;)V",
					shift = At.Shift.BEFORE
			)
	)
	private void animateMinecraftPre(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, yPos, 0);
	}

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawWithOutline(IILjava/util/function/BiConsumer;)V",
					shift = At.Shift.AFTER
			)
	)
	private void animateMinecraftPost(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.pop();
	}

	/**
	 * Applies the animation to <code>EDITION</code>.
	 */
	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V",
					ordinal = 0,
					shift = At.Shift.BEFORE
			)
	)
	private void animateEditionPre(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.push();
		matrixStack.translate(0, yPos, 0);
	}

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V",
					ordinal = 0,
					shift = At.Shift.AFTER
			)
	)
	private void animateEditionPost(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		matrixStack.pop();
	}

	/**
	 * Applies the animation to the splash text.
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
		return (float) (y + yPos);
	}
}
