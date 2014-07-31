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

    public static GameManager getInstance() {
        return INSTANCE;
    }

    public GameManager(GamePlugin plugin) {
        INSTANCE = this;
        this.pl = plugin;
    }

    public GamePlugin getPlugin() {
        return pl;
    }

    public boolean isExisting(String name) {
        return arenas.containsKey(name);
    }

    public boolean isExisting(Game arena) {
        return arenas.containsValue(arena);
    }

    public Game getArena(String name) {
        return arenas.get(name);
    }

    public void load(Game a) {
        if (arenas.containsValue(a)) return;
        arenas.put(a.getName(), a);
    }

    public Game getArena(int id) {
        for (Game a : arenas.values()) {
            if (a.getID() == id) return a;
        }
        return null;
    }

    public Game getArena(Player p) {
        for (Game a : arenas.values()) {
            if (a.containsPlayer(p)) return a;
        }
        return null;
    }

    public HashMap<String, Game> getArenas() {
        return arenas;
    }

}
