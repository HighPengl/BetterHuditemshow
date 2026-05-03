package com.example.betteritem;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BetterItemPlugin extends JavaPlugin {
    private static BetterItemPlugin instance;
    private final Map<UUID, LinkedList<PickupRecord>> playerRecords = new ConcurrentHashMap<>();
    private DisplayConfig displayConfig;
    private Map<String, String> chineseNames = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadChineseNames();
        reloadConfigValues();
        getServer().getPluginManager().registerEvents(new PickupListener(this), this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new ItemPlaceholders(this).register();
        }
        getCommand("bhitem").setExecutor(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        playerRecords.clear();
    }

    public void loadChineseNames() {
        File file = new File(getDataFolder(), "chinese.yml");
        if (!file.exists()) {
            saveResource("chinese.yml", false);
        }
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        chineseNames.clear();
        for (String key : yml.getKeys(false)) {
            chineseNames.put(key, yml.getString(key));
        }
        getLogger().info("已加载 " + chineseNames.size() + " 个物品中文名映射。");
    }

    public void reloadConfigValues() {
        reloadConfig();
        String nameFormat = getConfig().getString("display.name-format", "%name% x%amount%");
        int pickedDuration = getConfig().getInt("display.picked-duration", 5);
        int maxRecords = getConfig().getInt("display.max-records", 5);
        DisplayConfig.ColorMode colorMode = DisplayConfig.ColorMode.fromString(
                getConfig().getString("color-mode", "none")
        );
        displayConfig = new DisplayConfig(nameFormat, pickedDuration, maxRecords, colorMode);
        loadChineseNames();
    }

    public static BetterItemPlugin getInstance() { return instance; }
    public Map<UUID, LinkedList<PickupRecord>> getPlayerRecords() { return playerRecords; }
    public DisplayConfig getDisplayConfig() { return displayConfig; }
    public Map<String, String> getChineseNames() { return chineseNames; }
}