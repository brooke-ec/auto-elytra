package net.nimajnebec.autoelytra;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.nimajnebec.autoelytra.config.Keybinds;
import org.slf4j.LoggerFactory;

public class AutoElytra
{
	public static final int CHEST_SLOT = EquipmentSlot.CHEST.getIndex(Inventory.INVENTORY_SIZE);
	private static final Minecraft client = Minecraft.getInstance();
	public static final String MOD_ID = "autoelytra";

    public static void initialise(String mod_name, String mod_version, String platform) {
        LoggerFactory.getLogger(MOD_ID).info("{} v{} for {} successfully enabled!", mod_name, mod_version, platform);
        Keybinds.setup();
    }

	public static void sendMessage(Component component) {
        if (client.player == null) return;
        Component message = Component.literal("[AutoElytra] ").append(component);
        client.player.displayClientMessage(message, false);
	}
}
