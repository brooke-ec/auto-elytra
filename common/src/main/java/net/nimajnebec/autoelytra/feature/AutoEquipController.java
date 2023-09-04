package net.nimajnebec.autoelytra.feature;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AutoEquipController {
    private static final NbtPathArgument.NbtPath[] BLACKLISTED_TAGS = parseTagPaths("tag.Damage"); // List of tags to ignore when searching for previous chest item
    @Nullable
    private static CompoundTag previousChestTag;

    public static void setPreviousChestItem(ItemStack item) {
        if (!item.isEmpty()) previousChestTag = getFilteredTag(item);
    }

    public static boolean hasPreviousChestItem() {
        return previousChestTag != null;
    }

    public static void resetPreviousChestItem() {
        previousChestTag = null;
    }

    public static boolean matchesPreviousChestItem(ItemStack item) {
        return !item.isEmpty() && getFilteredTag(item).equals(previousChestTag);
    }

    private static CompoundTag getFilteredTag(ItemStack itemStack) {
        CompoundTag tag = itemStack.save(new CompoundTag());

        // Remove all blacklisted tags
        for (NbtPathArgument.NbtPath path : BLACKLISTED_TAGS) {
            path.remove(tag);
        }

        return tag;
    }

    private static NbtPathArgument.NbtPath[] parseTagPaths(String... args) {
        NbtPathArgument.NbtPath[] result = new NbtPathArgument.NbtPath[args.length];
        for (int i = 0; i < args.length; i++) {
            try {
                // Use data command's path parser for blacklist
                result[i] = NbtPathArgument.nbtPath().parse(new StringReader(args[i]));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
