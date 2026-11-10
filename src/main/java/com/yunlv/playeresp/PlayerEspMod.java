package com.yunlv.playeresp;

import com.mojang.blaze3d.platform.InputConstants;
import com.yunlv.playeresp.event.RenderEventManager;
import com.yunlv.playeresp.feature.*;
import com.yunlv.playeresp.gui.PlayerEspScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class PlayerEspMod implements ClientModInitializer {
    public static PlayerEspConfig config;
    public static KeyMapping menuKey, chestEspKey, itemEspKey, mobEspKey, noFallKey, fullbrightKey;

    @Override
    public void onInitializeClient() {
        config = ConfigManager.load();

        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.parse("playeresp:main")
        );

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.menu", InputConstants.Type.KEYSYM,
            config.keyMenu, category));

        chestEspKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.chestesp", InputConstants.Type.KEYSYM,
            config.keyChestEsp, category));

        itemEspKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.itemesp", InputConstants.Type.KEYSYM,
            config.keyItemEsp, category));

        mobEspKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.mobesp", InputConstants.Type.KEYSYM,
            config.keyMobEsp, category));

        noFallKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.nofall", InputConstants.Type.KEYSYM,
            config.keyNoFall, category));

        fullbrightKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.fullbright", InputConstants.Type.KEYSYM,
            config.keyFullbright, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new PlayerEspScreen());
                }
            }
            while (chestEspKey.consumeClick()) {
                FeatureManager.chestEsp = !FeatureManager.chestEsp;
            }
            while (itemEspKey.consumeClick()) {
                FeatureManager.itemEsp = !FeatureManager.itemEsp;
            }
            while (mobEspKey.consumeClick()) {
                FeatureManager.mobEsp = !FeatureManager.mobEsp;
            }
            while (noFallKey.consumeClick()) {
                FeatureManager.noFall = !FeatureManager.noFall;
            }
            while (fullbrightKey.consumeClick()) {
                FeatureManager.fullbright = !FeatureManager.fullbright;
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