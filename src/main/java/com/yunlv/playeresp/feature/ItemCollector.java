package com.yunlv.playeresp.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ItemCollector {

    private static final List<ItemEntity> items = new ArrayList<>();

    public static void update(Minecraft mc) {
        items.clear();
        if (!FeatureManager.itemEsp || mc.player == null || mc.level == null) return;

        for (Entity e : mc.level.entitiesForRendering()) {
            if (e instanceof ItemEntity item) {
                items.add(item);
            }
        }
    }

    public static List<ItemEntity> getItems() {
        return items;
    }

    public static void clear() {
        items.clear();
    }
}