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
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> Minigame.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public abstract class Minigame extends JavaPlugin {

    private GameManager manager;

    /**
     * Called when the plugin is allowed to load stuff, does the same like Bukkits onEnable method
     */
    public abstract void onPluginStart();

    /**
     * Called when the plugin is allowed to stop stuff, does the same like Bukkits onDisable method
     */
    public abstract void onPluginStop();

    @Override
    public void onEnable() {
        manager = new GameManager(this);
        getServer().getPluginManager().registerEvents(new BukkitInventoryEvent(this), this);
        getServer().getPluginManager().registerEvents(new BukkitMoveEvent(this), this);
        getServer().getPluginManager().registerEvents(new BukkitQuitEvent(this), this);
        getServer().getPluginManager().registerEvents(new BukkitSignEvent(this), this);
        onPluginStart();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        for (Game g : manager.getArenas().values()) {
            if (g.isStarted()) g.stop();
        }
        onPluginStop();
    }

    /**
     * The GameManager of this plugin
     *
     * @return {@link com.redthirddivision.bukkitgamelib.arena.GameManager}
     */
    public GameManager getGameManager() {
        return manager;
    }

}
