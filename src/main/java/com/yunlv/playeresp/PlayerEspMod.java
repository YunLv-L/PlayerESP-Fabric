package com.yunlv.playeresp;

import com.mojang.blaze3d.platform.InputConstants;
import com.yunlv.playeresp.event.RenderEventManager;
import com.yunlv.playeresp.feature.FeatureManager;
import com.yunlv.playeresp.feature.FullbrightHandler;
import com.yunlv.playeresp.feature.NoFallHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class PlayerEspMod implements ClientModInitializer {
    public static PlayerEspConfig config;
    public static KeyMapping toggleKey;
    public static KeyMapping noFallKey;
    public static KeyMapping fullbrightKey;

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

        noFallKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.nofall",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            category
        ));

        fullbrightKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.fullbright",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                config.enabled = !config.enabled;
            }
            while (noFallKey.consumeClick()) {
                FeatureManager.noFall = !FeatureManager.noFall;
            }
            while (fullbrightKey.consumeClick()) {
                FeatureManager.fullbright = !FeatureManager.fullbright;
            }

            FullbrightHandler.tick(client);
            NoFallHandler.tick(client);

            if (config.enabled) {
                PlayerCollector.update(client);
            } else {
                PlayerCollector.clear();
            }
        });

        RenderEventManager.register(new PlayerEspRenderer());
    }
}