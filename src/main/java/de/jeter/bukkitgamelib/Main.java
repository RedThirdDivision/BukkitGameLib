package de.jeter.bukkitgamelib;

import de.jeter.bukkitgamelib.plugin.MinigameManager;
import de.jeter.bukkitgamelib.utils.LocaleManager;
import de.jeter.bukkitgamelib.utils.Locales;
import de.jeter.bukkitgamelib.utils.SelectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main INSTANCE;

    @Override
    public void onEnable() {
        long currentTimeMillisStart = System.currentTimeMillis();
        INSTANCE = this;
        getLogger().info("Minigame Library has been started!");

        if (SelectionManager.getWorldEdit() == null) {
            getLogger().info("WorldEdit was not found!");
            disablePlugin();
            return;
        }

        Locales.load();
        
        LocaleManager.load();
        
        MinigameManager.loadMinigames();

        long timeTook = System.currentTimeMillis() - currentTimeMillisStart;
        getLogger().info("Minigame library has been started. It took " + timeTook + " miliseconds.");
    }

    @Override
    public void onDisable() {
        long currentTimeMillisStart = System.currentTimeMillis();
        getLogger().info("Minigame Library is shutting down!");

        MinigameManager.disable();

        INSTANCE = null;

        long timeTook = System.currentTimeMillis() - currentTimeMillisStart;
        getLogger().info("Minigame Library has been shut down. It took " + timeTook + " miliseconds.");
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public void disablePlugin() {
        getServer().getPluginManager().disablePlugin(this);
        setEnabled(false);
    }

}
