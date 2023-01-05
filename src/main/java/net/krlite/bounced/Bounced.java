package net.krlite.bounced;

import net.fabricmc.api.ModInitializer;
import net.krlite.equator.util.Pusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class Bounced implements ModInitializer {
	public static final String MOD_ID = "bounced";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Pusher PUSHER = new Pusher(true);

	@Override
	public void onInitialize() {}
}
