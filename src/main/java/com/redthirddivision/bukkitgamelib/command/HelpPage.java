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

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> HelpPage.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class HelpPage {
    
    public HelpPage(String command) {
        this.command = command;
    }
    
    private final List<CommandHelp> helpPages = new ArrayList<>();
    private final List<String> HELP_TEXT = new ArrayList<>();
    private final String command;
    
    public void addPage(String argument, String description) {
        helpPages.add(new CommandHelp(command + argument, description));
    }
    
    public void prepare() {
        if (helpPages == null || helpPages.isEmpty()) return;
        HELP_TEXT.add(ChatColor.GREEN + "------------------------" + ChatColor.BLUE + "Help" + ChatColor.GREEN + "-------------------------");
        for (CommandHelp ch : helpPages) {
            HELP_TEXT.add(ChatColor.GOLD + "/" + ch.getText());
        }
        HELP_TEXT.add(ChatColor.GREEN + "-----------------------------------------------------");
    }
    
    public boolean sendHelp(CommandSender s, CommandArgs args) {
        if (args.getLength() == 1 && (args.getString(0).equalsIgnoreCase("?") || args.getString(0).equalsIgnoreCase("help")) && !HELP_TEXT.isEmpty()) {
            for (String string : HELP_TEXT) {
                s.sendMessage(string);
            }
            return true;
        }
        return false;
    }
    
    private class CommandHelp {
        
        private final String FULL_TEXT;
        
        public CommandHelp(String cmd, String description) {
            this.FULL_TEXT = ChatColor.GOLD + cmd + ChatColor.GRAY + " - " + description;
        }
        
        public String getText() {
            return FULL_TEXT;
        }
        
    }
    
}
