package com.yunlv.playeresp.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yunlv.playeresp.feature.FeatureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
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

    @Inject(method = "renderNameTag", at = @At("HEAD"), cancellable = true)
    private void onRenderNameTag(T entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTicks, CallbackInfo ci) {
        if (!FeatureManager.nameTags) return;

        // 取消原版受深度测试影响的渲染
        ci.cancel();

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        double yOffset = entity.getBbHeight() + 0.5;
        poseStack.pushPose();
        poseStack.translate(0.0, yOffset, 0.0);
        
        // 相机朝向旋转
        var camera = mc.gameRenderer.getMainCamera();
        if (camera.isInitialized()) {
            poseStack.mulPose(camera.rotation());
        }
        poseStack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix = poseStack.last().pose();

        String name = component.getString();
        float textWidth = font.width(name);
        float x = -textWidth / 2.0f;

        // 使用 SEE_THROUGH 模式渲染，穿透方块遮挡
        font.drawInBatch(
            Component.literal(name),
            x, 0, 0xFFFFFFFF,
            false,
            matrix,
            multiBufferSource,
            Font.DisplayMode.SEE_THROUGH,
            0,
            packedLight
        );

        poseStack.popPose();
    }
}