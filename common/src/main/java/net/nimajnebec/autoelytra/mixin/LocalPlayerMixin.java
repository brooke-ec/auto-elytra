package net.nimajnebec.autoelytra.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.nimajnebec.autoelytra.config.Configuration;
import net.nimajnebec.autoelytra.feature.AutoEquipController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

    // See https://github.com/NimajnebEC/auto-elytra/issues/11
    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onClimbable()Z"))
    private boolean patchLavaFlight(LocalPlayer instance) {
        return onClimbable() || isInLava();
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/player/LocalPlayer;tryToStartFallFlying()Z"))
    private void tryEquipElytra(CallbackInfo ci) {
        if (!Configuration.AUTO_EQUIP_ENABLED.get()) return;

        if (this.autoelytra$canStartFlying()) {
            List<ItemStack> inventory = this.autoelytra$getCombinedInventory();

            // Return if elytra is already equipped
            if (this.getItemBySlot(EquipmentSlot.CHEST).has(DataComponents.GLIDER)) return;

            ItemStack bestElytra = null;
            int finalSlot = 0;

            // Find best elytra in inventory
            for (int slot = 0; slot < inventory.size(); slot++) {
                ItemStack stack = inventory.get(slot);

                if (stack.has(DataComponents.GLIDER)) {
                    // TODO Implement calculating effective durability with unbreaking (multiply by unbreaking level)
                    // Use EnchantmentHelper#getLevel and get the Enchantment from the dynamic registry manager of the clients world

                    int effectiveDurability = stack.getMaxDamage() - stack.getDamageValue();
                    if (bestElytra == null || effectiveDurability > (bestElytra.getMaxDamage() - bestElytra.getDamageValue())) {
                        bestElytra = stack;
                        finalSlot = slot;
                    }
                }
            }

            if (bestElytra == null) return;

            AutoEquipController.setPreviousChestItem(this.getItemBySlot(EquipmentSlot.CHEST));
            this.autoelytra$swapSlots(CHEST_SLOT, finalSlot);
        }
    }

    @Unique private boolean autoelytra$canStartFlying() {
        return !this.onGround() && !this.isFallFlying() && !this.isInWater() && !this.hasEffect(MobEffects.LEVITATION);
    }

    @Inject(method = "aiStep", at = @At(value = "TAIL"))
    private void unequipElytra(CallbackInfo ci) {
        if (!Configuration.AUTO_EQUIP_ENABLED.get()) return;

        List<ItemStack> inventory = this.autoelytra$getCombinedInventory();

        // Check if just stopped flying
        if (AutoEquipController.hasPreviousChestItem() && !this.isFallFlying() && this.getItemBySlot(EquipmentSlot.CHEST).has(DataComponents.GLIDER)) {

            // Find previous chest item
            for (int slot = 0; slot < inventory.size(); slot++) {
                if (AutoEquipController.matchesPreviousChestItem(inventory.get(slot))) {
                    this.autoelytra$swapSlots(CHEST_SLOT, slot);
                    break;
                }
            }

            AutoEquipController.resetPreviousChestItem();
        }
    }

    @Unique private void autoelytra$swapSlots(int slotA, int slotB) {

        // Convert inventory slot to menu slot
        NonNullList<Slot> slots = this.inventoryMenu.slots;
        int slotAMenu = -1;
        int slotBMenu = -1;
        for (int i = 5; i < slots.size(); i++) {  // Start at 5 to skip crafting grid
            if (slots.get(i).getContainerSlot() == slotA) slotAMenu = i;
            if (slots.get(i).getContainerSlot() == slotB) slotBMenu = i;
            if (slotAMenu > -1 && slotBMenu > -1) break;
        }

        assert slotAMenu > -1;
        assert slotBMenu > -1;
        assert this.minecraft.gameMode != null;

        // Swap using ClickType.PICKUP as ClickType.SWAP only works in hotbar since 1.20.4: https://github.com/NimajnebEC/auto-elytra/issues/10
        this.minecraft.gameMode.handleInventoryMouseClick(this.inventoryMenu.containerId, slotAMenu, 0, ClickType.PICKUP, this);
        this.minecraft.gameMode.handleInventoryMouseClick(this.inventoryMenu.containerId, slotBMenu, 0, ClickType.PICKUP, this);
        this.minecraft.gameMode.handleInventoryMouseClick(this.inventoryMenu.containerId, slotAMenu, 0, ClickType.PICKUP, this);
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
