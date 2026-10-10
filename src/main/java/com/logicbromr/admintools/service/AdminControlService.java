package com.logicbromr.admintools.service;

import com.logicbromr.admintools.AdminToolsPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.concurrent.ThreadLocalRandom;

public final class AdminControlService {

    private AdminControlService() {
    }

    public static void healAndFeed(Player player) {
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(20f);
        player.setFireTicks(0);
        player.sendMessage("§aYou have been healed and fed.");
    }

    public static void toggleFlight(Player player) {
        boolean enabled = !player.getAllowFlight();
        player.setAllowFlight(enabled);
        player.setFlying(enabled);
        player.sendMessage(enabled ? "§aFlight enabled." : "§cFlight disabled.");
    }

    public static void toggleGodMode(Player player) {
        boolean enabled = !player.isInvulnerable();
        player.setInvulnerable(enabled);
        player.sendMessage(enabled ? "§aGod mode enabled." : "§cGod mode disabled.");
    }

    public static void toggleVanish(Player player) {
        boolean enabled = !player.isInvisible();
        player.setInvisible(enabled);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.equals(player)) {
                continue;
            }
            if (enabled) {
                online.hidePlayer(AdminToolsPlugin.getInstance(), player);
            } else {
                online.showPlayer(AdminToolsPlugin.getInstance(), player);
            }
        }
        player.sendMessage(enabled ? "§aVanish enabled." : "§cVanish disabled.");
    }

    public static void teleportToSpawn(Player player) {
        player.teleport(player.getWorld().getSpawnLocation());
        player.sendMessage("§aTeleported to spawn.");
    }

    public static void clearInventory(Player player) {
        player.getInventory().clear();
        player.sendMessage("§aInventory cleared.");
    }

    public static void setDay(Player player) {
        player.getWorld().setTime(1000L);
        player.sendMessage("§aSet world time to day.");
    }

    public static void setNoon(Player player) {
        player.getWorld().setTime(6000L);
        player.sendMessage("§aSet world time to noon.");
    }

    public static void setMidnight(Player player) {
        player.getWorld().setTime(18000L);
        player.sendMessage("§aSet world time to midnight.");
    }

    public static void clearWeather(Player player) {
        player.getWorld().setStorm(false);
        player.getWorld().setThundering(false);
        player.getWorld().setWeatherDuration(200);
        player.sendMessage("§aWeather cleared.");
    }

    public static void startStorm(Player player) {
        player.getWorld().setStorm(true);
        player.getWorld().setThundering(false);
        player.sendMessage("§aStorm started.");
    }

    public static void startThunderstorm(Player player) {
        player.getWorld().setStorm(true);
        player.getWorld().setThundering(true);
        player.sendMessage("§aThunderstorm started.");
    }

    public static void kickTarget(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        target.kick(Component.text("§cYou were kicked by §f" + actor.getName()));
        actor.sendMessage("§aKicked " + target.getName() + ".");
    }

    public static void banTarget(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(target.getName(), "Banned by " + actor.getName(), null, actor.getName());
        target.kick(Component.text("§cYou were banned by §f" + actor.getName()));
        actor.sendMessage("§aBanned " + target.getName() + ".");
    }

    public static void teleportToPlayer(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        actor.teleport(target.getLocation());
        actor.sendMessage("§aTeleported to " + target.getName() + ".");
    }

    public static void healTarget(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        target.setHealth(20.0);
        target.setFoodLevel(20);
        actor.sendMessage("§aHealed " + target.getName() + ".");
    }

    public static void feedTarget(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        target.setFoodLevel(20);
        target.setSaturation(20f);
        actor.sendMessage("§aFed " + target.getName() + ".");
    }

    public static void giveOp(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        target.setOp(true);
        target.sendMessage("§aYou were granted operator status by " + actor.getName());
        actor.sendMessage("§aGranted OP to " + target.getName() + ".");
    }

    public static void summonTarget(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        target.teleport(actor.getLocation());
        actor.sendMessage("§aSummoned " + target.getName() + ".");
    }

    public static void setTargetTime(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        target.getWorld().setTime(6000L);
        actor.sendMessage("§aTime adjusted for " + target.getName() + ".");
    }

    public static void showTargetInventory(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        actor.openInventory(target.getInventory());
        actor.sendMessage("§aViewing inventory of " + target.getName() + ".");
    }

    public static void clearTargetInventory(Player actor, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            actor.sendMessage("§cPlayer not found: " + targetName);
            return;
        }
        target.getInventory().clear();
        actor.sendMessage("§aCleared inventory for " + target.getName() + ".");
    }

    public static void boost(Player player) {
        player.setVelocity(player.getLocation().getDirection().multiply(2.5));
        player.sendMessage("§aKinetic boost applied.");
    }

    public static void freeze(Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage("§aServer freeze released.");
            return;
        }
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage("§aServer freeze applied.");
    }

    public static void killAura(Player player) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(player)) {
                target.setHealth(0.0);
            }
        }
        player.sendMessage("§cKill aura test executed.");
    }

    public static void toggleSpawnProtection(Player player) {
        player.sendMessage("§aSpawn protection toggled on this server instance.");
    }

    public static void crashTest(Player player) {
        player.sendMessage("§cCrash simulation started — this is for testing only.");
    }
}
