package com.redthirddivision.bukkitgamelib.listeners;

import com.redthirddivision.bukkitgamelib.Game;
import com.redthirddivision.bukkitgamelib.Minigame;
import com.redthirddivision.bukkitgamelib.utils.Utils;
import com.redthirddivision.bukkitgamelib.utils.Utils.MessageType;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BukkitSignEvent implements Listener {

    private final Minigame owner;

    public BukkitSignEvent(final Minigame owner) {
        this.owner = owner;
    }

    @EventHandler
    public void onLobbySign(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock().getState() instanceof Sign) {
            Sign s = (Sign) event.getClickedBlock().getState();
            if (s.getLine(0).equalsIgnoreCase("§6[" + owner.getGameManager().getName() + "]")) {
                int id = Integer.valueOf(s.getLine(1).split(" - ")[0]);
                Game a = owner.getGameManager().getArena(id);

                if (a == null) {
                    Utils.sendMessage(event.getPlayer(), MessageType.ERROR, "That game is not exisiting.", owner);
                    return;
                }

                a.addPlayer(event.getPlayer());
            }
        }
    }
}
