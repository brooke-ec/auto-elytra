package net.nimajnebec.autoelytra.config;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.screens.Screen;

public class Configuration {
    public static final String CLOTH_MODID = "cloth-config";
    private static boolean autoEquipEnabled = true;

    public static void setAutoEquipEnabled(boolean value) {
        autoEquipEnabled = value;
    }

    public static boolean isAutoEquipEnabled() {
        return autoEquipEnabled;
    }

    public static Screen createScreen(Screen parent)  {
        /* AutoElytra uses cloth config to generate configs screens
        If the cloth config dependency is not found do not attempt to generate the screen. */
        if (!Configuration.ClothConfigInstalled()) return null;;
        return ClothConfigFactory.create(parent);
    }

    @ExpectPlatform
    public static boolean ClothConfigInstalled() {
        throw new AssertionError();
    }
}
