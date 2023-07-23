package net.nimajnebec.autoelytra.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import net.nimajnebec.autoelytra.AutoElytra;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(targets = "net.minecraft.client.network.ClientPlayerEntity")
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    @Unique private static final int chestSlot = EquipmentSlot.CHEST.getOffsetEntitySlotId(PlayerInventory.MAIN_SIZE);
    @Unique @Nullable private ItemStack previousChestItem;
    @Shadow @Final protected MinecraftClient client;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Redirect(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack checkWearingElytra(ClientPlayerEntity instance, EquipmentSlot equipmentSlot) {
        if (this.canStartFlying()) tryEquipElytra();
        return this.getEquippedStack(equipmentSlot);
    }

    @Unique private boolean canStartFlying() {
        return !this.isOnGround() && !this.isFallFlying() && !this.isTouchingWater() && !this.hasStatusEffect(StatusEffects.LEVITATION);
    }

    @Unique private void tryEquipElytra() {
        List<ItemStack> inventory = this.getCombinedInventory();

        // Return if elytra is alredy equiped
        if (inventory.get(this.chestSlot).isOf(Items.ELYTRA)) return;

        // Find elytra in inventory
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.get(slot);

            if (stack.isOf(Items.ELYTRA)) {
                this.previousChestItem = inventory.get(chestSlot);
                this.swapSlots(slot, chestSlot);
                return;
            }
        }
    }

    @Unique private void swapSlots(int slotA, int slotB) {

        // Convert inventory slot to screen slot
        int slotAScreen = -1;
        for (int i = 5; i < this.playerScreenHandler.slots.size(); i++) {
            if (this.playerScreenHandler.slots.get(i).getIndex() == slotA) {
                slotAScreen = i;
                break;
            }
        }

        // Swap slots
        assert client.interactionManager != null;
        client.interactionManager.clickSlot(this.playerScreenHandler.syncId, slotAScreen, slotB, SlotActionType.SWAP, this);
    }

    @Unique private List<ItemStack> getCombinedInventory() {
        PlayerInventory inventory = this.getInventory();
        List<ItemStack> result = new ArrayList<>();

        for (DefaultedList<ItemStack> list : inventory.combinedInventory) {
            result.addAll(list);
        }

        return result;
    }

}
