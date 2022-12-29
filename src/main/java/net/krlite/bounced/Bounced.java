package net.krlite.bounced;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class Bounced implements ModInitializer {
	public static final String MOD_ID = "bounced";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Pusher PUSHER = new Pusher(true);

	public record Timer(long origin, long lasting) {
		public Timer(long lasting) {
			this(System.currentTimeMillis(), lasting);
		}

		public Timer reset() {
			return new Timer(lasting);
		}

		public long queue() {
			return Math.min(queueElapsed(), lasting);
		}

		public long queueElapsed() {
			return System.currentTimeMillis() - origin;
		}
	}

	public record Pusher(AtomicBoolean ready) {
		public Pusher(boolean ready) {
			this(new AtomicBoolean(ready));
		}

		public void push() {
			ready.set(true);
		}

		public boolean pull() {
			return ready.get() && ready.getAndSet(false);
		}

		public void and(boolean and, Runnable runnable) {
			if (and && pull()) runnable.run();
		}
	}

	@Override
	public void onInitialize() {
	}

	/**
	 * Easing bounce function out, for pretty animations.
	 * @param origin	The origin value.
	 * @param shift		The shift value.
	 * @param timer		The timer.
	 * @return			The eased value.
	 */
	public static double easeOutBounce(double origin, double shift, Timer timer) {
		double progress = timer.queue();
		if ((progress /= timer.lasting()) <= (1 / 2.75)) {
			return shift * 7.5625 * Math.pow(progress, 2) + origin;
		} else if (progress < (2 / 2.75)) {
			return shift * (7.5625 * (progress -= (1.5 / 2.75)) * progress + 0.75) + origin;
		} else if (progress < (2.5 / 2.75)) {
			return shift * (7.5625 * (progress -= (2.25 / 2.75)) * progress + 0.9375) + origin;
		} else {
			return shift * (7.5625 * (progress -= (2.625 / 2.75)) * progress + 0.984375) + origin;
		}
	}
}
