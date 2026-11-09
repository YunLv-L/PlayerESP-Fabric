package com.yunlv.playeresp;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yunlv.playeresp.event.RenderListener;
import com.yunlv.playeresp.render.RenderHelpers;
import com.yunlv.playeresp.render.RenderHelpers.ColoredBox;
import com.yunlv.playeresp.render.RenderHelpers.ColoredPoint;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;

public class PlayerEspRenderer implements RenderListener {

    @Override
    public void onRender(PoseStack matrices, float partialTicks) {
        if (!PlayerEspMod.config.enabled) return;

        List<Player> players = PlayerCollector.getPlayers();
        if (players.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (PlayerEspMod.config.hasBoxes()) {
            double extraSize = PlayerEspMod.config.getExtraSize() / 2.0;
            List<ColoredBox> boxes = new ArrayList<>(players.size());
            for (Player player : players) {
                AABB box = getLerpedBox(player, partialTicks).move(0, extraSize, 0).inflate(extraSize);
                boxes.add(new ColoredBox(box, getColor(player)));
            }
            RenderHelpers.drawOutlinedBoxes(matrices, boxes, false);
        }

        if (PlayerEspMod.config.hasLines()) {
            List<ColoredPoint> ends = new ArrayList<>(players.size());
            for (Player player : players) {
                Vec3 center = getLerpedBox(player, partialTicks).getCenter();
                ends.add(new ColoredPoint(center, getColor(player)));
            }
            RenderHelpers.drawTracers(matrices, partialTicks, ends, false);
        }
    }

    private static AABB getLerpedBox(Player e, float partialTicks) {
        if (e.isRemoved()) return e.getBoundingBox();
        Vec3 lerpedPos = getLerpedPos(e, partialTicks);
        Vec3 offset = lerpedPos.subtract(e.position());
        return e.getBoundingBox().move(offset);
    }

    private static Vec3 getLerpedPos(Player e, float partialTicks) {
        if (e.isRemoved()) return e.position();
        double x = Mth.lerp(partialTicks, e.xOld, e.getX());
        double y = Mth.lerp(partialTicks, e.yOld, e.getY());
        double z = Mth.lerp(partialTicks, e.zOld, e.getZ());
        return new Vec3(x, y, z);
    }

    private static int getColor(Player e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return 0xFFFFFFFF;
        float f = mc.player.distanceTo(e) / 20f;
        float r = Math.clamp(2 - f, 0, 1);
        float g = Math.clamp(f, 0, 1);
        return RenderHelpers.toIntColor(new float[]{r, g, 0}, 0.5f);
    }
}