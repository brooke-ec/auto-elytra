package net.nimajnebec.autoelytra;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AutoElytra
{
	private static final String[] BLACKLISTED_TAGS = new String[] {};
	public static final int CHEST_SLOT = EquipmentSlot.CHEST.getIndex(Inventory.INVENTORY_SIZE);
	public static final String ENABLED_MESSAGE = "%s v%s for %s successfully enabled!";
	public static final String MOD_ID = "autoelytra";
	@Nullable private static CompoundTag previousChestTag;

	public static void setPreviousChestItem(ItemStack item) {
		if (!item.isEmpty()) previousChestTag = filterTag(item.getOrCreateTag());
	}

	public static boolean hasPreviousChestItem() {
		return previousChestTag != null;
	}

	public static void resetPreviousChestItem() {
		previousChestTag = null;
	}

	public static boolean equalsPreviousChestItem(ItemStack item) {
		return filterTag(item.getOrCreateTag()).equals(previousChestTag);
	}

	private static CompoundTag filterTag(CompoundTag tag) {
		return tag;
	}
}
