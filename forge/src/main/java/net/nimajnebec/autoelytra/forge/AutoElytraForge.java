package net.nimajnebec.autoelytra.forge;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.nimajnebec.autoelytra.AutoElytra;
import net.nimajnebec.autoelytra.config.Configuration;

@Mod(AutoElytra.MOD_ID)
public class AutoElytraForge {

    public AutoElytraForge() {
        // Register Config Screen
        if (Configuration.ClothConfigInstalled())
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> Configuration.createScreen(parent)));

        ModFileInfo info = FMLLoader.getLoadingModList().getModFileById(AutoElytra.MOD_ID);
        AutoElytra.initialise(info.moduleName(), info.versionString(), "forge");
    }

}