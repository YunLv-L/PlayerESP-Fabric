package com.yunlv.playeresp.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yunlv.playeresp.feature.FeatureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void onRenderLabelIfPresent(T entity, Component text, PoseStack matrices,
                                         MultiBufferSource bufferSource, int light,
                                         CallbackInfo ci) {
        if (!FeatureManager.nameTags) return;

        ci.cancel();

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        double yOffset = entity.getBbHeight() + 0.5;
        matrices.pushPose();
        matrices.translate(0.0, yOffset, 0.0);
        matrices.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        matrices.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix = matrices.last().pose();

        String name = text.getString();
        float textWidth = font.width(name);
        float x = -textWidth / 2.0f;

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.textSeeThrough());

        font.drawInBatch(
            Component.literal(name),
            x, 0, 0xFFFFFFFF,
            false,
            matrix,
            consumer,
            Font.DisplayMode.SEE_THROUGH,
            0,
            light
        );

        matrices.popPose();
    }
}