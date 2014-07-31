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

import com.redthirddivision.bukkitgamelib.arena.GameManager;
import com.redthirddivision.bukkitgamelib.arena.PlayerData;
import com.redthirddivision.bukkitgamelib.utils.Utils.MessageType;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public abstract class Game {

    private final int id, minplayers, maxplayers;
    private final ArrayList<PlayerData> alive = new ArrayList<>(), spectator = new ArrayList<>();
    private final Location min, max;
    private final String name, joinPermission;
    private final GamePlugin owner;
    private final Sign sign;

    private ArenaState state;
    private int taskid;

    public Game(int id, String name, GamePlugin owner, ArenaState state, Location min, Location max, int minplayers, int maxplayers, Sign sign, String joinPermission) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.min = min;
        this.max = max;
        this.minplayers = minplayers;
        this.maxplayers = maxplayers;
        this.sign = sign;
        this.joinPermission = joinPermission;
        updateStatusAndSign(state);
        GameManager.getInstance().load(this);
    }

    public abstract void onPlayerAddToArena(Player p);

    public abstract void onPlayerRemoveFromArena(Player p);

    public abstract void onPlayerStartSpectating(Player p);

    public abstract void onArenaStart();

    public abstract void onArenaStop();

    public abstract boolean onStatusChange();

    public int getID() {
        return id;
    }

    public ArrayList<PlayerData> getAlivePlayers() {
        return alive;
    }

    public ArrayList<PlayerData> getSpecators() {
        return spectator;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayers() {
        return minplayers;
    }

    public int getAlive() {
        return alive.size();
    }

    public int getSpectators() {
        return spectator.size();
    }

    public int getAllPlayers() {
        return alive.size() + spectator.size();
    }

    public ArenaState getState() {
        return this.state;
    }

    public int getMaxPlayers() {
        return maxplayers;
    }

    public GamePlugin getOwningPlugin() {
        return owner;
    }

    public ArrayList<PlayerData> getAllDatas() {
        ArrayList<PlayerData> ret = new ArrayList<>();
        ret.addAll(alive);
        ret.addAll(spectator);
        return ret;
    }

    public void sendMessage(Player p, MessageType type, String msg) {
        String pre = "";
        switch (type) {
            case INFO:
                pre = "§a" + owner.getName() + "§7 ";
                break;
            case ERROR:
                pre = "§4" + owner.getName() + "§7 ";
                break;
        }
        p.sendMessage(pre + msg);
    }

    public void broadcastMessage(MessageType type, String msg) {
        for (PlayerData pd : getAllDatas()) {
            sendMessage(pd.getPlayer(), type, msg);
        }
    }

    public void setSpectator(final Player p) {
        p.getServer().getScheduler().scheduleSyncDelayedTask(owner, new Runnable() {

            @Override
            public void run() {
                if (getPlayer(p) == null) return;
                PlayerData pd = getPlayer(p);
                alive.remove(pd);
                spectator.add(pd);
                updateStatusAndSign(state);

                if (alive.isEmpty()) {
                    updateStatusAndSign(ArenaState.WON);
                    stop(null);
                    return;
                } else if (alive.size() == 1) {
                    updateStatusAndSign(ArenaState.WON);
                    stop(alive.get(0).getPlayer());
                    return;
                }

                pd.startSpectating();
            }
        }, 2);

    }

    public void addPlayer(final Player p) {
        if (GameManager.getInstance().getArena(p) != null) {
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

        if (getPlayer(p) != null) return;

        if (state == ArenaState.WAITING) start();

        alive.add(new PlayerData(p, this));
        onPlayerAddToArena(p);
        broadcastMessage(MessageType.INFO, p.getDisplayName() + " has joined the game.");
        updateStatusAndSign(state);
    }

    public void removePlayer(final PlayerData pl) {
        pl.restorePlayerData();

        if (alive.contains(pl)) {
            alive.remove(pl);
        }

        if (spectator.contains(pl)) {
            spectator.remove(pl);
        }

        for (PlayerData pd : getAllDatas()) {
            pd.getPlayer().showPlayer(pl.getPlayer());
            pl.getPlayer().showPlayer(pd.getPlayer());
            if (state != ArenaState.WON) {
                sendMessage(pd.getPlayer(), MessageType.INFO, pl.getPlayer().getDisplayName() + " has left the game.");
            }
        }

        onPlayerRemoveFromArena(pl.getPlayer());

        if (state != ArenaState.WON) {
            if (alive.isEmpty()) {
                updateStatusAndSign(ArenaState.WON);
                stop(null);
            } else if (alive.size() == 1) {
                updateStatusAndSign(ArenaState.WON);
                stop(alive.get(0).getPlayer());
            }
            updateStatusAndSign(state);
        }
    }

    public void removePlayer(Player p) {
        PlayerData pl = getPlayer(p);
        removePlayer(pl);
    }

    public boolean containsPlayer(Player p) {
        return getPlayer(p) != null;
    }

    public boolean isStarted() {
        return state != ArenaState.DISABLED && state != ArenaState.WAITING;
    }

    public boolean isDisabled() {
        return state == ArenaState.DISABLED;
    }

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
                        stop(null);
                        return;
                    }

                    //ARENA STARTED
                    onArenaStart();

                    for (PlayerData pd : alive) {
                        pd.getPlayer().setLevel(0);
                        pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.LEVEL_UP, 3, 1);
                    }

                    updateStatusAndSign(ArenaState.STARTED);
                    broadcastMessage(MessageType.INFO, "The game has started!");
                    Bukkit.getServer().getScheduler().cancelTask(taskid);
                }

                countdown--;
            }
        }, 0, 20);
    }

    public void stop(final Player winner) {
        onArenaStop();
        updateStatusAndSign(ArenaState.WON);
        if (winner != null) {
            sendMessage(winner, MessageType.INFO, "Congratulations you won the game!");
        }
        for (PlayerData pd : getAllDatas()) {
            if (winner != null) {
                sendMessage(pd.getPlayer(), MessageType.INFO, winner.getDisplayName() + " won the game!");
            } else {
                sendMessage(pd.getPlayer(), MessageType.INFO, "Nobody won the game :(");
            }

            for (PlayerData other : getAllDatas()) {
                pd.getPlayer().showPlayer(other.getPlayer());
                other.getPlayer().showPlayer(pd.getPlayer());
            }
        }
        Bukkit.getScheduler().cancelTask(this.taskid);

        for (PlayerData data : getAllDatas()) {
            removePlayer(data);
        }

        alive.clear();
        spectator.clear();
        updateStatusAndSign(ArenaState.WAITING);
    }

    public PlayerData getPlayer(Player p) {
        for (PlayerData pd : getAllDatas()) {
            if (pd.isForPlayer(p)) return pd;
        }
        return null;
    }

    public void updateStatusAndSign(ArenaState state) {
        this.state = state;
        onStatusChange();
        this.sign.setLine(0, "§6[" + owner.getName() + "]");
        this.sign.setLine(1, name);
        this.sign.setLine(2, state.getText());
        this.sign.setLine(3, "§a" + alive.size() + "§r/§c" + spectator.size() + "§r/§7" + maxplayers);
        this.sign.update(true);
    }

    public void setWinner(Player p) {
        stop(p);
    }

    public boolean containsBlock(Location v) {
        if (v.getWorld() != min.getWorld()) return false;
        final double x = v.getX();
        final double y = v.getY();
        final double z = v.getZ();
        return (x >= min.getBlockX() && x < max.getBlockX() + 1) && (y >= min.getBlockY() && y < max.getBlockY() + 1) && (z >= min.getBlockZ() && z < max.getBlockZ() + 1);
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
