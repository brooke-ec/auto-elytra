package net.nimajnebec.autoelytra.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.nimajnebec.autoelytra.AutoElytra;

@Mod(AutoElytra.MOD_ID)
public class AutoElytraForge {

    public AutoElytraForge() {
        ModFileInfo info = FMLLoader.getLoadingModList().getModFileById(AutoElytra.MOD_ID);
        AutoElytra.sendEnabledMessage(info.moduleName(), info.versionString(), "forge");
    }

}