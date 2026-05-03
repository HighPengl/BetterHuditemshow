package com.example.betteritem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class PickupListener implements Listener {
    private final BetterItemPlugin plugin;

    public PickupListener(BetterItemPlugin plugin) {
        this.plugin = plugin;
    }

    private static final Map<String, String> CHINESE_NAMES = new HashMap<>();
    static {
        // ============ 建筑方块 ============
        CHINESE_NAMES.put("air", "空气"); CHINESE_NAMES.put("stone", "石头");
        // ... 你的所有映射 ...

        // 如果你还需要更多，请继续按上述格式添加。
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack item = event.getItem().getItemStack();
        if (item == null || item.getType().isAir()) return;

        String rawName = getDisplayName(item, player);
        int amount = item.getAmount();

        DisplayConfig config = plugin.getDisplayConfig();
        String processedName;
        if (config.getColorMode() == DisplayConfig.ColorMode.MINIMESSAGE) {
            processedName = convertToMiniMessage(rawName);
        } else if (config.getColorMode() == DisplayConfig.ColorMode.STRIP) {
            processedName = stripAllColors(rawName);
        } else {
            processedName = rawName;
        }

        PickupRecord record = new PickupRecord(processedName, amount);

        Map<UUID, LinkedList<PickupRecord>> records = plugin.getPlayerRecords();
        LinkedList<PickupRecord> list = records.computeIfAbsent(player.getUniqueId(), k -> new LinkedList<>());
        list.addFirst(record);
        while (list.size() > config.getMaxRecords()) {
            list.removeLast();
        }
    }

    /**
     * 获取物品展示名：
     * 1. 自定义名称直接返回。
     * 2. 否则检查内置中文映射表，有则返回中文。
     * 3. 否则回退为英文材质小写名。
     */
    private String getDisplayName(ItemStack item, Player player) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }

        String matName = item.getType().name().toLowerCase();
        Map<String, String> chineseNames = plugin.getChineseNames();
        String chinese = chineseNames.get(matName);
        if (chinese != null) {
            return chinese;
        }

        return matName;
    }

    private String stripAllColors(String s) {
        return s.replaceAll("[§&][0-9a-fklmnor]", "");
    }

    private String convertToMiniMessage(String s) {
        s = s.replaceAll("[§&]0", "<black>");
        s = s.replaceAll("[§&]1", "<dark_blue>");
        s = s.replaceAll("[§&]2", "<dark_green>");
        s = s.replaceAll("[§&]3", "<dark_aqua>");
        s = s.replaceAll("[§&]4", "<dark_red>");
        s = s.replaceAll("[§&]5", "<dark_purple>");
        s = s.replaceAll("[§&]6", "<gold>");
        s = s.replaceAll("[§&]7", "<gray>");
        s = s.replaceAll("[§&]8", "<dark_gray>");
        s = s.replaceAll("[§&]9", "<blue>");
        s = s.replaceAll("[§&]a", "<green>");
        s = s.replaceAll("[§&]b", "<aqua>");
        s = s.replaceAll("[§&]c", "<red>");
        s = s.replaceAll("[§&]d", "<light_purple>");
        s = s.replaceAll("[§&]e", "<yellow>");
        s = s.replaceAll("[§&]f", "<white>");
        s = s.replaceAll("[§&]k", "<obfuscated>");
        s = s.replaceAll("[§&]l", "<bold>");
        s = s.replaceAll("[§&]m", "<strikethrough>");
        s = s.replaceAll("[§&]n", "<underlined>");
        s = s.replaceAll("[§&]o", "<italic>");
        s = s.replaceAll("[§&]r", "<reset>");
        return s;
    }
}