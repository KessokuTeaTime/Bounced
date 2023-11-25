package band.kessokuteatime.bounced.forge;

import band.kessokuteatime.bounced.Bounced;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(Bounced.ID)
public class BouncedForge {
    public BouncedForge() {
        EventBuses.registerModEventBus(Bounced.ID, FMLJavaModLoadingContext.get().getModEventBus());
        if (FMLLoader.getDist().isClient()) {
            Bounced.onInitialize();
        }
    }
}
