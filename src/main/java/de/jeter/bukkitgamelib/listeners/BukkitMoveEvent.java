package de.jeter.bukkitgamelib.listeners;

import de.jeter.bukkitgamelib.Game;
import de.jeter.bukkitgamelib.Minigame;
import de.jeter.bukkitgamelib.arena.PlayerData;
import de.jeter.bukkitgamelib.utils.Utils.MessageType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BukkitMoveEvent implements Listener {
    
    private final Minigame owner;

    public BukkitMoveEvent(final Minigame owner) {
        this.owner = owner;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerWalkOutOfArena(PlayerMoveEvent e) {
        Game a = owner.getGameManager().getArena(e.getPlayer());
        if (a == null || e.getPlayer().isOp()) return;
        if (!a.containsBlock(e.getTo()) && a.isStarted()) {
            
            PlayerData pd = a.getPlayer(e.getPlayer());
            if (pd.isSpectator()) return;
            
            e.setCancelled(true);
            e.getPlayer().teleport(e.getFrom());
            a.sendMessage(e.getPlayer(), MessageType.ERROR, "You are not allowed to walk out of the arena!");
        }
    }

}
