package net.nimajnebec.autoelytra.mixin;

import net.nimajnebec.autoelytra.AutoElytra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.screen.GameMenuScreen")
public abstract class GameMenuScreenMixin {

    @Inject(method = "disconnect()V", at = @At( value = "HEAD"))
    private void clearPreviousChestItem(CallbackInfo ci) {
        AutoElytra.getInstance().setPreviousChestItemNbt(null);
    }

}
