package net.krlite.bounced.fabric;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import net.fabricmc.api.ClientModInitializer;
import net.krlite.bounced.Bounced;
import net.krlite.splasher.Splasher;
import net.minecraft.client.gui.screen.TitleScreen;

public class BouncedClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
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
