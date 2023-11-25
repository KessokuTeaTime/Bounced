package band.kessokuteatime.bounced.neoforge;

import band.kessokuteatime.bounced.Bounced;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod(Bounced.ID)
public class BouncedNeoForge {
    public BouncedNeoForge() {
        if (FMLLoader.getDist().isClient()) {
            Bounced.onInitialize();
        }
    }
}
