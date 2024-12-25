package net.nimajnebec.autoelytra.feature;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutoEquipController {
    private static final Set<DataComponentType<?>> BLACKLISTED_COMPONENTS = new HashSet<>(List.of(DataComponents.DAMAGE));
    @Nullable
    private static DataComponentMap previousChestDataComponents;

    public static void setPreviousChestItem(ItemStack item) {
        if (!item.isEmpty()) previousChestDataComponents = item.getComponents();
    }

    public static boolean hasPreviousChestItem() {
        return previousChestDataComponents != null;
    }

    public static void resetPreviousChestItem() {
        previousChestDataComponents = null;
    }

    public static boolean matchesPreviousChestItem(ItemStack item) {
        Set<DataComponentType<?>> components = item.getComponents().keySet();

        boolean matches = true;

        if (previousChestDataComponents == null || components.isEmpty()) return false;

        for (DataComponentType<?> type : components) {
            if (!BLACKLISTED_COMPONENTS.contains(type)) {
                if (item.getComponents().get(type) != previousChestDataComponents.get(type)) {
                    matches = false;
                }
            }
        }

        return matches;
    }
}
