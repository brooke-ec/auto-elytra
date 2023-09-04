package net.nimajnebec.autoelytra.config;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigFactory {
    public static Screen create(Screen parent) {
        var builder = me.shedaniel.clothconfig2.api.ConfigBuilder.create()
                .setSavingRunnable(Configuration::save)
                .setTitle(Component.translatable("title.autoelytra.config"))
                .setParentScreen(parent);

        var general = builder.getOrCreateCategory(Component.empty());
        var entryBuilder = builder.entryBuilder();

        // Auto Equip Enabled
        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("option.autoelytra.equip.enabled"), Configuration.AUTO_EQUIP_ENABLED.get())
                .setTooltip(Component.translatable("toolip.autoelytra.equip.enabled"))
                .setSaveConsumer(Configuration.AUTO_EQUIP_ENABLED::set)
                .build());

        // Auto Equip Toggle Keybind
        general.addEntry(entryBuilder.fillKeybindingField(
                        Component.translatable("key.autoelytra.toggle.equip"), Keybinds.TOGGLE_AUTO_EQUIP)
                .setTooltip(Component.translatable("tooltip.autoelytra.toggle.equip"))
                .build());

        return builder.build();
    }
}
