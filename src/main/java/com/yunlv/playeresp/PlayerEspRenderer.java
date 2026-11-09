package com.yunlv.playeresp;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yunlv.playeresp.event.RenderListener;
import com.yunlv.playeresp.feature.ChestCollector;
import com.yunlv.playeresp.feature.FeatureManager;
import com.yunlv.playeresp.feature.ItemCollector;
import com.yunlv.playeresp.feature.MobCollector;
import com.yunlv.playeresp.render.RenderHelpers;
import com.yunlv.playeresp.render.RenderHelpers.ColoredBox;
import com.yunlv.playeresp.render.RenderHelpers.ColoredPoint;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PlayerEspRenderer implements RenderListener {

    @Override
    public void onRender(PoseStack matrices, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        List<ColoredBox> boxes = new ArrayList<>();

        if (PlayerEspMod.config.enabled) {
            double extraSize = PlayerEspMod.config.getExtraSize() / 2.0;
            for (Player player : PlayerCollector.getPlayers()) {
                AABB box = getLerpedBox(player, partialTicks)
                    .move(0, extraSize, 0).inflate(extraSize);
                boxes.add(new ColoredBox(box, getColor(player)));
            }
        }

        if (FeatureManager.chestEsp) {
            for (BlockPos pos : ChestCollector.getContainers()) {
                AABB box = new AABB(pos).inflate(0.05);
                boxes.add(new ColoredBox(box, 0xFFFFAA00));
            }
        }

        if (FeatureManager.itemEsp) {
            for (ItemEntity item : ItemCollector.getItems()) {
                AABB box = item.getBoundingBox().inflate(0.1);
                boxes.add(new ColoredBox(box, 0xFFFFFFFF));
            }
        }

        if (FeatureManager.mobEsp) {
            for (LivingEntity mob : MobCollector.getMobs()) {
                AABB box = mob.getBoundingBox().inflate(0.1);
                boxes.add(new ColoredBox(box, 0xFFFF4444));
            }
        }

        if (!boxes.isEmpty()) {
            RenderHelpers.drawOutlinedBoxes(matrices, boxes, false);
        }

        if (PlayerEspMod.config.enabled && PlayerEspMod.config.hasLines()) {
            List<ColoredPoint> ends = new ArrayList<>();
            for (Player player : PlayerCollector.getPlayers()) {
                Vec3 center = getLerpedBox(player, partialTicks).getCenter();
                ends.add(new ColoredPoint(center, getColor(player)));
            }
            if (!ends.isEmpty()) {
                RenderHelpers.drawTracers(matrices, partialTicks, ends, false);
            }
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