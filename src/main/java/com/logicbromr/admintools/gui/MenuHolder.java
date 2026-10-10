package com.logicbromr.admintools.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public final class MenuHolder implements InventoryHolder {

    public enum MenuType {
        MAIN,
        PLAYER_LIST,
        PLAYER_ACTIONS,
        WORLD_CONTROL
    }

    private final MenuType type;
    private final int page;
    private final String targetName;
    private Inventory inventory;

    public MenuHolder(MenuType type, int page, String targetName) {
        this.type = type;
        this.page = page;
        this.targetName = targetName;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public MenuType getType() {
        return type;
    }

    public int getPage() {
        return page;
    }

    public String getTargetName() {
        return targetName;
    }
}
