package com.yunlv.playeresp.gui;

import com.yunlv.playeresp.PlayerEspMod;
import com.yunlv.playeresp.feature.FeatureManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PlayerEspScreen extends Screen {

    public PlayerEspScreen() {
        super(Component.translatable("gui.playeresp.title"));
    }

    @Override
    protected void init() {
        super.init();
        int startY = this.height / 2 - 60;
        int step = 25;
        int x = this.width / 2 - 100;

        addRenderableWidget(Button.builder(getText("feature.playeresp", PlayerEspMod.config.enabled), b -> {
            PlayerEspMod.config.enabled = !PlayerEspMod.config.enabled;
            b.setMessage(getText("feature.playeresp", PlayerEspMod.config.enabled));
        }).bounds(x, startY, 200, 20).build());

        addRenderableWidget(Button.builder(getText("feature.chestesp", FeatureManager.chestEsp), b -> {
            FeatureManager.chestEsp = !FeatureManager.chestEsp;
            b.setMessage(getText("feature.chestesp", FeatureManager.chestEsp));
        }).bounds(x, startY + step, 200, 20).build());

        addRenderableWidget(Button.builder(getText("feature.itemesp", FeatureManager.itemEsp), b -> {
            FeatureManager.itemEsp = !FeatureManager.itemEsp;
            b.setMessage(getText("feature.itemesp", FeatureManager.itemEsp));
        }).bounds(x, startY + step * 2, 200, 20).build());

        addRenderableWidget(Button.builder(getText("feature.mobesp", FeatureManager.mobEsp), b -> {
            FeatureManager.mobEsp = !FeatureManager.mobEsp;
            b.setMessage(getText("feature.mobesp", FeatureManager.mobEsp));
        }).bounds(x, startY + step * 3, 200, 20).build());

        addRenderableWidget(Button.builder(getText("feature.nofall", FeatureManager.noFall), b -> {
            FeatureManager.noFall = !FeatureManager.noFall;
            b.setMessage(getText("feature.nofall", FeatureManager.noFall));
        }).bounds(x, startY + step * 4, 200, 20).build());

        addRenderableWidget(Button.builder(getText("feature.fullbright", FeatureManager.fullbright), b -> {
            FeatureManager.fullbright = !FeatureManager.fullbright;
            b.setMessage(getText("feature.fullbright", FeatureManager.fullbright));
        }).bounds(x, startY + step * 5, 200, 20).build());

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> this.onClose())
            .bounds(x, startY + step * 6 + 10, 200, 20).build());
    }

    private Component getText(String key, boolean state) {
        String status = state ? "gui.playeresp.on" : "gui.playeresp.off";
        return Component.translatable(key).append(": ").append(Component.translatable(status));
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 留空以阻断 1.21.11 的多次模糊冲突
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 自己画一个半透明黑色遮罩
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000);
        
        // 绘制标题
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        
        // 渲染按钮
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}