package band.kessokuteatime.bounced.mixin;


import band.kessokuteatime.bounced.Bounced;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This class is responsible for triggering the title animation before the title screen is rendered.
 */
@Mixin(MinecraftClient.class)
public class Trigger {
    @Inject(method = "setScreen", at = @At("TAIL"))
    private void trigger(Screen screen, CallbackInfo ci) {
        if (!(screen instanceof TitleScreen || screen instanceof AccessibilityOnboardingScreen)) {
            Bounced.push();
        }
    }
}

/**
 * This class is responsible for triggering the title animation when the game starts for the first time.
 */
@Mixin(AccessibilityOnboardingScreen.class)
class AccessibilityOnboardingTrigger {
    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        Bounced.init(true);
    }

    /**
     * Triggers and restarts the animation.
     */
    @ModifyArg(
            method = "renderPanoramaBackground",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(Lnet/minecraft/client/gui/DrawContext;IIFF)V"
            ), index = 3
    )
    private float trigger(float progress) {
        Bounced.resetWhen(progress > 0.9);
        Bounced.update();
        return progress;
    }
}

/**
 * This class is responsible for triggering the animation every time the title screen is shown.
 */
@Mixin(TitleScreen.class)
class TitleScreenTrigger {
    @Shadow
    private boolean doBackgroundFade;

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        Bounced.init(true);
    }

    /**
     * Triggers the animation when background is fading.
     */
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;clampedMap(FFFFF)F"
            )
    )
    private float trigger(float value, float oldStart, float oldEnd, float newStart, float newEnd) {
        float clamped = MathHelper.clampedMap(value, oldStart, oldEnd, newStart, newEnd);
        Bounced.resetWhen(clamped > 0.9);
        return clamped;
    }

    /**
     * Triggers and restarts the animation at normal render phases.
     */
    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void trigger(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Bounced.resetWhen(!doBackgroundFade);
        Bounced.update();
    }
}
