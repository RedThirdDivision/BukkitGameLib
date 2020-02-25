package com.redthirddivision.bukkitgamelib.arena;

import com.redthirddivision.bukkitgamelib.Game;
import com.redthirddivision.bukkitgamelib.Main;
import com.redthirddivision.bukkitgamelib.utils.Utils.MessageType;
import com.redthirddivision.bukkitgamelib.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerData {

    private final Player player;
    private final Game a;
    private final ItemStack[] inventory, armor;
    private final int level, foodlevel;
    private final float xp;
    private final GameMode gm;
    private final double health;
    private final boolean flying;
    private boolean spectator = false;

    /**
     * Stores all stuff like inventory, xp, health, maxhealth... in the RAM and completely resets a Player
     *
     * @param p the player to reset
     * @param a the game the player is in
     */
    public PlayerData(Player p, Game a) {
        this.player = p;
        this.a = a;

        inventory = p.getInventory().getContents();
        armor = p.getInventory().getArmorContents();
        gm = player.getGameMode();
        xp = p.getExp();
        level = p.getLevel();
        foodlevel = p.getFoodLevel();
        health = p.getHealth();
        flying = p.getAllowFlight();

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setSleepingIgnored(true);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setExp(0F);
        player.setLevel(0);
        Utils.clearInventory(player);
        player.updateInventory();
    }

    /**
     * Gives the Player contained in this instance all his stuff back
     */
    public void restorePlayerData() {
        if (player == null) return;
        player.setAllowFlight(flying);
        player.setFoodLevel(foodlevel);
        player.setHealth(health);
        player.setGameMode(gm);
        player.getInventory().setArmorContents(armor);
        player.getInventory().setContents(inventory);
        player.setExp(xp);
        player.setLevel(level);
        player.setSleepingIgnored(false);
    }

    /**
     * Checks if this instance is used for a Player
     *
     * @param p the Player we want to look for
     * @return true if this instance belongs to the player
     */
    public boolean isForPlayer(Player p) {
        return player.getUniqueId().equals(p.getUniqueId());
    }

    /**
     * Returns the Player instance stored in this object
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Checks if the Player is spectating
     *
     * @return true if the player is in spectator mode
     */
    public boolean isSpectator() {
        return spectator;
    }

    /**
     * Put the player in spectating mode.
     * WARNING: You should use {@link com.redthirddivision.bukkitgamelib.Game#setSpectator(org.bukkit.entity.Player)} for this!
     */
    public void startSpectating() {
        spectator = true;

        for (PlayerData alive : a.getAlivePlayers()) {
            alive.getPlayer().hidePlayer(Main.getInstance(), player);
        }

        a.getOwningPlugin().getServer().getScheduler().scheduleSyncDelayedTask(a.getOwningPlugin(), new Runnable() {

            @Override
            public void run() {
                Utils.setSpectatorInventory(player);
                player.setAllowFlight(true);
            }
        }, 5);

        a.sendMessage(player, MessageType.INFO, "You are now spectating the game.");
    }
}
