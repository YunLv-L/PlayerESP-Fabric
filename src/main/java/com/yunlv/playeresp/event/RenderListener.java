package com.yunlv.playeresp.event;

import com.mojang.blaze3d.vertex.PoseStack;

public interface RenderListener {
    void onRender(PoseStack matrices, float partialTicks);
}