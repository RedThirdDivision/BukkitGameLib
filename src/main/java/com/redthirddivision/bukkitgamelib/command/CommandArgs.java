/*
 * Copyright 2014 RedThirdDivision.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redthirddivision.bukkitgamelib.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> CommandArgs.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class CommandArgs {

    private final String[] args;
    private final int length;

    public static CommandArgs getArgs(String[] args, int start) {
        String a = "";
        int length = 0;
        for (int i = start; i < args.length; i++) {
            a += args[i] + ";";
            length++;
        }
        return new CommandArgs(a.split(";"), length);
    }

    private CommandArgs(String[] args, int length) {
        this.args = args;
        this.length = length;
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
}
