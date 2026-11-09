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

public class FakePlayerEntity extends RemotePlayer {

    private final Player realPlayer;
    private PlayerInfo playerInfo;

    private final Vec3 spawnPos;
    private final float spawnYRot;
    private final float spawnXRot;
    private final float spawnYHeadRot;
    private final float spawnYBodyRot;

    public FakePlayerEntity(Minecraft mc) {
        super(mc.level, mc.player.getGameProfile());
        this.realPlayer = mc.player;
        this.setUUID(UUID.randomUUID());

        this.spawnPos = mc.player.position();
        this.spawnYRot = mc.player.getYRot();
        this.spawnXRot = mc.player.getXRot();
        this.spawnYHeadRot = mc.player.getYHeadRot();
        this.spawnYBodyRot = mc.player.yBodyRot;

        // 位置 + 旧位置（关键：旧位置必须和当前位置一致）
        this.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        this.xOld = spawnPos.x;
        this.yOld = spawnPos.y;
        this.zOld = spawnPos.z;

        // 当前朝向
        this.setYRot(spawnYRot);
        this.setXRot(spawnXRot);
        this.setYHeadRot(spawnYHeadRot);
        this.yBodyRot = spawnYBodyRot;

        // 旧朝向
        this.yRotO = spawnYRot;
        this.xRotO = spawnXRot;
        this.yHeadRotO = spawnYHeadRot;
        this.yBodyRotO = spawnYBodyRot;

        copyEquipment();
        mc.level.addFreshEntity(this);
    }

    @Override
    public void tick() {
        // 空实现：不执行任何原版 tick 逻辑
    }

    @Override
    public void aiStep() {
        // 空实现：不做移动/AI 处理
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
    }

    private void copyEquipment() {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR
                || slot.getType() == EquipmentSlot.Type.HAND) {
                this.setItemSlot(slot, realPlayer.getItemBySlot(slot).copy());
            }
        }
    }

    public void despawn() {
        this.discard();
    }
}