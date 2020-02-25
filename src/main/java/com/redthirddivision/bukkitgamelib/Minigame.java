package com.redthirddivision.bukkitgamelib;

import com.redthirddivision.bukkitgamelib.arena.GameManager;
import com.redthirddivision.bukkitgamelib.command.CommandManager;
import com.redthirddivision.bukkitgamelib.listeners.BukkitInventoryEvent;
import com.redthirddivision.bukkitgamelib.listeners.BukkitMoveEvent;
import com.redthirddivision.bukkitgamelib.listeners.BukkitQuitEvent;
import com.redthirddivision.bukkitgamelib.listeners.BukkitSignEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Minigame extends JavaPlugin {
    
    private GameManager manager;
    private CommandManager cmdManager;

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
        cmdManager = new CommandManager(this);
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

    /**
     * The CommandManager of the plugin
     *
     * @return {@link com.redthirddivision.bukkitgamelib.command.CommandManager}
     */
    public CommandManager getCommandManager() {
        return cmdManager;
    }

    /**
     * The PluginDescriptionFile of the core
     *
     * @return {@link org.bukkit.plugin.PluginDescriptionFile}
     */
    public PluginDescriptionFile getCorePluginYML() {
        return Main.getInstance().getDescription();
    }
    
}
