package net.krlite.bounced;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class Bounced implements ModInitializer {
	public static final String NAME = "Bounced!", ID = "bounced";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	private static double yPos;
	private static long startTime;
	private static final long animationTime = 863;
	private static final AtomicBoolean shouldAnimate = new AtomicBoolean(true);

	@Override
	public void onInitialize() {
	}

	public static void update() {
		double offset = MinecraftClient.getInstance().getWindow().getScaledHeight() / 4.1;
		yPos = (shouldAnimate.get () ? 0 : easeOutBounce() * offset) - offset;
	}

	public static double getPos() {
		return yPos;
	}

	public static void push() {
		shouldAnimate.set(true);
	}

	public static void resetWhen(boolean condition) {
		if (condition && shouldAnimate.getAndSet(false))
			startTime = System.currentTimeMillis();
	}

	public static double easeOutBounce() {
		double progress = Math.min((double) (System.currentTimeMillis() - startTime) / animationTime, 1);

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
