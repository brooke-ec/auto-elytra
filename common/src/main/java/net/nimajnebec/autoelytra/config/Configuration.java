package net.nimajnebec.autoelytra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.screens.Screen;
import net.nimajnebec.autoelytra.AutoElytra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    private static final List<ConfigOption<Object>> options = new ArrayList<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final ConfigOption<Boolean> AUTO_EQUIP_ENABLED = registerOption(new ConfigOption<>("auto_equip_enabled", true));

    public static void load() {
        AutoElytra.logger.info("Loading Auto Elytra config...");
        String data;

        try {
            data = Files.readString(getConfigFile());

            Map<?, ?> map = gson.fromJson(data, Map.class);
            for (ConfigOption<Object> option: options) {
                option.set(map.get(option.getName()));
            }
        }
        catch (Exception ignored) {}
    }

    public static void save() {
        AutoElytra.logger.info("Saving Auto Elytra config...");

        Map<String, Object> map = new HashMap<>();
        for (ConfigOption<Object> option: options) {
            map.put(option.getName(), option.get());
        }

        try {
            Files.writeString(getConfigFile(), gson.toJson(map));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Screen createScreen(Screen parent)  {
        /* AutoElytra uses cloth config to generate configs screens
        If the cloth config dependency is not found do not attempt to generate the screen. */
        if (!Configuration.ClothConfigInstalled()) return null;
        return ClothConfigFactory.create(parent);
    }

    @ExpectPlatform
    public static boolean ClothConfigInstalled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }

    public static Path getConfigFile() {
        return getConfigDirectory().resolve(AutoElytra.MOD_ID + ".json");
    }

    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        options.add((ConfigOption<Object>) option);
        return option;
    }
}
