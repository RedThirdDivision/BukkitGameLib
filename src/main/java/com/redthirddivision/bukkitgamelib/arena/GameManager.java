package com.redthirddivision.bukkitgamelib.arena;

import com.redthirddivision.bukkitgamelib.Game;
import com.redthirddivision.bukkitgamelib.Minigame;
import java.util.HashMap;
import org.bukkit.entity.Player;

public class GameManager {

    private final HashMap<String, Game> arenas = new HashMap<>();
    private final Minigame pl;

    public GameManager(Minigame plugin) {
        this.pl = plugin;
    }

    /**
     * Gets the Minigame which belongs to this GameManager instance
     *
     * @return {@link com.redthirddivision.bukkitgamelib.Minigame}
     */
    public Minigame getPlugin() {
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
     * @return true if a game by that name is existing
     */
    public boolean isExisting(String name) {
        return arenas.containsKey(name);
    }

    /**
     * Checks if a Game is existing by Game instance
     *
     * @param arena the game we look for
     * @return true if that game is existing
     */
    public boolean isExisting(Game arena) {
        return arenas.containsValue(arena);
    }

    /**
     * Gets the Game instance of an arena name or null if its not existing
     *
     * @param name The name of a game
     * @return the game this name belongs to
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
     * @return the game this id belongs to
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
     * @return the game this player is in or null
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
     * @return a map containg all arenas
     */
    public HashMap<String, Game> getArenas() {
        return arenas;
    }

}
