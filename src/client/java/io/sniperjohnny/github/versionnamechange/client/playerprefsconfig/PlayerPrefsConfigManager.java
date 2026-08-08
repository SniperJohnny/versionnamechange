package io.sniperjohnny.github.versionnamechange.client.playerprefsconfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sniperjohnny.github.versionnamechange.VersionnameChange;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerPrefsConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final File CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve(VersionnameChange.MOD_ID)
            .resolve("configs")
            .resolve(VersionnameChange.MOD_ID + "_Current_Version_Name.json")
            .toFile();

    private static PlayerPrefsConfig currentplayerversionConfig;

    public static PlayerPrefsConfig getConfig() {
        if (currentplayerversionConfig == null) {
            load();
        }
        return currentplayerversionConfig;
    }

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            currentplayerversionConfig = new PlayerPrefsConfig();
            currentplayerversionConfig.newVersionName = StandardConfigManager.getConfig().standardmodversionnumber;
            save();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            currentplayerversionConfig = GSON.fromJson(reader, PlayerPrefsConfig.class);
            if (currentplayerversionConfig == null) {
                currentplayerversionConfig = new PlayerPrefsConfig();
            }
        } catch (IOException e) {
            System.err.println("[" + VersionnameChange.MOD_ID + "] Failed to load client config, using defaults.");
            currentplayerversionConfig = new PlayerPrefsConfig();
        }
    }

    public static void save() {
        if (currentplayerversionConfig == null) {
            currentplayerversionConfig = new PlayerPrefsConfig();
        }

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(currentplayerversionConfig, writer);
        } catch (IOException e) {
            System.err.println("[" + VersionnameChange.MOD_ID + "] Failed to save client config.");
        }
    }
}
