package net.nimajnebec.autoelytra.config.forge;

import net.minecraftforge.fml.ModList;

public class ConfigurationImpl {
    public static final String CLOTH_MODID = "cloth_config";

    public static boolean ClothConfigInstalled() {
        return ModList.get().isLoaded(CLOTH_MODID);
    }
}
