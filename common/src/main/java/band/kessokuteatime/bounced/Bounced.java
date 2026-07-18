package band.kessokuteatime.bounced;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

public final class Bounced {
	public static final String ID = "bounced";

	private static final long PRIMARY_ANIMATION_TIME = 863;
	private static final long SECONDARY_ANIMATION_TIME = 936;

	private static double primaryPosition;
	private static double secondaryPosition;
	private static long startTime = -1;
	private static long initializationTime = -1;
	private static long thresholdOffset;
	private static boolean shouldAnimate = true;
	private static boolean shouldJump;

	private Bounced() {
	}

	public static void startIntro() {
		initializationTime = Util.getMillis();
		thresholdOffset = -1;
		shouldAnimate = true;
		shouldJump = false;
	}

	public static void init() {
		init(false);
	}

	public static void init(boolean force) {
		if (force || !isIntro()) {
			startIntro();
		}
	}

	public static void push() {
		shouldAnimate = true;
		shouldJump = true;
	}

	public static void resetWhen(boolean condition) {
		if (!condition || !shouldAnimate) {
			return;
		}

		long now = Util.getMillis();
		shouldAnimate = false;
		startTime = now;
		if (thresholdOffset == -1) {
			thresholdOffset = now - initializationTime;
		}
	}

	public static void update() {
		long now = Util.getMillis();
		boolean intro = isIntro(now);
		double offset = offset(intro);

		if (intro) {
			primaryPosition = (shouldAnimate ? 0 : easeOutBounce(PRIMARY_ANIMATION_TIME, now) * offset) - offset;
			secondaryPosition = (shouldAnimate ? 0 : easeOutBounce(SECONDARY_ANIMATION_TIME, now) * offset) - offset;
			return;
		}

		if (shouldAnimate) {
			primaryPosition = 0;
			secondaryPosition = 0;
			return;
		}

		if (shouldJump && Math.max(
				Math.abs(-offset - primaryPosition),
				Math.abs(-offset - secondaryPosition)
		) > 0.5) {
			primaryPosition += (-offset - primaryPosition) * 0.26;
			secondaryPosition += (-offset - secondaryPosition) * 0.23;
			startTime = now;
			return;
		}

		shouldJump = false;
		primaryPosition = easeOutBounce(PRIMARY_ANIMATION_TIME, now) * offset - offset;
		secondaryPosition = easeOutBounce(SECONDARY_ANIMATION_TIME, now) * offset - offset;
	}

	public static double primaryPosition() {
		return primaryPosition;
	}

	public static double primaryPos() {
		return primaryPosition;
	}

	public static double secondaryPosition() {
		return secondaryPosition;
	}

	public static double secondaryPos() {
		return secondaryPosition;
	}

	public static long totalAnimationTime() {
		return SECONDARY_ANIMATION_TIME;
	}

	public static boolean isIntro() {
		return isIntro(Util.getMillis());
	}

	public static boolean isLogoHovered(int screenWidth, double mouseX, double mouseY) {
		double left = screenWidth / 2.0 - 155;
		double top = 30 + primaryPosition;
		return mouseX >= left && mouseX <= left + 310
				&& mouseY >= top && mouseY <= top + 44;
	}

	public static double offset(boolean intro) {
		return Minecraft.getInstance().getWindow().getGuiScaledHeight() / (intro ? 4.1 : 7.0);
	}

	public static double easeOutBounce(double animationTime) {
		return easeOutBounce((long) animationTime, Util.getMillis());
	}

	private static boolean isIntro(long now) {
		return now - (initializationTime + thresholdOffset) <= SECONDARY_ANIMATION_TIME;
	}

	private static double easeOutBounce(long animationTime, long now) {
		double progress = Math.min((double) (now - startTime) / animationTime, 1);

		if (progress < 1 / 2.75) {
			return 7.5625 * progress * progress;
		}
		if (progress < 2 / 2.75) {
			progress -= 1.5 / 2.75;
			return 7.5625 * progress * progress + 0.75;
		}
		if (progress < 2.5 / 2.75) {
			progress -= 2.25 / 2.75;
			return 7.5625 * progress * progress + 0.9375;
		}

		progress -= 2.625 / 2.75;
		return 7.5625 * progress * progress + 0.984375;
	}
}
