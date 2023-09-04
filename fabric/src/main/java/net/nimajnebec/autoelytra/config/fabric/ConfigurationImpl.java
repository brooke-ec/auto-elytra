package net.nimajnebec.autoelytra.config.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Optional;

public class ConfigurationImpl {
    public static final String CLOTH_MODID = "cloth-config";

    public static boolean ClothConfigInstalled() {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(CLOTH_MODID);
        return modContainer.isPresent();
    }
}
