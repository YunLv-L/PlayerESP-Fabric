package com.yunlv.playeresp.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.yunlv.playeresp.ConfigManager;
import com.yunlv.playeresp.PlayerEspMod;
import com.yunlv.playeresp.feature.KillPotionHandler;
import com.yunlv.playeresp.feature.TrollPotionHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class PlayerEspScreen extends Screen {

    private int currentCategory = 0; 
    private String listeningForKey = null;

    public PlayerEspScreen() {
        super(Component.translatable("gui.playeresp.title"));
    }

    @Override
    protected void init() {
        super.init();
        
        int tabY = 30;
        int tabWidth = 75;
        int tabSpacing = 5;
        int startX = this.width / 2 - (tabWidth * 4 + tabSpacing * 3) / 2;

        addRenderableWidget(Button.builder(Component.translatable("category.playeresp.esp"), b -> switchCategory(0)).bounds(startX, tabY, tabWidth, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("category.playeresp.utility"), b -> switchCategory(1)).bounds(startX + tabWidth + tabSpacing, tabY, tabWidth, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("category.playeresp.potion"), b -> switchCategory(2)).bounds(startX + (tabWidth + tabSpacing) * 2, tabY, tabWidth, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("category.playeresp.keys"), b -> switchCategory(3)).bounds(startX + (tabWidth + tabSpacing) * 3, tabY, tabWidth, 20).build());

        int startY = this.height / 2 - 50;
        int step = 25;
        int x = this.width / 2 - 100;

        if (currentCategory == 0) {
            addToggle(x, startY, "feature.playeresp", () -> PlayerEspMod.config.enabled, v -> PlayerEspMod.config.enabled = v);
            addToggle(x, startY + step, "feature.chestesp", () -> PlayerEspMod.config.chestEsp, v -> PlayerEspMod.config.chestEsp = v);
            addToggle(x, startY + step * 2, "feature.itemesp", () -> PlayerEspMod.config.itemEsp, v -> PlayerEspMod.config.itemEsp = v);
            addToggle(x, startY + step * 3, "feature.mobesp", () -> PlayerEspMod.config.mobEsp, v -> PlayerEspMod.config.mobEsp = v);
        } 
        else if (currentCategory == 1) {
            addToggle(x, startY, "feature.nofall", () -> PlayerEspMod.config.noFall, v -> PlayerEspMod.config.noFall = v);
            addToggle(x, startY + step, "feature.fullbright", () -> PlayerEspMod.config.fullbright, v -> PlayerEspMod.config.fullbright = v);
        } 
        else if (currentCategory == 2) {
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
        else if (currentCategory == 3) {
            addKeybindButton(x, startY, "key.playeresp.menu", "keyMenu", PlayerEspMod.config.keyMenu);
            addKeybindButton(x, startY + step, "key.playeresp.chestesp", "keyChestEsp", PlayerEspMod.config.keyChestEsp);
            addKeybindButton(x, startY + step * 2, "key.playeresp.itemesp", "keyItemEsp", PlayerEspMod.config.keyItemEsp);
            addKeybindButton(x, startY + step * 3, "key.playeresp.mobesp", "keyMobEsp", PlayerEspMod.config.keyMobEsp);
            addKeybindButton(x, startY + step * 4, "key.playeresp.nofall", "keyNoFall", PlayerEspMod.config.keyNoFall);
            addKeybindButton(x, startY + step * 5, "key.playeresp.fullbright", "keyFullbright", PlayerEspMod.config.keyFullbright);
        }

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> this.onClose()).bounds(this.width / 2 - 100, this.height - 30, 200, 20).build());
    }

    private void addToggle(int x, int y, String transKey, java.util.function.BooleanSupplier getter, java.util.function.Consumer<Boolean> setter) {
        addRenderableWidget(Button.builder(getText(transKey, getter.getAsBoolean()), b -> {
            boolean newVal = !getter.getAsBoolean();
            setter.accept(newVal);
            b.setMessage(getText(transKey, newVal));
            save();
        }).bounds(x, y, 200, 20).build());
    }

    private void addKeybindButton(int x, int y, String transKey, String configField, int currentKey) {
        String keyName = listeningForKey != null && listeningForKey.equals(configField) ? "> " + Component.translatable("gui.playeresp.listening").getString() + " <" : InputConstants.getKey(currentKey, 0).getDisplayName().getString();
        addRenderableWidget(Button.builder(Component.translatable(transKey).append(": §e" + keyName), b -> {
            listeningForKey = configField;
            rebuildWidgets();
        }).bounds(x, y, 200, 20).build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningForKey != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                listeningForKey = null;
                rebuildWidgets();
                return true;
            }
            
            if (listeningForKey.equals("keyMenu")) PlayerEspMod.config.keyMenu = keyCode;
            else if (listeningForKey.equals("keyChestEsp")) PlayerEspMod.config.keyChestEsp = keyCode;
            else if (listeningForKey.equals("keyItemEsp")) PlayerEspMod.config.keyItemEsp = keyCode;
            else if (listeningForKey.equals("keyMobEsp")) PlayerEspMod.config.keyMobEsp = keyCode;
            else if (listeningForKey.equals("keyNoFall")) PlayerEspMod.config.keyNoFall = keyCode;
            else if (listeningForKey.equals("keyFullbright")) PlayerEspMod.config.keyFullbright = keyCode;
            
            save();
            listeningForKey = null;
            rebuildWidgets();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void switchCategory(int newCategory) {
        this.currentCategory = newCategory;
        this.listeningForKey = null;
        this.rebuildWidgets();
    }

    private void save() { ConfigManager.save(PlayerEspMod.config); }

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
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 10, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}