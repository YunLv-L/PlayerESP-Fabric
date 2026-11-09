package com.yunlv.playeresp.feature;

import com.yunlv.playeresp.util.FakePlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class FreecamHandler {

    private static boolean active = false;
    private static Vec3 camPos = Vec3.ZERO;
    private static Vec3 prevCamPos = Vec3.ZERO;
    private static float camYaw = 0;
    private static float camPitch = 0;
    private static FakePlayerEntity fakePlayer = null;

    private static final double horizontalSpeed = 1.0;
    private static final double verticalRatio = 0.5;

    public static boolean isActive() { return active; }

    public static void toggle(Minecraft mc) {
        if (mc.player == null || mc.level == null) return;

        if (!active) {
            active = true;
            camPos = mc.player.getEyePosition();
            prevCamPos = camPos;
            camYaw = mc.player.getYRot();
            camPitch = mc.player.getXRot();
            fakePlayer = new FakePlayerEntity(mc);
        } else {
            active = false;
            if (fakePlayer != null) {
                fakePlayer.despawn();
                fakePlayer = null;
            }
        }
    }

    public static void tick(Minecraft mc) {
        if (!active || mc.player == null) return;
        if (mc.screen != null) {
            prevCamPos = camPos;
            return;
        }

        prevCamPos = camPos;

        boolean forward = mc.options.keyUp.isDown();
        boolean back    = mc.options.keyDown.isDown();
        boolean left    = mc.options.keyLeft.isDown();
        boolean right   = mc.options.keyRight.isDown();
        boolean jump    = mc.options.keyJump.isDown();
        boolean sneak   = mc.options.keyShift.isDown();

        // xxa 翻转符号：A 按下时 xxa = -1
        double xxa = (right ? 1.0 : 0.0) - (left ? 1.0 : 0.0);
        double zza = (forward ? 1.0 : 0.0) - (back ? 1.0 : 0.0);

        double yawRad = Math.toRadians(camYaw);
        double sinYaw = Math.sin(yawRad);
        double cosYaw = Math.cos(yawRad);

        double offsetX = xxa * cosYaw - zza * sinYaw;
        double offsetZ = xxa * sinYaw + zza * cosYaw;

        double offsetY = 0;
        double vSpeed = horizontalSpeed * verticalRatio;
        if (jump)  offsetY += vSpeed;
        if (sneak) offsetY -= vSpeed;

        camPos = camPos.add(offsetX * horizontalSpeed, offsetY, offsetZ * horizontalSpeed);
    }

    public static Vec3 getCamPos(float partialTicks) {
        return new Vec3(
            Mth.lerp(partialTicks, prevCamPos.x, camPos.x),
            Mth.lerp(partialTicks, prevCamPos.y, camPos.y),
            Mth.lerp(partialTicks, prevCamPos.z, camPos.z)
        );
    }

    public static float getCamYaw() { return camYaw; }
    public static float getCamPitch() { return camPitch; }

    public static void turn(double deltaYaw, double deltaPitch) {
        camYaw += (float) (deltaYaw * 0.15);
        camPitch += (float) (deltaPitch * 0.15);
        camPitch = Mth.clamp(camPitch, -90, 90);
    }

    public static void cleanup() {
        if (fakePlayer != null) {
            fakePlayer.despawn();
            fakePlayer = null;
        }
        active = false;
    }
}