package net.nimajnebec.autoelytra;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class Configuration {
    private static boolean autoEquipEnabled = true;

    public static void setAutoEquipEnabled(boolean value) {
        autoEquipEnabled = value;
    }

    public static boolean isAutoEquipEnabled() {
        return autoEquipEnabled;
    }

    public static Screen createScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(Component.translatable("title.autoelytra.config"))
                .setParentScreen(parent);

        ConfigCategory general = builder.getOrCreateCategory(Component.empty());
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Auto Equip Enabled
        general.addEntry(entryBuilder.startBooleanToggle(
                Component.translatable("option.autoelytra.equip.enabled"), isAutoEquipEnabled())
                .setTooltip(Component.translatable("toolip.autoelytra.equip.enabled"))
                .setSaveConsumer(Configuration::setAutoEquipEnabled)
                .build());

        // Auto Equip Toggle Keybind
        general.addEntry(entryBuilder.fillKeybindingField(
                Component.translatable("key.autoelytra.toggle.equip"), Keybinds.TOGGLE_AUTO_EQUIP)
                .setTooltip(Component.translatable("tooltip.autoelytra.toggle.equip"))
                .build());

        return builder.build();
    }

}
