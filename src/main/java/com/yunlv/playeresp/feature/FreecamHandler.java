package com.yunlv.playeresp.feature;

import com.yunlv.playeresp.util.FakePlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class FreecamHandler {

    private static boolean active = false;
    private static Vec3 camPos = Vec3.ZERO;
    private static Vec3 prevCamPos = Vec3.ZERO;
    private static float camYaw = 0;
    private static float camPitch = 0;
    private static FakePlayerEntity fakePlayer = null;

    private static final double horizontalSpeed = 1.0;
    private static final double verticalRatio = 0.5;

    public static boolean isActive() {
        return active;
    }

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

        double xxa = mc.player.input.xxa;
        double zza = mc.player.input.zza;

        double yawRad = Math.toRadians(camYaw);
        double sinYaw = Math.sin(yawRad);
        double cosYaw = Math.cos(yawRad);

        double offsetX = zza * -sinYaw - xxa * cosYaw;
        double offsetZ = zza * cosYaw - xxa * sinYaw;

        double offsetY = 0;
        double vSpeed = horizontalSpeed * verticalRatio;
        long window = mc.getWindow().getWindow();
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            offsetY += vSpeed;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            offsetY -= vSpeed;
        }

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