package net.nimajnebec.autoelytra.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nimajnebec.autoelytra.config.Configuration;
import net.nimajnebec.autoelytra.features.AutoEquipController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static net.nimajnebec.autoelytra.AutoElytra.CHEST_SLOT;

@Mixin(value = LocalPlayer.class)
public class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow @Final protected Minecraft minecraft;

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/player/LocalPlayer;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
    private void tryEquipElytra(CallbackInfo ci) {
        if (!Configuration.isAutoEquipEnabled()) return;

        if (this.autoelytra$canStartFlying()) {
            List<ItemStack> inventory = this.autoelytra$getCombinedInventory();

            // Return if elytra is already equipped
            if (inventory.get(CHEST_SLOT).is(Items.ELYTRA)) return;

            // Find elytra in inventory
            for (int slot = 0; slot < inventory.size(); slot++) {
                ItemStack stack = inventory.get(slot);

                if (stack.is(Items.ELYTRA)) {
                    AutoEquipController.setPreviousChestItem(inventory.get(CHEST_SLOT));
                    this.autoelytra$swapSlots(slot, CHEST_SLOT);
                    return;
                }
            }
        }
    }

    @Unique private boolean autoelytra$canStartFlying() {
        return !this.onGround() && !this.isFallFlying() && !this.isInWater() && !this.hasEffect(MobEffects.LEVITATION);
    }

    @Inject(method = "aiStep", at = @At(value = "TAIL"))
    private void unequipElytra(CallbackInfo ci) {
        if (!Configuration.isAutoEquipEnabled()) return;

        List<ItemStack> inventory = this.autoelytra$getCombinedInventory();

        // Check if just stopped flying
        if (AutoEquipController.hasPreviousChestItem() && !this.isFallFlying() && inventory.get(CHEST_SLOT).is(Items.ELYTRA)) {

            // Find previous chest item
            for (int slot = 0; slot < inventory.size(); slot++) {
                if (AutoEquipController.matchesPreviousChestItem(inventory.get(slot))) {
                    this.autoelytra$swapSlots(slot, CHEST_SLOT);
                    break;
                }
            }

            AutoEquipController.resetPreviousChestItem();
        }
    }

    @Unique private void autoelytra$swapSlots(int slotA, int slotB) {

        // Convert inventory slot to menu slot
        int slotAMenu = -1;
        NonNullList<Slot> slots = this.inventoryMenu.slots;
        for (int i = 5; i < slots.size(); i++) {  // Start at 5 to skip crafting grid
            if (slots.get(i).getContainerSlot() == slotA) {
                slotAMenu = i;
                break;
            }
        }

        assert slotAMenu > -1;
        assert this.minecraft.gameMode != null;
        this.minecraft.gameMode.handleInventoryMouseClick(this.inventoryMenu.containerId, slotAMenu, slotB, ClickType.SWAP, this);
    }

    @Unique private List<ItemStack> autoelytra$getCombinedInventory() {
        Inventory inventory = this.getInventory();

        List<ItemStack> result = new ArrayList<>();
        for (NonNullList<ItemStack> compartment : inventory.compartments) {
            result.addAll(compartment);
        }

        return result;
    }
}
