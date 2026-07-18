package band.kessokuteatime.bounced.mixin;

import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LogoRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LogoRenderer.class)
public abstract class LogoRendererAnimator {
	@Inject(
			method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V",
					ordinal = 0,
					shift = At.Shift.BEFORE
			)
	)
	private void bounced$moveLogoBefore(
			GuiGraphicsExtractor graphics,
			int screenWidth,
			float alpha,
			int y,
			CallbackInfo ci
	) {
		graphics.pose().pushMatrix().translate(0, (float) Bounced.primaryPosition());
	}

	@Inject(
			method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V",
					ordinal = 0,
					shift = At.Shift.AFTER
			)
	)
	private void bounced$moveLogoAfter(
			GuiGraphicsExtractor graphics,
			int screenWidth,
			float alpha,
			int y,
			CallbackInfo ci
	) {
		graphics.pose().popMatrix();
	}

	@Inject(
			method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V",
					ordinal = 1,
					shift = At.Shift.BEFORE
			)
	)
	private void bounced$moveEditionBefore(
			GuiGraphicsExtractor graphics,
			int screenWidth,
			float alpha,
			int y,
			CallbackInfo ci
	) {
		graphics.pose().pushMatrix().translate(0, (float) Bounced.secondaryPosition());
	}

	@Inject(
			method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V",
					ordinal = 1,
					shift = At.Shift.AFTER
			)
	)
	private void bounced$moveEditionAfter(
			GuiGraphicsExtractor graphics,
			int screenWidth,
			float alpha,
			int y,
			CallbackInfo ci
	) {
		graphics.pose().popMatrix();
	}
}
