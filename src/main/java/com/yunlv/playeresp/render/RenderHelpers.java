package com.yunlv.playeresp.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import java.util.List;

public class RenderHelpers {

    public static MultiBufferSource.BufferSource getVCP() {
        return Minecraft.getInstance().renderBuffers().bufferSource();
    }

    public static Vec3 getCameraPos() {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        var pos = camera.position(); return new Vec3(pos.x, pos.y, pos.z);
    }

    public static int toIntColor(float[] rgb, float opacity) {
        return (int)(Math.clamp(opacity, 0, 1) * 255) << 24
            | (int)(Math.clamp(rgb[0], 0, 1) * 255) << 16
            | (int)(Math.clamp(rgb[1], 0, 1) * 255) << 8
            | (int)(Math.clamp(rgb[2], 0, 1) * 255);
    }

    public static void drawOutlinedBoxes(PoseStack matrices, List<ColoredBox> boxes, boolean depthTest) {
        MultiBufferSource.BufferSource vcp = getVCP();
        VertexConsumer buffer = vcp.getBuffer(PlayerEspRenderTypes.ESP_LINES);
        Vec3 camOffset = getCameraPos().reverse();
        for (ColoredBox box : boxes) {
            drawOutlinedBox(matrices, buffer, box.box().move(camOffset), box.color());
        }
        vcp.endBatch(PlayerEspRenderTypes.ESP_LINES);
    }

    public static void drawOutlinedBox(PoseStack matrices, VertexConsumer buffer, AABB box, int color) {
        PoseStack.Pose entry = matrices.last();
        float x1 = (float) box.minX, y1 = (float) box.minY, z1 = (float) box.minZ;
        float x2 = (float) box.maxX, y2 = (float) box.maxY, z2 = (float) box.maxZ;

        line(entry, buffer, x1, y1, z1, x2, y1, z1, color, 1, 0, 0);
        line(entry, buffer, x1, y1, z1, x1, y1, z2, color, 0, 0, 1);
        line(entry, buffer, x2, y1, z1, x2, y1, z2, color, 0, 0, 1);
        line(entry, buffer, x1, y1, z2, x2, y1, z2, color, 1, 0, 0);
        line(entry, buffer, x1, y2, z1, x2, y2, z1, color, 1, 0, 0);
        line(entry, buffer, x1, y2, z1, x1, y2, z2, color, 0, 0, 1);
        line(entry, buffer, x2, y2, z1, x2, y2, z2, color, 0, 0, 1);
        line(entry, buffer, x1, y2, z2, x2, y2, z2, color, 1, 0, 0);
        line(entry, buffer, x1, y1, z1, x1, y2, z1, color, 0, 1, 0);
        line(entry, buffer, x2, y1, z1, x2, y2, z1, color, 0, 1, 0);
        line(entry, buffer, x1, y1, z2, x1, y2, z2, color, 0, 1, 0);
        line(entry, buffer, x2, y1, z2, x2, y2, z2, color, 0, 1, 0);
    }

    public static void drawTracers(PoseStack matrices, float partialTicks, List<ColoredPoint> ends, boolean depthTest) {
        MultiBufferSource.BufferSource vcp = getVCP();
        VertexConsumer buffer = vcp.getBuffer(PlayerEspRenderTypes.ESP_LINES);
        Vec3 start = getTracerOrigin();
        Vec3 camOffset = getCameraPos().reverse();
        for (ColoredPoint end : ends) {
            drawLine(matrices, buffer, start, end.point().add(camOffset), end.color());
        }
        vcp.endBatch(PlayerEspRenderTypes.ESP_LINES);
    }

    private static Vec3 getTracerOrigin() {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vector3f look = new Vector3f(0, 0, -1);
        camera.rotation().transform(look);
        return new Vec3(look.x, look.y, look.z).scale(10);
    }

    public static void drawLine(PoseStack matrices, VertexConsumer buffer, Vec3 start, Vec3 end, int color) {
        PoseStack.Pose entry = matrices.last();
        float x1 = (float) start.x, y1 = (float) start.y, z1 = (float) start.z;
        float x2 = (float) end.x, y2 = (float) end.y, z2 = (float) end.z;
        Vector3f normal = new Vector3f(x2, y2, z2).sub(x1, y1, z1).normalize();

        buffer.addVertex(entry, x1, y1, z1).setColor(color).setNormal(entry, normal).setLineWidth(2.0f);

        float t = new Vector3f(x1, y1, z1).negate().dot(normal);
        float length = new Vector3f(x2, y2, z2).sub(x1, y1, z1).length();
        if (t > 0 && t < length) {
            Vector3f closeToCam = new Vector3f(normal).mul(t).add(x1, y1, z1);
            buffer.addVertex(entry, closeToCam).setColor(color).setNormal(entry, normal).setLineWidth(2.0f);
            buffer.addVertex(entry, closeToCam).setColor(color).setNormal(entry, normal).setLineWidth(2.0f);
        }

        buffer.addVertex(entry, x2, y2, z2).setColor(color).setNormal(entry, normal).setLineWidth(2.0f);
    }

    private static void line(PoseStack.Pose entry, VertexConsumer buffer,
                             float x1, float y1, float z1, float x2, float y2, float z2,
                             int color, float nx, float ny, float nz) {
        Vector3f normal = new Vector3f(nx, ny, nz);
        buffer.addVertex(entry, x1, y1, z1).setColor(color).setNormal(entry, normal).setLineWidth(2.0f);
        buffer.addVertex(entry, x2, y2, z2).setColor(color).setNormal(entry, normal).setLineWidth(2.0f);
    }

    public record ColoredBox(AABB box, int color) {}
    public record ColoredPoint(Vec3 point, int color) {}
}