package net.nimajnebec.autoelytra.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.nimajnebec.autoelytra.AutoElytra;

public class AutoElytraFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoElytra.init();
    }
}