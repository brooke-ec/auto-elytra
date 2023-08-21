package net.nimajnebec.autoelytra;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.nimajnebec.autoelytra.features.AutoEquipController;

import java.util.HashMap;
import java.util.Map;

public class Keybinds {
    protected static final Map<KeyMapping, Runnable> KEYBINDS = new HashMap<>();
    public static final KeyMapping TOGGLE_AUTO_EQUIP = registerKeybind(
        new KeyMapping("key.autoelytra.toggle.equip", InputConstants.Type.KEYSYM, -1, "key.categories.inventory"),
        () -> {
            if (Minecraft.getInstance().screen != null) return; // Only allow when no screen is open

            boolean enabling = !Configuration.isAutoEquipEnabled();

            Configuration.setAutoEquipEnabled(enabling);
            MutableComponent message = Component.literal("Auto Elytra Equip is Now ");

            if (enabling) {
                AutoEquipController.resetPreviousChestItem();
                message.append(Component.literal("Enabled").withStyle(ChatFormatting.GREEN));
            } else message.append(Component.literal("Disabled").withStyle(ChatFormatting.RED));

            AutoElytra.sendMessage(message);
    });

    private static KeyMapping registerKeybind(KeyMapping mapping, Runnable action) {
        KEYBINDS.put(mapping, action);
        return mapping;
    }

    @ExpectPlatform
    public static void setup() {
        throw new AssertionError();
    }
}
