package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.gui.components.SplashRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SplashRenderer.class)
public abstract class SplashRendererAnimator {
	@ModifyArg(
			method = "extractRenderState",
			at = @At(
					value = "INVOKE",
					target = "Lorg/joml/Matrix3x2f;translate(FF)Lorg/joml/Matrix3x2f;"
			),
			index = 1
	)
	private float bounced$moveSplash(float y) {
		return y + (float) Bounced.secondaryPosition();
	}
}
