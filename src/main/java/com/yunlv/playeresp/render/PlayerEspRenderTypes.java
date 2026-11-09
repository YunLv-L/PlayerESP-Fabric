package com.yunlv.playeresp.render;

import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public class PlayerEspRenderTypes {
    public static final RenderType ESP_LINES = RenderType.create(
        "playeresp:esp_lines",
        RenderSetup.builder(PlayerEspPipelines.ESP_LINES)
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .setOutputTarget(OutputTarget.MAIN_TARGET)
            .createRenderSetup()
    );
}