package com.yunlv.playeresp.event;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RenderEventManager {
    private static final List<RenderListener> LISTENERS = new CopyOnWriteArrayList<>();

    public static void register(RenderListener listener) {
        LISTENERS.add(listener);
    }

    public static void fire(PoseStack matrices, float partialTicks) {
        for (RenderListener listener : LISTENERS) {
            listener.onRender(matrices, partialTicks);
        }
    }
}