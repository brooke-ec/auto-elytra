package ec.brroke.autoelytra.neoforge;

import net.neoforged.fml.common.Mod;

import ec.brooke.autoelytra.AutoElytra;

@Mod(AutoElytra.MOD_ID)
public final class AutoElytraNeoForge {
    public AutoElytraNeoForge() {
        // Run our common setup.
        AutoElytra.init();
    }
}
