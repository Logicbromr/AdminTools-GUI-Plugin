package com.logicbromr.admintools;

import com.logicbromr.admintools.command.AdminMenuCommand;
import com.logicbromr.admintools.listener.AdminMenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdminToolsPlugin extends JavaPlugin {

    private static AdminToolsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getCommand("adminmenu").setExecutor(new AdminMenuCommand(this));
        getServer().getPluginManager().registerEvents(new AdminMenuListener(this), this);

        getLogger().info("AdminTools premium GUI enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AdminTools premium GUI disabled.");
    }

    public static AdminToolsPlugin getInstance() {
        return instance;
    }
}
