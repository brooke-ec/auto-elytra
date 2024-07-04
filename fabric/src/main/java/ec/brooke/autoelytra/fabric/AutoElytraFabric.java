package ec.brooke.autoelytra.fabric;

import net.fabricmc.api.ClientModInitializer;

import ec.brooke.autoelytra.AutoElytra;

public final class AutoElytraFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        // Run our common setup.
        AutoElytra.init();
    }
}
