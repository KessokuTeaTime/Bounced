package net.krlite.bounced;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.krlite.splasher.Splasher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class Bounced implements ModInitializer {
	public static final String NAME = "Bounced!", ID = "bounced";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	private static double primaryPos, secondaryPos;
	private static long startTime, initializationTime, thresholdOffset;
	private static final long
			primaryAnimationTime = 863 /* Animation time for the 'MINECRAFT' logo */ ,
			secondaryAnimationTime = 936 /* Animation time for the 'EDITION' banner and splash text */ ;
	private static final AtomicBoolean
			shouldAnimate = new AtomicBoolean(true),
			shouldJump = new AtomicBoolean(false);

	@Override
	public void onInitialize() {
		boolean isSplasherLoaded = FabricLoader.getInstance().isModLoaded("splasher");

		ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof TitleScreen) {
				ScreenMouseEvents.beforeMouseClick(screen)
						.register((currentScreen, mouseX, mouseY, button) -> {
							double centerX = scaledWidth / 2.0, y = 30, width = 310, height = 44;
							if (!isIntro()
										&& mouseX >= centerX - width / 2 && mouseX <= centerX + width / 2
										&& mouseY >= y && mouseY <= y + height
							) {
								if (!isSplasherLoaded || !Splasher.isMouseHovering(scaledWidth, mouseX, mouseY)) {
									// Linkage with Splasher
									push();
								}
							}
						});
			}
		});
	}

	public static void update() {
		if (isIntro()) {
			double offset = MinecraftClient.getInstance().getWindow().getScaledHeight() / 4.1;
			primaryPos = (shouldAnimate.get() ? 0 : easeOutBounce(primaryAnimationTime) * offset) - offset;
			secondaryPos = (shouldAnimate.get() ? 0 : easeOutBounce(secondaryAnimationTime) * offset) - offset;
		}
		else {
			double offset = MinecraftClient.getInstance().getWindow().getScaledHeight() / 7.0;

			if (shouldAnimate.get()) {
				primaryPos = 0;
				secondaryPos = 0;
			}
			else {
				if (shouldJump.get() && Math.max(Math.abs(-offset - primaryPos()), Math.abs(-offset - secondaryPos())) > 0.5) {
					primaryPos += (-offset - primaryPos()) * 0.26;
					secondaryPos += (-offset - secondaryPos()) * 0.23;

					startTime = System.currentTimeMillis();
				} else {
					shouldJump.set(false);

					primaryPos = easeOutBounce(primaryAnimationTime) * offset - offset;
					secondaryPos = easeOutBounce(secondaryAnimationTime) * offset - offset;
				}
			}
		}
	}

	public static double primaryPos() {
		return primaryPos;
	}

	public static double secondaryPos() {
		return secondaryPos;
	}

	public static long totalAnimationTime() {
		return Math.max(primaryAnimationTime, secondaryAnimationTime);
	}

	public static boolean isIntro() {
		return System.currentTimeMillis() - (initializationTime + thresholdOffset) <= totalAnimationTime();
	}

	public static void init() {
		initializationTime = System.currentTimeMillis();
		thresholdOffset = -1;
		shouldJump.set(false);
	}

	public static void push() {
		shouldAnimate.set(true);
		shouldJump.set(true);
	}

	public static void resetWhen(boolean condition) {
		if (condition && shouldAnimate.getAndSet(false)) {
			startTime = System.currentTimeMillis();
			if (thresholdOffset == -1) thresholdOffset = System.currentTimeMillis() - initializationTime;
		}
	}

	public static double easeOutBounce(double animationTime) {
		double progress = Math.min((System.currentTimeMillis() - startTime) / animationTime, 1);

		if (progress < 1 / 2.75) {
			return 7.5625 * progress * progress;
		} else if (progress < 2 / 2.75) {
			progress -= 1.5 / 2.75;
			return 7.5625 * progress * progress + 0.75;
		} else if (progress < 2.5 / 2.75) {
			progress -= 2.25 / 2.75;
			return 7.5625 * progress * progress + 0.9375;
		} else {
			progress -= 2.625 / 2.75;
			return 7.5625 * progress * progress + 0.984375;
		}
	}
}
