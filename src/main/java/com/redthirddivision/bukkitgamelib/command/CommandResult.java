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

import org.bukkit.ChatColor;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> CommandResult.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
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
