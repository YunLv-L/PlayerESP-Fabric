package com.yunlv.playeresp.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class MobCollector {

    private static final List<LivingEntity> mobs = new ArrayList<>();

    public static void update(Minecraft mc) {
        mobs.clear();
        if (!FeatureManager.mobEsp || mc.player == null || mc.level == null) return;

        for (Entity e : mc.level.entitiesForRendering()) {
            if (e instanceof LivingEntity le && !(le instanceof Player)) {
                mobs.add(le);
            }
        }
    }

    public static List<LivingEntity> getMobs() {
        return mobs;
    }

    public static void clear() {
        mobs.clear();
    }
}