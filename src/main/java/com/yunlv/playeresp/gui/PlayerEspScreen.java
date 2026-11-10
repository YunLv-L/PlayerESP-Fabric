package com.yunlv.playeresp.gui;

import com.yunlv.playeresp.ConfigManager;
import com.yunlv.playeresp.PlayerEspMod;
import com.yunlv.playeresp.feature.KillPotionHandler;
import com.yunlv.playeresp.feature.TrollPotionHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PlayerEspScreen extends Screen {

    private int currentCategory = 0; // 0: ESP, 1: Utility, 2: Potions

    public PlayerEspScreen() {
        super(Component.translatable("gui.playeresp.title"));
    }

    @Override
    protected void init() {
        super.init();
        
        // 顶部：分类切换按钮 (Tab)
        int tabY = 40;
        int tabWidth = 100;
        int tabSpacing = 10;
        int startX = this.width / 2 - (tabWidth * 3 + tabSpacing * 2) / 2;

        addRenderableWidget(Button.builder(Component.translatable("category.playeresp.esp"), b -> switchCategory(0))
            .bounds(startX, tabY, tabWidth, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("category.playeresp.utility"), b -> switchCategory(1))
            .bounds(startX + tabWidth + tabSpacing, tabY, tabWidth, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("category.playeresp.potion"), b -> switchCategory(2))
            .bounds(startX + (tabWidth + tabSpacing) * 2, tabY, tabWidth, 20).build());

        // 中部：根据分类加载小类开关
        int startY = this.height / 2 - 50;
        int step = 25;
        int x = this.width / 2 - 100;

        if (currentCategory == 0) {
            // --- ESP 分类 ---
            addRenderableWidget(Button.builder(getText("feature.playeresp", PlayerEspMod.config.enabled), b -> {
                PlayerEspMod.config.enabled = !PlayerEspMod.config.enabled;
                b.setMessage(getText("feature.playeresp", PlayerEspMod.config.enabled));
                save();
            }).bounds(x, startY, 200, 20).build());

            addRenderableWidget(Button.builder(getText("feature.chestesp", PlayerEspMod.config.chestEsp), b -> {
                PlayerEspMod.config.chestEsp = !PlayerEspMod.config.chestEsp;
                b.setMessage(getText("feature.chestesp", PlayerEspMod.config.chestEsp));
                save();
            }).bounds(x, startY + step, 200, 20).build());

            addRenderableWidget(Button.builder(getText("feature.itemesp", PlayerEspMod.config.itemEsp), b -> {
                PlayerEspMod.config.itemEsp = !PlayerEspMod.config.itemEsp;
                b.setMessage(getText("feature.itemesp", PlayerEspMod.config.itemEsp));
                save();
            }).bounds(x, startY + step * 2, 200, 20).build());

            addRenderableWidget(Button.builder(getText("feature.mobesp", PlayerEspMod.config.mobEsp), b -> {
                PlayerEspMod.config.mobEsp = !PlayerEspMod.config.mobEsp;
                b.setMessage(getText("feature.mobesp", PlayerEspMod.config.mobEsp));
                save();
            }).bounds(x, startY + step * 3, 200, 20).build());

        } else if (currentCategory == 1) {
            // --- 实用工具分类 ---
            addRenderableWidget(Button.builder(getText("feature.nofall", PlayerEspMod.config.noFall), b -> {
                PlayerEspMod.config.noFall = !PlayerEspMod.config.noFall;
                b.setMessage(getText("feature.nofall", PlayerEspMod.config.noFall));
                save();
            }).bounds(x, startY, 200, 20).build());

            addRenderableWidget(Button.builder(getText("feature.fullbright", PlayerEspMod.config.fullbright), b -> {
                PlayerEspMod.config.fullbright = !PlayerEspMod.config.fullbright;
                b.setMessage(getText("feature.fullbright", PlayerEspMod.config.fullbright));
                save();
            }).bounds(x, startY + step, 200, 20).build());

        } else if (currentCategory == 2) {
            // --- 药水生成分类 ---
            addRenderableWidget(Button.builder(getPotionTypeText("gui.playeresp.killtype", PlayerEspMod.config.killPotionType), b -> {
                PlayerEspMod.config.killPotionType = (PlayerEspMod.config.killPotionType + 1) % 2;
                b.setMessage(getPotionTypeText("gui.playeresp.killtype", PlayerEspMod.config.killPotionType));
                save();
            }).bounds(x, startY, 200, 20).build());

            addRenderableWidget(Button.builder(Component.translatable("gui.playeresp.createkill"), b -> {
                KillPotionHandler.create(this.minecraft, PlayerEspMod.config.killPotionType == 1);
            }).bounds(x, startY + step, 200, 20).build());

            addRenderableWidget(Button.builder(getPotionTypeText("gui.playeresp.trolltype", PlayerEspMod.config.trollPotionType), b -> {
                PlayerEspMod.config.trollPotionType = (PlayerEspMod.config.trollPotionType + 1) % 2;
                b.setMessage(getPotionTypeText("gui.playeresp.trolltype", PlayerEspMod.config.trollPotionType));
                save();
            }).bounds(x, startY + step * 3, 200, 20).build());

            addRenderableWidget(Button.builder(Component.translatable("gui.playeresp.createtroll"), b -> {
                TrollPotionHandler.create(this.minecraft, PlayerEspMod.config.trollPotionType == 1);
            }).bounds(x, startY + step * 4, 200, 20).build());
        }

        // 底部：完成按钮
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> this.onClose())
            .bounds(this.width / 2 - 100, this.height - 40, 200, 20).build());
    }

    private void switchCategory(int newCategory) {
        this.currentCategory = newCategory;
        this.rebuildWidgets();
    }

    private void save() {
        ConfigManager.save(PlayerEspMod.config);
    }

    private Component getText(String key, boolean state) {
        return Component.translatable(key).append(": ").append(Component.translatable(state ? "gui.playeresp.on" : "gui.playeresp.off"));
    }

    private Component getPotionTypeText(String key, int type) {
        return Component.translatable(key).append(": ").append(Component.translatable(type == 1 ? "gui.playeresp.lingering" : "gui.playeresp.splash"));
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}