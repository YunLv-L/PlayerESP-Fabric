package com.yunlv.playeresp.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class NoFallHandler {

    public static void tick(Minecraft mc) {
        if (!FeatureManager.noFall) return;
        if (mc.player == null) return;
        if (mc.player.isCreative()) return;
        if (mc.player.isFallFlying()) return;
        if (mc.player.fallDistance <= 1.0f) return;

        mc.player.connection.send(new ServerboundMovePlayerPacket.StatusOnly(
            true,
            mc.player.onGround()
        ));
        mc.player.fallDistance = 0;
    }
}