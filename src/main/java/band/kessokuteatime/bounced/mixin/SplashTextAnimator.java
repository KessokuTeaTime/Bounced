package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.gui.screen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(SplashTextRenderer.class)
class SplashTextAnimator {
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
