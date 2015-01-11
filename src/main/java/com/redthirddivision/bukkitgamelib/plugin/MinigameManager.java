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
package com.redthirddivision.bukkitgamelib.plugin;

import com.redthirddivision.bukkitgamelib.Main;
import com.redthirddivision.bukkitgamelib.Minigame;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * <strong>Project:</strong> BukkitGameLib <br>
 * <strong>File:</strong> MinigameManager.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class MinigameManager {

    private static final Map<String, Minigame> games = new HashMap<>();
    private static final PluginManager manager = Bukkit.getPluginManager();

    public static void loadMinigames() {
        File folder = new File(Main.getInstance().getDataFolder(), "minigames");
        folder.mkdirs();

        for (File f : folder.listFiles()) {
            if (!f.getName().endsWith(".jar") || !f.isFile()) {
                try {
                    Plugin p = manager.loadPlugin(f);
                    if (p instanceof Minigame) {
                        manager.enablePlugin(p);
                        games.put(p.getDescription().getName(), (Minigame) p);
                    } else {
                        Main.getInstance().getLogger().severe("The plugin is not a Minigame!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void disable() {
        for (String s : games.keySet()) {
            Minigame game = games.get(s);
            manager.disablePlugin(game);
        }
        games.clear();
    }

}
