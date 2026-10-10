package com.logicbromr.admintools.gui;

import com.logicbromr.admintools.AdminToolsPlugin;
import com.logicbromr.admintools.service.AdminControlService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class AdminMenuFactory {

    private static final NamespacedKey ACTION_KEY = new NamespacedKey(AdminToolsPlugin.getInstance(), "admin_action");
    private static final NamespacedKey TARGET_KEY = new NamespacedKey(AdminToolsPlugin.getInstance(), "admin_target");

    private AdminMenuFactory() {
    }

    public static void openMainMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(new MenuHolder(MenuHolder.MenuType.MAIN, 1, null), 54, Component.text("Admin Console"));
        MenuHolder holder = (MenuHolder) inventory.getHolder();
        holder.setInventory(inventory);
        fillBorder(inventory);

        addActionItem(inventory, 10, Material.GOLDEN_APPLE, "§aHeal & Feed", "heal_feed", null);
        addActionItem(inventory, 12, Material.ENDER_EYE, "§aToggle Flight", "toggle_flight", null);
        addActionItem(inventory, 14, Material.IRON_SWORD, "§aToggle God Mode", "toggle_god", null);
        addActionItem(inventory, 16, Material.BARRIER, "§aToggle Vanish", "toggle_vanish", null);

        addActionItem(inventory, 19, Material.ENDER_PEARL, "§bTeleport to Spawn", "tp_spawn", null);
        addActionItem(inventory, 21, Material.COMPASS, "§bOnline Players", "open_players", null);
        addActionItem(inventory, 23, Material.FEATHER, "§bWorld Control", "open_worlds", null);
        addActionItem(inventory, 25, Material.CHEST, "§bClear Inventory", "clear_inventory", null);

        addActionItem(inventory, 31, Material.CLOCK, "§6Set Day", "set_day", null);
        addActionItem(inventory, 33, Material.WATER_BUCKET, "§6Clear Weather", "clear_weather", null);
        addActionItem(inventory, 39, Material.REDSTONE_BLOCK, "§cKinetic Boost", "boost", null);
        addActionItem(inventory, 41, Material.DIAMOND_BLOCK, "§cServer Freeze", "freeze", null);
        addActionItem(inventory, 43, Material.BEACON, "§cKill Aura Test", "kill_aura", null);

        player.openInventory(inventory);
    }

    public static void openPlayerListMenu(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(new MenuHolder(MenuHolder.MenuType.PLAYER_LIST, page, null), 54, Component.text("Select Target"));
        MenuHolder holder = (MenuHolder) inventory.getHolder();
        holder.setInventory(inventory);
        fillBorder(inventory);

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        onlinePlayers.sort(Comparator.comparing(Player::getName));

        int itemsPerPage = 45;
        int maxPage = Math.max(1, (int) Math.ceil(onlinePlayers.size() / (double) itemsPerPage));
        int safePage = Math.max(1, Math.min(page, maxPage));

        int startIndex = (safePage - 1) * itemsPerPage;
        int slot = 10;

        for (int i = startIndex; i < onlinePlayers.size() && slot < 53; i++) {
            Player target = onlinePlayers.get(i);
            ItemStack head = createPlayerHead(target);
            ItemMeta meta = head.getItemMeta();
            meta.displayName(Component.text("§a" + target.getName()));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Click to open actions"));
            lore.add(Component.text("§7Health: §f" + Math.ceil(target.getHealth())));
            lore.add(Component.text("§7World: §f" + target.getWorld().getName()));
            meta.lore(lore);
            meta.getPersistentDataContainer().set(ACTION_KEY, PersistentDataType.STRING, "open_player_actions");
            meta.getPersistentDataContainer().set(TARGET_KEY, PersistentDataType.STRING, target.getName());
            head.setItemMeta(meta);
            inventory.setItem(slot, head);
            slot++;
            if (slot == 17 || slot == 26 || slot == 35 || slot == 44) {
                slot += 2;
            }
        }

        if (safePage > 1) {
            addActionItem(inventory, 48, Material.ARROW, "§7Previous Page", "page_prev", null);
        }

        addActionItem(inventory, 49, Material.DIAMOND, "§dMain Menu", "main_menu", null);

        if (safePage < maxPage) {
            addActionItem(inventory, 50, Material.ARROW, "§7Next Page", "page_next", null);
        }

        if (onlinePlayers.isEmpty()) {
            ItemStack none = new ItemStack(Material.BARRIER);
            ItemMeta noneMeta = none.getItemMeta();
            noneMeta.displayName(Component.text("§cNo players online"));
            none.setItemMeta(noneMeta);
            inventory.setItem(22, none);
        }

        player.openInventory(inventory);
    }

    public static void openPlayerActionMenu(Player player, Player target) {
        Inventory inventory = Bukkit.createInventory(new MenuHolder(MenuHolder.MenuType.PLAYER_ACTIONS, 1, target.getName()), 54, Component.text("Player Actions: " + target.getName()));
        MenuHolder holder = (MenuHolder) inventory.getHolder();
        holder.setInventory(inventory);
        fillBorder(inventory);

        addActionItem(inventory, 10, Material.ENDER_PEARL, "§aTeleport", "player_tp", target.getName());
        addActionItem(inventory, 13, Material.GOLDEN_APPLE, "§aHeal", "player_heal", target.getName());
        addActionItem(inventory, 16, Material.BREAD, "§aFeed", "player_feed", target.getName());

        addActionItem(inventory, 21, Material.EMERALD, "§dGive OP", "player_op", target.getName());
        addActionItem(inventory, 24, Material.IRON_SWORD, "§cKick", "player_kick", target.getName());
        addActionItem(inventory, 27, Material.BARRIER, "§cBan", "player_ban", target.getName());

        addActionItem(inventory, 31, Material.COMPASS, "§9Teleport to Me", "player_summon", target.getName());
        addActionItem(inventory, 34, Material.CLOCK, "§9Set Time", "player_time", target.getName());
        addActionItem(inventory, 39, Material.DIAMOND, "§7Back to List", "back_player_list", null);
        addActionItem(inventory, 41, Material.PAPER, "§7Inventory View", "player_inventory", target.getName());
        addActionItem(inventory, 43, Material.POTION, "§7Clear Items", "player_clear", target.getName());

        player.openInventory(inventory);
    }

    public static void openWorldControlMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(new MenuHolder(MenuHolder.MenuType.WORLD_CONTROL, 1, null), 54, Component.text("World Control"));
        MenuHolder holder = (MenuHolder) inventory.getHolder();
        holder.setInventory(inventory);
        fillBorder(inventory);

        addActionItem(inventory, 11, Material.SUNFLOWER, "§aSet Day", "set_day", null);
        addActionItem(inventory, 13, Material.MELON, "§aSet Noon", "set_noon", null);
        addActionItem(inventory, 15, Material.CLOCK, "§aSet Midnight", "set_midnight", null);

        addActionItem(inventory, 20, Material.WATER_BUCKET, "§bClear Weather", "clear_weather", null);
        addActionItem(inventory, 22, Material.BLUE_ICE, "§bStorm", "storm_weather", null);
        addActionItem(inventory, 24, Material.NETHER_STAR, "§bThunder", "thunder_weather", null);

        addActionItem(inventory, 29, Material.ENDER_PEARL, "§dTeleport Spawn", "tp_spawn", null);
        addActionItem(inventory, 31, Material.DIAMOND_BLOCK, "§dSpawn Protect", "spawn_protect", null);
        addActionItem(inventory, 33, Material.BEDROCK, "§dServer Crash Test", "crash_test", null);

        addActionItem(inventory, 40, Material.BARRIER, "§cMain Menu", "main_menu", null);
        addActionItem(inventory, 49, Material.ENDER_EYE, "§eRefresh", "refresh_menu", null);

        player.openInventory(inventory);
    }

    private static void addActionItem(Inventory inventory, int slot, Material material, String name, String action, String targetName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(ACTION_KEY, PersistentDataType.STRING, action);
        if (targetName != null && !targetName.isBlank()) {
            meta.getPersistentDataContainer().set(TARGET_KEY, PersistentDataType.STRING, targetName);
        }
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    private static ItemStack createPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        meta.displayName(Component.text("§a" + player.getName()));
        head.setItemMeta(meta);
        return head;
    }

    private static void fillBorder(Inventory inventory) {
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta();
        meta.displayName(Component.text(" "));
        filler.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (i < 9 || i > inventory.getSize() - 10 || i % 9 == 0 || i % 9 == 8) {
                inventory.setItem(i, filler);
            }
        }
    }

    public static void handleAction(Player player, String action, String targetName) {
        switch (action) {
            case "heal_feed" -> AdminControlService.healAndFeed(player);
            case "toggle_flight" -> AdminControlService.toggleFlight(player);
            case "toggle_god" -> AdminControlService.toggleGodMode(player);
            case "toggle_vanish" -> AdminControlService.toggleVanish(player);
            case "tp_spawn" -> AdminControlService.teleportToSpawn(player);
            case "open_players" -> openPlayerListMenu(player, 1);
            case "open_worlds" -> openWorldControlMenu(player);
            case "clear_inventory" -> AdminControlService.clearInventory(player);
            case "set_day" -> AdminControlService.setDay(player);
            case "set_noon" -> AdminControlService.setNoon(player);
            case "set_midnight" -> AdminControlService.setMidnight(player);
            case "clear_weather" -> AdminControlService.clearWeather(player);
            case "storm_weather" -> AdminControlService.startStorm(player);
            case "thunder_weather" -> AdminControlService.startThunderstorm(player);
            case "main_menu" -> openMainMenu(player);
            case "page_prev" -> openPlayerListMenu(player, Math.max(1, Integer.parseInt(player.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder holder ? String.valueOf(holder.getPage() - 1) : "1")));
            case "page_next" -> openPlayerListMenu(player, ((MenuHolder) player.getOpenInventory().getTopInventory().getHolder()).getPage() + 1);
            case "refresh_menu" -> openWorldControlMenu(player);
            case "boost" -> AdminControlService.boost(player);
            case "freeze" -> AdminControlService.freeze(player);
            case "kill_aura" -> AdminControlService.killAura(player);
            case "open_player_actions" -> {
                Player target = Bukkit.getPlayerExact(targetName);
                if (target != null) {
                    openPlayerActionMenu(player, target);
                }
            }
            case "back_player_list" -> openPlayerListMenu(player, 1);
            case "player_tp" -> AdminControlService.teleportToPlayer(player, targetName);
            case "player_heal" -> AdminControlService.healTarget(player, targetName);
            case "player_feed" -> AdminControlService.feedTarget(player, targetName);
            case "player_op" -> AdminControlService.giveOp(player, targetName);
            case "player_kick" -> AdminControlService.kickTarget(player, targetName);
            case "player_ban" -> AdminControlService.banTarget(player, targetName);
            case "player_summon" -> AdminControlService.summonTarget(player, targetName);
            case "player_time" -> AdminControlService.setTargetTime(player, targetName);
            case "player_inventory" -> AdminControlService.showTargetInventory(player, targetName);
            case "player_clear" -> AdminControlService.clearTargetInventory(player, targetName);
            case "spawn_protect" -> AdminControlService.toggleSpawnProtection(player);
            case "crash_test" -> AdminControlService.crashTest(player);
            default -> player.sendMessage("§cUnknown action: " + action);
        }
    }
}
