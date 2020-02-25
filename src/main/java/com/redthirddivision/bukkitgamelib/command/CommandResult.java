package com.redthirddivision.bukkitgamelib.command;

import org.bukkit.ChatColor;

public enum CommandResult {

    SUCCESS(null),
    NO_PERMISSION(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GRAY + "You don't have permission to do that! " + ChatColor.RED + "(%perm%)"),
    NO_PERMISSION_OTHER(ChatColor.DARK_RED + "[ERROR] " + ChatColor.GRAY + "You don't have permission to do that! " + ChatColor.RED + "(%perm%.other)"),
    ERROR(ChatColor.RED + "[ERROR] " + ChatColor.GRAY + "Wrong usage! Please use " + ChatColor.GOLD + "/%cmd% help " + ChatColor.GRAY + " to see the correct usage!"),
    ONLY_PLAYER(ChatColor.RED + "[ERROR] " + ChatColor.GRAY + "This command is only for players!"),
    NOT_ONLINE(ChatColor.RED + "[ERROR] " + ChatColor.GRAY + "That player is not online."),
    NOT_A_NUMBER(ChatColor.RED + "[ERROR] " + ChatColor.GRAY + "This is not a number!");

    private final String msg;

    CommandResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
}
