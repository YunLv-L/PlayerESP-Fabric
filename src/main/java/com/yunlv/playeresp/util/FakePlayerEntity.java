package com.yunlv.playeresp.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
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

        // 用 super.setPos 初始化内部字段
        super.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        syncOldFields();

        copyEquipment();
        mc.level.addFreshEntity(this);
    }

    // ========== 覆写所有读接口，永远返回 spawn 值 ==========

    @Override public Vec3 position() { return spawnPos; }
    @Override public Vec3 getPosition(float partialTicks) { return spawnPos; }
    @Override public double getX() { return spawnPos.x; }
    @Override public double getY() { return spawnPos.y; }
    @Override public double getZ() { return spawnPos.z; }
    @Override public float getYRot() { return spawnYRot; }
    @Override public float getXRot() { return spawnXRot; }
    @Override public float getYHeadRot() { return spawnYHeadRot; }

    @Override
    public AABB getBoundingBox() {
        double halfW = getBbWidth() / 2.0;
        return new AABB(
            spawnPos.x - halfW, spawnPos.y, spawnPos.z - halfW,
            spawnPos.x + halfW, spawnPos.y + getBbHeight(), spawnPos.z + halfW
        );
    }

    // ========== 覆写所有写接口，全部空实现 ==========

    @Override public void setPos(double x, double y, double z) { }
    @Override public void setPosRaw(double x, double y, double z) { }
    @Override public void setDeltaMovement(Vec3 v) { }
    @Override public void setDeltaMovement(double x, double y, double z) { }
    @Override public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) { }
    @Override public void lerpMotion(double x, double y, double z) { }
    @Override public void move(MoverType type, Vec3 movement) { }
    @Override public void travel(Vec3 input) { }

    // ========== tick 空实现 ==========

    @Override
    public void tick() {
        // 完全不做任何事
    }

    @Override
    public void aiStep() { }

    // ========== 内部字段同步一次 ==========

    private void syncOldFields() {
        this.xOld = spawnPos.x;
        this.yOld = spawnPos.y;
        this.zOld = spawnPos.z;
        this.yRotO = spawnYRot;
        this.xRotO = spawnXRot;
        this.yHeadRotO = spawnYHeadRot;
        this.yBodyRotO = spawnYBodyRot;
        this.yBodyRot = spawnYBodyRot;
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
    public void push(Entity entity) { }

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