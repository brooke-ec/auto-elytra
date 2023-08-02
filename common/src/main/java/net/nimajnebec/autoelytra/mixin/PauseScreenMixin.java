package net.nimajnebec.autoelytra.mixin;

import net.minecraft.client.gui.screens.PauseScreen;
import net.nimajnebec.autoelytra.AutoElytra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PauseScreen.class)
public class PauseScreenMixin {

    @Inject(method = "onDisconnect", at = @At( value = "HEAD" ))
    private void resetPreviousChestItem(CallbackInfo ci){
        AutoElytra.resetPreviousChestItem();
    }

}
