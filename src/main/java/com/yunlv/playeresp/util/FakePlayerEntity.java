package com.yunlv.playeresp.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FakePlayerEntity extends RemotePlayer {

    private final Player realPlayer;
    private PlayerInfo playerInfo;

    public FakePlayerEntity(Minecraft mc) {
        super(mc.level, mc.player.getGameProfile());
        this.realPlayer = mc.player;
        this.setUUID(UUID.randomUUID());
        this.copyPosition(mc.player);
        this.getInventory().copyFrom(mc.player.getInventory());
        copyRotation();
        mc.level.addFreshEntity(this);
    }

    @Override
    protected @Nullable PlayerInfo getPlayerInfo() {
        if (playerInfo == null) {
            playerInfo = Minecraft.getInstance().getConnection()
                .getPlayerInfo(realPlayer.getUUID());
        }
        return playerInfo;
    }

    @Override
    protected void push(Entity entity) {
        // 不推开真实玩家
    }

    private void copyRotation() {
        this.setYRot(realPlayer.getYRot());
        this.setXRot(realPlayer.getXRot());
        this.setYHeadRot(realPlayer.getYHeadRot());
        this.yBodyRot = realPlayer.yBodyRot;
        this.yRotO = realPlayer.getYRot();
        this.xRotO = realPlayer.getXRot();
        this.yHeadRotO = realPlayer.getYHeadRot();
        this.yBodyRotO = realPlayer.yBodyRot;
    }

    public void despawn() {
        this.discard();
    }
}