package com.yunlv.playeresp;

import com.mojang.blaze3d.platform.InputConstants;
import com.yunlv.playeresp.event.RenderEventManager;
import com.yunlv.playeresp.feature.*;
import com.yunlv.playeresp.gui.PlayerEspScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class PlayerEspMod implements ClientModInitializer {
    public static PlayerEspConfig config;
    
    // 记录按键状态，用于防连发
    private static boolean menuWasDown = false;
    private static boolean chestWasDown = false;
    private static boolean itemWasDown = false;
    private static boolean mobWasDown = false;
    private static boolean noFallWasDown = false;
    private static boolean brightWasDown = false;

    @Override
    public void onInitializeClient() {
        config = ConfigManager.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.screen != null) {
                // 在菜单中时，将按键状态重置，避免关闭菜单后触发
                menuWasDown = false; chestWasDown = false; itemWasDown = false;
                mobWasDown = false; noFallWasDown = false; brightWasDown = false;
                return;
            }

            long window = client.getWindow().getWindow();

            // 辅助防连发逻辑
            boolean menuDown = InputConstants.isKeyDown(window, config.keyMenu);
            if (menuDown && !menuWasDown) client.setScreen(new PlayerEspScreen());
            menuWasDown = menuDown;

            boolean chestDown = InputConstants.isKeyDown(window, config.keyChestEsp);
            if (chestDown && !chestWasDown) { config.chestEsp = !config.chestEsp; ConfigManager.save(config); }
            chestWasDown = chestDown;

            boolean itemDown = InputConstants.isKeyDown(window, config.keyItemEsp);
            if (itemDown && !itemWasDown) { config.itemEsp = !config.itemEsp; ConfigManager.save(config); }
            itemWasDown = itemDown;

            boolean mobDown = InputConstants.isKeyDown(window, config.keyMobEsp);
            if (mobDown && !mobWasDown) { config.mobEsp = !config.mobEsp; ConfigManager.save(config); }
            mobWasDown = mobDown;

            boolean noFallDown = InputConstants.isKeyDown(window, config.keyNoFall);
            if (noFallDown && !noFallWasDown) { config.noFall = !config.noFall; ConfigManager.save(config); }
            noFallWasDown = noFallDown;

            boolean brightDown = InputConstants.isKeyDown(window, config.keyFullbright);
            if (brightDown && !brightWasDown) { config.fullbright = !config.fullbright; ConfigManager.save(config); }
            brightWasDown = brightDown;

            // 执行渲染 Tick
            FullbrightHandler.tick(client);
            NoFallHandler.tick(client);
            ChestCollector.tick(client);
            ItemCollector.update(client);
            MobCollector.update(client);

            if (config.enabled) PlayerCollector.update(client);
            else PlayerCollector.clear();
        });

        RenderEventManager.register(new PlayerEspRenderer());
    }
}