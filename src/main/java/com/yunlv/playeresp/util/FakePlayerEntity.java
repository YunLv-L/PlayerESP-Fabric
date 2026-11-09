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

        this.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        applySpawnRotation();
        copyEquipment();
        mc.level.addFreshEntity(this);
    }

    // 完全冻结：不调用 super.tick()，防止 AI/移动逻辑被执行
    @Override
    public void tick() {
        this.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        this.setDeltaMovement(Vec3.ZERO);
        applySpawnRotation();
    }

    @Override
    public void aiStep() {
        // 不做任何移动处理
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

    private void applySpawnRotation() {
        this.setYRot(spawnYRot);
        this.setXRot(spawnXRot);
        this.setYHeadRot(spawnYHeadRot);
        this.yBodyRot = spawnYBodyRot;
        this.yRotO = spawnYRot;
        this.xRotO = spawnXRot;
        this.yHeadRotO = spawnYHeadRot;
        this.yBodyRotO = spawnYBodyRot;
    }

    public void despawn() {
        this.discard();
    }
}