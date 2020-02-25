package com.redthirddivision.bukkitgamelib.plugin;

import com.redthirddivision.bukkitgamelib.Main;
import com.redthirddivision.bukkitgamelib.Minigame;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class MinigameManager {

    private static final Map<String, Minigame> games = new HashMap<>();
    private static final PluginManager manager = Bukkit.getPluginManager();

    public static void loadMinigames() {
        File folder = new File(Main.getInstance().getDataFolder(), "minigames");
        folder.mkdirs();

        for (File f : folder.listFiles()) {
            if (!f.getName().endsWith(".jar") || !f.isFile()) continue;
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

    public static void disable() {
        for (String s : games.keySet()) {
            Minigame game = games.get(s);
            manager.disablePlugin(game);
        }
        games.clear();
    }

}
