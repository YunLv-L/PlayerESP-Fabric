package com.yunlv.playeresp;

import com.mojang.blaze3d.platform.InputConstants;
import com.yunlv.playeresp.event.RenderEventManager;
import com.yunlv.playeresp.feature.ChestCollector;
import com.yunlv.playeresp.feature.FeatureManager;
import com.yunlv.playeresp.feature.FullbrightHandler;
import com.yunlv.playeresp.feature.ItemCollector;
import com.yunlv.playeresp.feature.MobCollector;
import com.yunlv.playeresp.feature.NoFallHandler;
import com.yunlv.playeresp.gui.PlayerEspScreen;
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
            "key.playeresp.menu", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new PlayerEspScreen());
                }
            }

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