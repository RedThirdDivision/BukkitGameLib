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
import com.redthirddivision.bukkitgamelib.utils.Utils.MessageType;
import com.redthirddivision.bukkitgamelib.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> PlayerData.java
 *
 * @author <a href="http://jeter.vc-network.com">TheJeterLP</a>
 */
public class PlayerData {

    private final Player player;
    private final Game a;
    private final ItemStack[] inventory, armor;
    private final int level, foodlevel;
    private final float xp;
    private final GameMode gm;
    private final double maxhealth, health;
    private final boolean flying;
    private boolean spectator = false;

    /**
     * Stores all stuff like inventory, xp, health, maxhealth... in the RAM and completely resets a Player
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
        maxhealth = ((Damageable) p).getMaxHealth();
        health = ((Damageable) p).getHealth();
        flying = p.getAllowFlight();

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setSleepingIgnored(true);
        player.setMaxHealth(20.0);
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
        player.setMaxHealth(maxhealth);
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
     * @param p the Player we want to look for
     * @return 
     */
    public boolean isForPlayer(Player p) {
        return player.getUniqueId().equals(p.getUniqueId());
    }

    /**
     * Returns the Player instance stored in this object
     * @return 
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Checks if the Player is spectating
     * @return 
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
            alive.getPlayer().hidePlayer(player);
        }

        Utils.clearInventory(player);

        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName("§6Teleport");
        compass.setItemMeta(meta);

        ItemStack ball = new ItemStack(Material.SLIME_BALL);
        ItemMeta ballmeta = ball.getItemMeta();
        ballmeta.setDisplayName("§5Quit");
        ball.setItemMeta(ballmeta);

        player.getInventory().addItem(compass, ball);
        player.setAllowFlight(true);

        a.onPlayerStartSpectating(player);

        a.sendMessage(player, MessageType.INFO, "You are now spectating the game.");
    }
}
