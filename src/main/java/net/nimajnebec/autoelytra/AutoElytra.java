package net.nimajnebec.autoelytra;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.nbt.NbtCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class AutoElytra implements ClientModInitializer {
    public static final Logger logger = LogManager.getLogger();
    @Nullable private NbtCompound previousChestItemNbt;

    private static AutoElytra instance;

    public AutoElytra() {
        instance = this;
    }

    @Override
    public void onInitializeClient() {}

    public void setPreviousChestItemNbt(@Nullable NbtCompound value) {
        this.previousChestItemNbt = value;
    }

    public @Nullable NbtCompound getPreviousChestItemNbt() {
        return this.previousChestItemNbt;
    }

    public static AutoElytra getInstance() {
        return instance;
    }
}