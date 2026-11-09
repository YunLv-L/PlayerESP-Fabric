package com.yunlv.playeresp.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * 完全照抄 Wurst 的 FakePlayerEntity 结构：
 * 不覆写 tick / aiStep / move / travel 任何东西，只覆写 push 和 getPlayerInfo。
 * RemotePlayer 的输入永远是 0，因此假人天然不动。
 */
public class FakePlayerEntity extends RemotePlayer {

    private final Player realPlayer;
    private PlayerInfo playerInfo;

    public FakePlayerEntity(Minecraft mc) {
        super(mc.level, mc.player.getGameProfile());
        this.realPlayer = mc.player;
        this.setUUID(UUID.randomUUID());
        this.copyPosition(mc.player);
        this.setDeltaMovement(Vec3.ZERO);
        copyEquipment();
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
    public void push(Entity entity) {
        // 不推开真实玩家
    }

    private void copyEquipment() {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR
                || slot.getType() == EquipmentSlot.Type.HAND) {
                this.setItemSlot(slot, realPlayer.getItemBySlot(slot).copy());
            }
        }
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