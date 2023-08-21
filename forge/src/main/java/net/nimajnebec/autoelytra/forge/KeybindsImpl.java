package net.nimajnebec.autoelytra.forge;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.nimajnebec.autoelytra.Keybinds;

import java.util.Map;

public class KeybindsImpl extends Keybinds {
    public static void handleKeybinds(InputEvent.Key event) {
        for (Map.Entry<KeyMapping, Runnable> binding : KEYBINDS.entrySet()) {
            while (binding.getKey().consumeClick()) binding.getValue().run();
        }
    }

    public static void onRegisterKeybinds(RegisterKeyMappingsEvent event) {
        for (Map.Entry<KeyMapping, Runnable> binding : KEYBINDS.entrySet()) {
            event.register(binding.getKey());
        }
    }

    public static void setup() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(KeybindsImpl::handleKeybinds);
        modEventBus.addListener(KeybindsImpl::onRegisterKeybinds);
    }
}
