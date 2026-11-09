package com.yunlv.playeresp;

import com.mojang.blaze3d.platform.InputConstants;
import com.yunlv.playeresp.event.RenderEventManager;
import com.yunlv.playeresp.feature.FeatureManager;
import com.yunlv.playeresp.feature.FreecamHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class PlayerEspMod implements ClientModInitializer {
    public static PlayerEspConfig config;
    public static KeyMapping toggleKey;
    public static KeyMapping freecamKey;

    @Override
    public void onInitializeClient() {
        config = new PlayerEspConfig();

        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.parse("playeresp:main")
        );

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            category
        ));

        freecamKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.freecam",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F,
            category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // 退出世界时清理
            if (client.player == null || client.level == null) {
                if (FreecamHandler.isActive()) {
                    FreecamHandler.cleanup();
                    FeatureManager.freecam = false;
                }
                return;
            }

            while (toggleKey.consumeClick()) {
                config.enabled = !config.enabled;
                FeatureManager.playerEsp = config.enabled;
            }
            while (freecamKey.consumeClick()) {
                FeatureManager.freecam = !FeatureManager.freecam;
                FreecamHandler.toggle(client);
            }

            FreecamHandler.tick(client);

            if (config.enabled) {
                PlayerCollector.update(client);
            } else {
                PlayerCollector.clear();
            }
        });

        RenderEventManager.register(new PlayerEspRenderer());
    }
}