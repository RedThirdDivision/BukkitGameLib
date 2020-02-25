package com.redthirddivision.bukkitgamelib.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CommandArgs {

    private final String[] args;
    private final int length;
    private final String base;

    public static CommandArgs getArgs(String base, String[] args, int start) {
        String a = "";
        int length = 0;
        for (int i = start; i < args.length; i++) {
            a += args[i] + ";";
            length++;
        }
        return new CommandArgs(a.split(";"), length, base);
    }

    private CommandArgs(String[] args, int length, String base) {
        this.args = args;
        this.length = length;
        this.base = base;
    }

    public String getString(int number) {
        return args[number];
    }

    public boolean isInteger(int number) {
        try {
            Integer.valueOf(args[number]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isDouble(int number) {
        try {
            Double.valueOf(args[number]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isEmpty() {
        return length < 1;
    }

    public int getLength() {
        return length;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean isPlayer(int num) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getDisplayName().equalsIgnoreCase(args[num]) || p.getName().equalsIgnoreCase(args[num])) {
                return true;
            }
        }
        return false;
    }

    public int getInt(int num) {
        return Integer.valueOf(args[num]);
    }

    public OfflinePlayer getOfflinePlayer(int num) {
        return Bukkit.getOfflinePlayer(args[num]);
    }

    public Player getPlayer(int i) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getDisplayName().equalsIgnoreCase(args[i]) || p.getName().equalsIgnoreCase(args[i])) {
                return p;
            }
        }
        return null;
    }

    public String getBase() {
        return base;
    }
    
    
}
