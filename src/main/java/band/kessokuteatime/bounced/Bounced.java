package band.kessokuteatime.bounced;

//import band.kessokuteatime.splasher.Splasher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@Mod(value = Bounced.ID, dist = Dist.CLIENT)
public class Bounced {
	public static final String NAME = "Bounced!", ID = "bounced";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	private static double primaryPos, secondaryPos;
	private static long startTime = -1, initializationTime = -1, thresholdOffset;
	private static final long
			primaryAnimationTime = 863 /* Animation time for the 'MINECRAFT' logo */ ,
			secondaryAnimationTime = 936 /* Animation time for the 'EDITION' banner and splash text */ ;
	private static final AtomicBoolean
			shouldAnimate = new AtomicBoolean(true),
			shouldJump = new AtomicBoolean(false);

    public Bounced() {
        if (FMLLoader.getDist().isClient()) {
            this.onInitializeClient();
        }
    }

	public void onInitializeClient() {
		boolean isSplasherLoaded = ModList.get().isLoaded("splasher");
        IEventBus neoForgeEventBus = NeoForge.EVENT_BUS;

        /*
        neoForgeEventBus.addListener(ScreenEvent.Init.Post.class, screenInitEvent -> {
            Screen screen = screenInitEvent.getScreen();
			if (screen instanceof TitleScreen || screen instanceof AccessibilityOnboardingScreen) {
                neoForgeEventBus.addListener(ScreenEvent.MouseButtonPressed.Post.class, screenMousePressedEvent -> {
                    double mouseX = screenMousePressedEvent.getMouseX();
                    double mouseY = screenMousePressedEvent.getMouseY();
                    double scaledWidth = screen.width;

                    double centerX = scaledWidth / 2.0, y = 30 + primaryPos(), width = 310, height = 44;
                    if (!isIntro()
                            && mouseX >= centerX - width / 2 && mouseX <= centerX + width / 2
                            && mouseY >= y && mouseY <= y + height
                    ) {
                        // Linkage with Splasher
                        if (!isSplasherLoaded || !Splasher.isMouseHovering(scaledWidth, mouseX, mouseY))
                            push();
                    }
                });
			}
		});
         */
	}

	public static double offset(boolean isIntro) {
		return MinecraftClient.getInstance().getWindow().getScaledHeight() / (isIntro ? 4.1 : 7.0);
	}

	public static void update() {
		double offset = offset(isIntro());
		if (isIntro()) {
			primaryPos = (shouldAnimate.get() ? 0 : easeOutBounce(primaryAnimationTime) * offset) - offset;
			secondaryPos = (shouldAnimate.get() ? 0 : easeOutBounce(secondaryAnimationTime) * offset) - offset;
		} else {
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
		init(false);
	}

	public static void init(boolean force) {
		if (force || !isIntro()) {
			initializationTime = System.currentTimeMillis();
			thresholdOffset = -1;
			shouldJump.set(false);
		}
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
