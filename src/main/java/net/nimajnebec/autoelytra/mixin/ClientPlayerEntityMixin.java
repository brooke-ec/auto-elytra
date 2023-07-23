package net.nimajnebec.autoelytra.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(targets = "net.minecraft.client.network.ClientPlayerEntity")
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    @Unique private static final int chestSlot = EquipmentSlot.CHEST.getOffsetEntitySlotId(PlayerInventory.MAIN_SIZE);
    @Unique private NbtCompound previousChestItemNbt;
    @Shadow @Final protected MinecraftClient client;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tickMovement()V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private void tryEquipElytra(CallbackInfo ci) {
        if (this.canStartFlying()) {
            List<ItemStack> inventory = this.getCombinedInventory();

            // Return if elytra is already equipped
            if (inventory.get(chestSlot).isOf(Items.ELYTRA)) return;

            // Find elytra in inventory
            for (int slot = 0; slot < inventory.size(); slot++) {
                ItemStack stack = inventory.get(slot);

                if (stack.isOf(Items.ELYTRA)) {
                    this.previousChestItemNbt = inventory.get(chestSlot).getOrCreateNbt();
                    this.swapSlots(slot, chestSlot);
                    return;
                }
            }
        }
    }

    @Unique private boolean canStartFlying() {
        return !this.isOnGround() && !this.isFallFlying() && !this.isTouchingWater() && !this.hasStatusEffect(StatusEffects.LEVITATION);
    }

    @Inject(method = "tickMovement()V",at = @At(value = "TAIL"))
    public void unequipElytra(CallbackInfo ci) {
        if (previousChestItemNbt != null && !this.isFallFlying() && this.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
            List<ItemStack> inventory = this.getCombinedInventory();

            // Find previous item
            for (int slot = 0; slot < inventory.size(); slot++) {
                if (inventory.get(slot).getOrCreateNbt().equals(previousChestItemNbt)) {
                    this.swapSlots(slot, chestSlot);
                    break;
                }
            }

            // Null previous item
            this.previousChestItemNbt = null;
        }
    }

    @Unique public void swapSlots(int slotA, int slotB) {

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

    @Unique public List<ItemStack> getCombinedInventory() {
        PlayerInventory inventory = this.getInventory();
        List<ItemStack> result = new ArrayList<>();

        for (DefaultedList<ItemStack> list : inventory.combinedInventory) {
            result.addAll(list);
        }

        return result;
    }

}
