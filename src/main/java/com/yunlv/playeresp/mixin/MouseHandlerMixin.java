package com.yunlv.playeresp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.yunlv.playeresp.feature.FreecamHandler;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @WrapOperation(
        method = "turnPlayer",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V")
    )
    private void wrapTurn(LocalPlayer player, double deltaYaw, double deltaPitch, Operation<Void> original) {
        if (FreecamHandler.isActive()) {
            FreecamHandler.turn(deltaYaw, deltaPitch);
            return;
        }
        original.call(player, deltaYaw, deltaPitch);
    }
}