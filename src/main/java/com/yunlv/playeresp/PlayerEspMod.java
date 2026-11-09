package com.yunlv.playeresp;

import com.mojang.blaze3d.platform.InputConstants;
import com.yunlv.playeresp.event.RenderEventManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class PlayerEspMod implements ClientModInitializer {
    public static PlayerEspConfig config;
    public static KeyMapping toggleKey;

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

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                config.enabled = !config.enabled;
            }
            if (config.enabled) {
                PlayerCollector.update(client);
            } else {
                PlayerCollector.clear();
            }
        });

        RenderEventManager.register(new PlayerEspRenderer());
    }
}