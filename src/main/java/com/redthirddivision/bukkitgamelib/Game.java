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

import com.redthirddivision.bukkitgamelib.arena.PlayerData;
import com.redthirddivision.bukkitgamelib.utils.Utils.MessageType;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> Game.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public abstract class Game {

    private final int id, minplayers, maxplayers;
    private final ArrayList<PlayerData> alive = new ArrayList<>(), spectator = new ArrayList<>();
    private final Location min, max;
    private final String name, joinPermission;
    private final Minigame owner;
    private final Sign sign;
    private PlayerData winner = null;

    private ArenaState state;
    private int taskid;

    /**
     * Creates a new Game instances and registers it in the
     * {@link com.redthirddivision.bukkitgamelib.arena.GameManager}
     *
     * @param id the id of the arena, only used internal
     * @param name the name of teh arena
     * @param owner the {@link com.redthirddivision.bukkitgamelib.Minigame}
     * which owns this game
     * @param state the actual arenastate (should be stored in some kind of
     * database)
     * @param selection the worldedit selection. Use {@link com.redthirddivision.bukkitgamelib.utils.SelectionManager#getSelection(org.bukkit.entity.Player)
     * }
     * @param minplayers the min number of palyers needed to start the game
     * @param maxplayers the max number of players allowed
     * @param sign the join sign
     * @param joinPermission the permission needed to use the join sign
     */
    public Game(int id, String name, Minigame owner, ArenaState state, Location[] selection, int minplayers, int maxplayers, Location sign, String joinPermission) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.min = selection[0];
        this.max = selection[1];
        this.minplayers = minplayers;
        this.maxplayers = maxplayers;

        BlockState signState = sign.getBlock().getState();
        if (!(signState instanceof Sign)) {
            signState.setType(Material.OAK_WALL_SIGN);
            signState.update(true);
            signState = sign.getBlock().getState();
        }

        this.sign = (Sign) signState;
        this.joinPermission = joinPermission;
        updateStatusAndSign(state);
        owner.getGameManager().load(this);
    }

    /**
     * Called after a player joined the arena
     *
     * @param p the player who joined
     */
    public abstract void onPlayerAddToArena(Player p);

    /**
     * Called after a player left the arena
     *
     * @param p the player who left
     */
    public abstract void onPlayerRemoveFromArena(Player p);

    /**
     * Called after a player started spectating the arena
     *
     * @param p the player who is spectating
     */
    public abstract void onPlayerStartSpectating(Player p);

    /**
     * Called before the arena starts
     */
    public abstract void onArenaStart();

    /**
     * Called before the arena stops
     */
    public abstract void onArenaStop();

    /**
     * Called afer the status of the arena changed
     */
    public abstract void onStatusChange();

    /**
     * @return the id of the arena
     */
    public int getID() {
        return id;
    }

    /**
     * The players who are alive
     *
     * @return {@link java.util.ArrayList}
     */
    public ArrayList<PlayerData> getAlivePlayers() {
        return alive;
    }

    /**
     * The players who are spectating
     *
     * @return {@link java.util.ArrayList}
     */
    public ArrayList<PlayerData> getSpecators() {
        return spectator;
    }

    /**
     * The name of the Arena
     *
     * @return {@link java.lang.String}
     */
    public String getName() {
        return name;
    }

    /**
     * The min number of players
     *
     * @return {@link java.lang.Integer}
     */
    public int getMinPlayers() {
        return minplayers;
    }

    /**
     * Gets the number of alive players
     *
     * @return {@link java.lang.Integer}
     */
    public int getAlive() {
        return alive.size();
    }

    /**
     * Gets the number of spectating players
     *
     * @return {@link java.lang.Integer}
     */
    public int getSpectators() {
        return spectator.size();
    }

    /**
     * Gets the number of al players
     *
     * @return {@link java.lang.Integer}
     */
    public int getAllPlayers() {
        return alive.size() + spectator.size();
    }

    /**
     * Gets the state of the arena
     *
     * @return {@link com.redthirddivision.bukkitgamelib.Game.ArenaState}
     */
    public ArenaState getState() {
        return this.state;
    }

    /**
     * The max number of players
     *
     * @return {@link java.lang.Integer}
     */
    public int getMaxPlayers() {
        return maxplayers;
    }

    /**
     * Gets the plugin which is owning this Game
     *
     * @return {@link com.redthirddivision.bukkitgamelib.Minigame}
     */
    public Minigame getOwningPlugin() {
        return owner;
    }

    /**
     * Gets all PlayerData's in the game
     *
     * @return {@link java.util.ArrayList}
     */
    public ArrayList<PlayerData> getAllDatas() {
        ArrayList<PlayerData> ret = new ArrayList<>();
        ret.addAll(alive);
        ret.addAll(spectator);
        return ret;
    }

    /**
     * Used to send a colored message to a player
     *
     * @param sender the receiver of the message
     * @param type the type of the message
     * @param msg the actual message without color codes
     */
    public void sendMessage(CommandSender sender, MessageType type, String msg) {
        String pre = "";
        switch (type) {
            case INFO:
                pre = "§a[" + owner.getName() + "]§7 ";
                break;
            case ERROR:
                pre = "§4[" + owner.getName() + "]§7 ";
                break;
        }
        sender.sendMessage(pre + msg);
    }

    /**
     * Broadcasts a message to all the players in the game (Spectators included)
     *
     * @param type the type of the message
     * @param msg the actual message without color codes
     */
    public void broadcastMessage(MessageType type, String msg) {
        for (PlayerData pd : getAllDatas()) {
            sendMessage(pd.getPlayer(), type, msg);
        }
    }

    /**
     * Puts a Player in spectator mode
     *
     * @param p the player who will be spectatorx
     */
    public void setSpectator(final Player p) {
        if (getPlayer(p) == null) {
            return;
        }
        PlayerData pd = getPlayer(p);

        alive.remove(pd);
        spectator.add(pd);

        if (alive.isEmpty()) {
            updateStatusAndSign(ArenaState.WON);
            stop();
            return;
        } else if (alive.size() == 1) {
            setWinner(alive.get(0));
            updateStatusAndSign(ArenaState.WON);
            stop();
            return;
        }

        onPlayerStartSpectating(p);

        if (state == ArenaState.STARTED) {
            updateStatusAndSign(state);
            pd.startSpectating();
        }
    }

    /**
     * Adds a player to the game
     *
     * @param p the player to add
     */
    public void addPlayer(final Player p) {
        if (owner.getGameManager().getArena(p) != null) {
            sendMessage(p, MessageType.ERROR, "You can't be in more than one game at a time.");
            return;
        }

        if (!p.hasPermission(joinPermission)) {
            sendMessage(p, MessageType.ERROR, "You do not have the permission to join this game.");
            return;
        }

        if (state == ArenaState.DISABLED) {
            sendMessage(p, MessageType.ERROR, "The game is actually disabled.");
            return;
        }

        if (getAlive() >= maxplayers) {
            sendMessage(p, MessageType.ERROR, "The game is already full.");
            return;
        }

        if (state == ArenaState.STARTED || state == ArenaState.WON) {
            sendMessage(p, MessageType.ERROR, "The game has already started.");
            return;
        }

        if (getPlayer(p) != null) {
            return;
        }

        if (state == ArenaState.WAITING) {
            start();
        }

        alive.add(new PlayerData(p, this));
        broadcastMessage(MessageType.INFO, p.getDisplayName() + " has joined the game.");
        updateStatusAndSign(state);
        onPlayerAddToArena(p);
    }

    /**
     * Removes a player from the game
     *
     * @param pl the player to remove
     */
    public void removePlayer(final PlayerData pl) {
        pl.restorePlayerData();

        if (alive.contains(pl)) {
            alive.remove(pl);
        }

        if (spectator.contains(pl)) {
            spectator.remove(pl);
        }

        for (PlayerData pd : getAllDatas()) {
            pd.getPlayer().showPlayer(Main.getInstance(), pl.getPlayer());
            pl.getPlayer().showPlayer(Main.getInstance(), pd.getPlayer());
            if (state != ArenaState.WON) {
                sendMessage(pd.getPlayer(), MessageType.INFO, pl.getPlayer().getDisplayName() + " has left the game.");
            }
        }

        onPlayerRemoveFromArena(pl.getPlayer());

        if (state != ArenaState.WON) {
            if (alive.isEmpty()) {
                updateStatusAndSign(ArenaState.WON);
                stop();
            } else if (alive.size() == 1) {
                updateStatusAndSign(ArenaState.WON);
                stop();
            }
            updateStatusAndSign(state);
        }
    }

    /**
     * Removes a player from the game
     *
     * @param p the player to remove
     */
    public void removePlayer(Player p) {
        PlayerData pl = getPlayer(p);
        removePlayer(pl);
    }

    /**
     * Checks if the Player is playing in this game
     *
     * @param p the player to check for
     * @return {@link java.lang.Boolean}
     */
    public boolean containsPlayer(Player p) {
        return getPlayer(p) != null;
    }

    /**
     * Checks if the game is started
     *
     * @return {@link java.lang.Boolean}
     */
    public boolean isStarted() {
        return state != ArenaState.DISABLED && state != ArenaState.WAITING;
    }

    /**
     * Checks if the game is disabled
     *
     * @return {@link java.lang.Boolean}
     */
    public boolean isDisabled() {
        return state == ArenaState.DISABLED;
    }

    /**
     * Starts the countdown for the game to start
     */
    public void start() {
        updateStatusAndSign(ArenaState.COUNTDING_DOWN);

        taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(owner, new Runnable() {
            private int countdown = 30;

            @Override
            public void run() {
                if (countdown > 0) {
                    for (PlayerData pd : alive) {
                        pd.getPlayer().playNote(pd.getPlayer().getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G));
                        pd.getPlayer().setExp(0);
                        pd.getPlayer().setLevel(countdown);
                    }

                    if (state == ArenaState.COUNTDING_DOWN) {
                        sign.setLine(2, state.getText() + "(" + countdown + ")");
                        sign.update(true);
                    }
                }

                if (countdown == 30 || countdown == 15 || (countdown <= 10 && countdown > 0)) {
                    broadcastMessage(MessageType.INFO, "The game starts in " + countdown + " seconds.");
                } else if (countdown == 0) {
                    if (alive.size() < minplayers) {
                        broadcastMessage(MessageType.ERROR, "Not enough players joined the game.");
                        Bukkit.getServer().getScheduler().cancelTask(taskid);
                        stop();
                        return;
                    }

                    //ARENA STARTED
                    onArenaStart();

                    for (PlayerData pd : alive) {
                        pd.getPlayer().setLevel(0);
                        pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 1);
                    }

                    updateStatusAndSign(ArenaState.STARTED);
                    broadcastMessage(MessageType.INFO, "The game has started!");
                    Bukkit.getServer().getScheduler().cancelTask(taskid);
                }

                countdown--;
            }
        }, 0, 20);
    }

    /**
     * Stops the game
     */
    public void stop() {
        onArenaStop();
        updateStatusAndSign(ArenaState.WON);
        for (PlayerData pd : getAllDatas()) {
            for (PlayerData other : getAllDatas()) {
                pd.getPlayer().showPlayer(Main.getInstance(), other.getPlayer());
                other.getPlayer().showPlayer(Main.getInstance(), pd.getPlayer());
            }
        }

        String winnerName = this.winner == null ? "noone" : this.winner.getPlayer().getName();
        broadcastMessage(MessageType.INFO, "Game is over, " + winnerName + " won!");

        Bukkit.getScheduler().cancelTask(this.taskid);

        for (PlayerData data : getAllDatas()) {
            removePlayer(data);
        }

        alive.clear();
        spectator.clear();
        updateStatusAndSign(ArenaState.WAITING);
    }

    /**
     * Transforms a Player to a PlayerData
     *
     * @param p the Player to transform
     * @return {@link com.redthirddivision.bukkitgamelib.arena.PlayerData}
     */
    public PlayerData getPlayer(Player p) {
        for (PlayerData pd : getAllDatas()) {
            if (pd.isForPlayer(p)) {
                return pd;
            }
        }
        return null;
    }

    /**
     * Changes the state of the game to the given one
     *
     * @param state The state to set the game state to
     */
    public void updateStatusAndSign(ArenaState state) {
        this.state = state;
        onStatusChange();

        String line1 = id + " - " + name;
        if (line1.length() > 16) {
            line1 = line1.substring(0, 16);
        }

        this.sign.setLine(0, "§6[" + owner.getGameManager().getName() + "]");
        this.sign.setLine(1, line1);
        this.sign.setLine(2, state.getText());
        this.sign.setLine(3, "§a" + alive.size() + "§r/§c" + spectator.size() + "§r/§7" + maxplayers);
        this.sign.update(true);
    }

    /**
     * Returns an {@link com.redthirddivision.minigames.utils.Cuboid}
     * representing the selection of the arena.
     *
     * @return {@link com.redthirddivision.minigames.utils.Cuboid}
     */
    public CuboidRegion getArena() {
        return new CuboidRegion(BukkitAdapter.adapt(min.getWorld()), BukkitAdapter.asBlockVector(min), BukkitAdapter.asBlockVector(max));
    }

    /**
     * Used to check if a {@link org.bukkit.Location} is contained in the arena
     *
     * @param v the location to check for
     * @return {@link java.lang.Boolean}
     */
    public boolean containsBlock(Location v) {
        return getArena().contains(BukkitAdapter.asBlockVector(v));
    }

    public void setWinner(PlayerData winner) {
        this.winner = winner;
    }

    public PlayerData getWinner() {
        return this.winner;
    }

    public enum ArenaState {

        WAITING(ChatColor.GREEN + "Lobby"),
        COUNTDING_DOWN(ChatColor.GREEN + "Countdown"),
        STARTED(ChatColor.AQUA + "Ingame"),
        WON(ChatColor.AQUA + "Winner!"),
        DISABLED(ChatColor.RED + "Disabled");

        private final String text;

        private ArenaState(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
