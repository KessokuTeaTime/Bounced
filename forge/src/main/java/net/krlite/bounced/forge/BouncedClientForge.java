package net.krlite.bounced.forge;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import dev.architectury.platform.forge.EventBuses;
import net.krlite.bounced.Bounced;
import net.krlite.splasher.Splasher;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

@Mod(Bounced.ID)
public class BouncedClientForge {
    public BouncedClientForge() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        EventBuses.registerModEventBus(Bounced.ID, FMLJavaModLoadingContext.get().getModEventBus());
        IEventBus modEventBus = EventBuses.getModEventBus(Bounced.ID).get();

        modEventBus.addListener(this::onInitializeClient);
    }

    public void onInitializeClient(FMLClientSetupEvent event) {
        ClientGuiEvent.INIT_POST.register((screen, screenAccess) -> {
            if (screen instanceof TitleScreen) {
                ClientScreenInputEvent.MOUSE_CLICKED_POST.register((client, currentScreen, mouseX, mouseY, button) -> {
                    double scaledWidth = screenAccess.getScreen().width;
                    double centerX = scaledWidth / 2.0, y = 30, width = 310, height = 44;
                    if (!Bounced.isIntro()
                                    && mouseX >= centerX - width / 2 && mouseX <= centerX + width / 2
                                    && mouseY >= y && mouseY <= y + height
                    ) {

                        if (!Bounced.isSplasherLoaded || !Splasher.isMouseHovering(scaledWidth, mouseX, mouseY)) {
                            // Linkage with Splasher
                            Bounced.push();
                        }
                    }
                    return EventResult.pass();
                });
            }
        });
    }
}