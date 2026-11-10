package com.yunlv.playeresp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance()
        .getConfigDir().resolve("playeresp.json").toFile();

    public static void save(PlayerEspConfig config) {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerEspConfig load() {
        if (!CONFIG_FILE.exists()) return new PlayerEspConfig();
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            PlayerEspConfig cfg = GSON.fromJson(reader, PlayerEspConfig.class);
            return cfg != null ? cfg : new PlayerEspConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return new PlayerEspConfig();
        }
    }
}