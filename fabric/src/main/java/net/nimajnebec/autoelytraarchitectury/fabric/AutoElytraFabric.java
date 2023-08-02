package net.nimajnebec.autoelytraarchitectury.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.nimajnebec.autoelytraarchitectury.AutoElytra;

public class AutoElytraFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoElytra.init();
    }
}