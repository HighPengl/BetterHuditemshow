package com.example.betteritem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
    private final BetterItemPlugin plugin;

    public ReloadCommand(BetterItemPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 权限检查：必须拥有 betteritem.admin 权限
        if (!sender.hasPermission("betteritem.admin")) {
            sender.sendMessage("§c你没有权限使用此命令！");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfigValues();
            sender.sendMessage("§aBetterItem 配置已重载！");
        } else {
            sender.sendMessage("§e用法: /bhitem reload");
        }
        return true;
    }
}