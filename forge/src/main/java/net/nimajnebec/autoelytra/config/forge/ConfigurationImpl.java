package net.nimajnebec.autoelytra.config.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ConfigurationImpl {
    public static final String CLOTH_MODID = "cloth_config";

    public static boolean ClothConfigInstalled() {
        return ModList.get().isLoaded(CLOTH_MODID);
    }

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
