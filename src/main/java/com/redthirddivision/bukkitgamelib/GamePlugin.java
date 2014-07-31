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
package com.redthirddivision.bukkitgamelib;

import com.redthirddivision.bukkitgamelib.arena.GameManager;
import com.redthirddivision.bukkitgamelib.listeners.BukkitInventoryEvent;
import com.redthirddivision.bukkitgamelib.listeners.BukkitMoveEvent;
import com.redthirddivision.bukkitgamelib.listeners.BukkitQuitEvent;
import com.redthirddivision.bukkitgamelib.listeners.BukkitSignEvent;
import de.thejeterlp.bukkit.bukkittools.command.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> GamePlugin.java
 * 
 * @author: <a href="http://jeter.vc-network.com">TheJeterLP</a>
 */
public abstract class GamePlugin extends JavaPlugin {

    private GameManager manager;
    private CommandManager cmdmanager;

    public abstract void onPluginStart();

    public abstract void onPluginStop();

    @Override
    public void onEnable() {
        manager = new GameManager(this);
        cmdmanager = new CommandManager(this);
        getServer().getPluginManager().registerEvents(new BukkitInventoryEvent(), this);
        getServer().getPluginManager().registerEvents(new BukkitMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new BukkitQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new BukkitSignEvent(), this);
        onPluginStart();
    }

    @Override
    public void onDisable() {
        onPluginStop();
    }

    public GameManager getGameManager() {
        return manager;
    }

    public CommandManager getCommandManager() {
        return cmdmanager;
    }

}
