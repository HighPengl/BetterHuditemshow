package com.example.betteritem;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class ItemPlaceholders extends PlaceholderExpansion {
    private final BetterItemPlugin plugin;

    public ItemPlaceholders(BetterItemPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "item";
    }

    @Override
    public String getAuthor() {
        return "YourName";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        if (offlinePlayer == null) return "";
        Player player = offlinePlayer.getPlayer();
        if (player == null) return "";

        LinkedList<PickupRecord> list = plugin.getPlayerRecords().get(player.getUniqueId());
        if (list == null || list.isEmpty()) {
            return getEmptyValue(params);
        }
        DisplayConfig config = plugin.getDisplayConfig();

        // 处理 name1~name5
        if (params.startsWith("name")) {
            int index;
            try {
                index = Integer.parseInt(params.substring(4)) - 1;
            } catch (NumberFormatException e) {
                return "";
            }
            if (index < 0 || index >= list.size()) return "";
            PickupRecord record = list.get(index);
            return config.getNameFormat()
                    .replace("%name%", record.getFormattedName())
                    .replace("%amount%", String.valueOf(record.getAmount()));
        }
        // 处理 has_picked1~has_picked5
        else if (params.startsWith("has_picked")) {
            int index;
            try {
                index = Integer.parseInt(params.substring(10)) - 1;
            } catch (NumberFormatException e) {
                return "";
            }
            if (index < 0 || index >= list.size()) return "false";
            long elapsed = System.currentTimeMillis() - list.get(index).getTimestamp();
            return elapsed < config.getPickedDurationSeconds() * 1000L ? "true" : "false";
        }
        return "";
    }

    private String getEmptyValue(String params) {
        if (params.startsWith("has_picked")) {
            return "false";
        }
        return "";
    }
}