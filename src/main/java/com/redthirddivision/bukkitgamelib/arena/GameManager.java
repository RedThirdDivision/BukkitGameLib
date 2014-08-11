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
package com.redthirddivision.bukkitgamelib.arena;

import com.redthirddivision.bukkitgamelib.Game;
import com.redthirddivision.bukkitgamelib.GamePlugin;
import java.util.HashMap;
import org.bukkit.entity.Player;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> GameManager.java
 *
 * @author <a href="http://jeter.vc-network.com">TheJeterLP</a>
 */
public class GameManager {

    private final HashMap<String, Game> arenas = new HashMap<>();
    private final GamePlugin pl;
    private static GameManager INSTANCE;

    /**
     * Use this to get the instance of the GameManager
     *
     * @return {@link com.redthirddivision.bukkitgamelib.arena.GameManager#INSTANCE}
     */
    public static GameManager getInstance() {
        return INSTANCE;
    }

    public GameManager(GamePlugin plugin) {
        INSTANCE = this;
        this.pl = plugin;
    }

    /**
     * Gets the GamePlugin which belongs to this GameManager instance
     *
     * @return {@link com.redthirddivision.bukkitgamelib.GamePlugin}
     */
    public GamePlugin getPlugin() {
        return pl;
    }

    /**
     * Gets the Name of the plugin which can find on a sign.
     * Using {@link java.lang.String#substring(int, int) to cut it down to 16 characters}
     *
     * @return {@link java.lang.String}
     */
    public String getName() {
        String ret = pl.getName();
        if (ret.length() > 12) {
            ret = ret.substring(0, 12);
        }
        return ret;
    }

    /**
     * Checks if a Game is existing by this name
     *
     * @param name the name we search for
     * @return
     */
    public boolean isExisting(String name) {
        return arenas.containsKey(name);
    }

    /**
     * Checks if a Game is existing by Game instance
     *
     * @param arena the game we look for
     * @return
     */
    public boolean isExisting(Game arena) {
        return arenas.containsValue(arena);
    }

    /**
     * Gets the Game instance of an arena name or null if its not existing
     *
     * @param name The name of a game
     * @return
     */
    public Game getArena(String name) {
        return arenas.get(name);
    }

    /**
     * Loads a new Game instance
     *
     * @param a the game we want to load
     */
    public void load(Game a) {
        if (arenas.containsValue(a)) return;
        arenas.put(a.getName(), a);
    }

    /**
     * Get a Game instance by ID
     *
     * @param id the id we look for
     * @return
     */
    public Game getArena(int id) {
        for (Game a : arenas.values()) {
            if (a.getID() == id) return a;
        }
        return null;
    }

    /**
     * Gets the game a player is in or null if he is not in a game
     *
     * @param p the player we search for
     * @return
     */
    public Game getArena(Player p) {
        for (Game a : arenas.values()) {
            if (a.containsPlayer(p)) return a;
        }
        return null;
    }

    /**
     * Returns a modifyable HashMap containing all Games
     *
     * @return
     */
    public HashMap<String, Game> getArenas() {
        return arenas;
    }

}
