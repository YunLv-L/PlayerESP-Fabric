package com.yunlv.playeresp.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class PlayerEspPipelines {
    public static final RenderPipeline ESP_LINES = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
            .withLocation(Identifier.parse("playeresp:pipeline/esp_lines"))
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build()
    );
}