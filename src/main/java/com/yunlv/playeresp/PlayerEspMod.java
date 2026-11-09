package com.yunlv.playeresp;

import com.mojang.blaze3d.platform.InputConstants;
import com.yunlv.playeresp.event.RenderEventManager;
import com.yunlv.playeresp.feature.ChestCollector;
import com.yunlv.playeresp.feature.FeatureManager;
import com.yunlv.playeresp.feature.FullbrightHandler;
import com.yunlv.playeresp.feature.ItemCollector;
import com.yunlv.playeresp.feature.MobCollector;
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
    public static KeyMapping chestEspKey;
    public static KeyMapping itemEspKey;
    public static KeyMapping mobEspKey;
    public static KeyMapping nameTagsKey;
    public static KeyMapping noFallKey;
    public static KeyMapping fullbrightKey;

    @Override
    public void onInitializeClient() {
        config = new PlayerEspConfig();

        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.parse("playeresp:main")
        );

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.toggle", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R, category));

        chestEspKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.chestesp", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C, category));

        itemEspKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.itemesp", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I, category));

        mobEspKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.mobesp", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M, category));

        nameTagsKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.nametags", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_T, category));

        noFallKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.nofall", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N, category));

        fullbrightKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.playeresp.fullbright", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) config.enabled = !config.enabled;
            while (chestEspKey.consumeClick()) FeatureManager.chestEsp = !FeatureManager.chestEsp;
            while (itemEspKey.consumeClick()) FeatureManager.itemEsp = !FeatureManager.itemEsp;
            while (mobEspKey.consumeClick()) FeatureManager.mobEsp = !FeatureManager.mobEsp;
            while (nameTagsKey.consumeClick()) FeatureManager.nameTags = !FeatureManager.nameTags;
            while (noFallKey.consumeClick()) FeatureManager.noFall = !FeatureManager.noFall;
            while (fullbrightKey.consumeClick()) FeatureManager.fullbright = !FeatureManager.fullbright;

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