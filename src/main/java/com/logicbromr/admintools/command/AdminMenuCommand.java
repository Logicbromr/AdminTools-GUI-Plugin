package com.logicbromr.admintools.command;

import com.logicbromr.admintools.AdminToolsPlugin;
import com.logicbromr.admintools.gui.AdminMenuFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class AdminMenuCommand implements CommandExecutor {

    private final AdminToolsPlugin plugin;

    public AdminMenuCommand(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("admintools.use")) {
            sender.sendMessage("§cYou do not have permission to use AdminTools.");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("admintools.reload")) {
                sender.sendMessage("§cYou do not have permission to reload AdminTools.");
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage("§aAdminTools reloaded.");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can open the admin menu.");
            return true;
        }

        AdminMenuFactory.openMainMenu(player);
        return true;
    }
}
