package net.nimajnebec.autoelytra.config.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.nimajnebec.autoelytra.config.Configuration;

import java.util.Optional;

public class ConfigurationImpl {
    public static boolean ClothConfigInstalled() {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(Configuration.CLOTH_MODID);
        return modContainer.isPresent();
    }
}
