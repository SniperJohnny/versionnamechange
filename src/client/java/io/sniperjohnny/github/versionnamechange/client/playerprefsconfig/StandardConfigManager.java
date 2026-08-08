package io.sniperjohnny.github.versionnamechange.client.playerprefsconfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sniperjohnny.github.versionnamechange.VersionnameChange;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StandardConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final File CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve(VersionnameChange.MOD_ID)
            .resolve("configs")
            .resolve(VersionnameChange.MOD_ID + "_standardVersionName.json")
            .toFile();

    private static StandardConfig standardConfig;

    public static StandardConfig getConfig() {
        if (standardConfig == null) {
            load();
        }
        return standardConfig;
    }

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            standardConfig = new StandardConfig();
            save();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            standardConfig = GSON.fromJson(reader, StandardConfig.class);
            if (standardConfig == null) {
                standardConfig = new StandardConfig();
            }
        } catch (IOException e) {
            System.err.println("[" + VersionnameChange.MOD_ID + "] Failed to load client config, using defaults.");
            standardConfig = new StandardConfig();
        }
    }

    public static void save() {
        if (standardConfig == null) {
            standardConfig = new StandardConfig();
        }

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(standardConfig, writer);
        } catch (IOException e) {
            System.err.println("[" + VersionnameChange.MOD_ID + "] Failed to save client config.");
        }
    }
}
