package com.logicbromr.admintools.listener;

import com.logicbromr.admintools.AdminToolsPlugin;
import com.logicbromr.admintools.gui.AdminMenuFactory;
import com.logicbromr.admintools.gui.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class AdminMenuListener implements Listener {

    private final AdminToolsPlugin plugin;

    public AdminMenuListener(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!(event.getInventory().getHolder() instanceof MenuHolder holder)) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType().isAir()) {
            return;
        }

        ItemStack item = event.getCurrentItem();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        String action = meta.getPersistentDataContainer().get(new org.bukkit.NamespacedKey(plugin, "admin_action"), PersistentDataType.STRING);
        String targetName = meta.getPersistentDataContainer().get(new org.bukkit.NamespacedKey(plugin, "admin_target"), PersistentDataType.STRING);

        if (action == null) {
            return;
        }

        AdminMenuFactory.handleAction(player, action, targetName);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof MenuHolder) {
            event.setCancelled(true);
        }
    }
}
