package com.redthirddivision.bukkitgamelib.listeners;

import com.redthirddivision.bukkitgamelib.Game;
import com.redthirddivision.bukkitgamelib.Minigame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitQuitEvent implements Listener {

    private final Minigame owner;

    public BukkitQuitEvent(final Minigame owner) {
        this.owner = owner;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (owner.getGameManager().getArena(e.getPlayer()) == null) return;
        Game a = owner.getGameManager().getArena(e.getPlayer());
        a.removePlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        if (owner.getGameManager().getArena(e.getPlayer()) == null) return;
        Game a = owner.getGameManager().getArena(e.getPlayer());
        a.removePlayer(e.getPlayer());
    }

}
