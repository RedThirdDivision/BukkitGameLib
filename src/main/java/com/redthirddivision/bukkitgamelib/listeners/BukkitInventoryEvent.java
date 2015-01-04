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
package com.redthirddivision.bukkitgamelib.listeners;

import com.redthirddivision.bukkitgamelib.Game;
import com.redthirddivision.bukkitgamelib.GamePlugin;
import com.redthirddivision.bukkitgamelib.arena.GameManager;
import com.redthirddivision.bukkitgamelib.arena.PlayerData;
import com.redthirddivision.bukkitgamelib.utils.Utils.MessageType;
import com.redthirddivision.bukkitgamelib.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> BukkitInventoryEvent.java
 * 
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class BukkitInventoryEvent implements Listener {

    private final GamePlugin owner;
    
    public BukkitInventoryEvent(final GamePlugin owner) {
        this.owner = owner;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onItemClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        if (owner.getGameManager().getArena(p) == null) return;

        Game a = owner.getGameManager().getArena(p);
        PlayerData pd = a.getPlayer(p);
        if (!pd.isSpectator()) return;
        if (!e.getInventory().getTitle().startsWith("Alive: ")) return;

        ItemStack skull = e.getCurrentItem();

        if (skull.getType() != Material.SKULL_ITEM) return;

        String target = skull.getItemMeta().getDisplayName();
        Player pt = Bukkit.getPlayer(target);

        if (pt == null) return;
        if (!a.containsPlayer(pt)) return;

        p.teleport(pt);

        a.sendMessage(p, MessageType.INFO, "You have been teleported to " + target);
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInteract(final PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Game a = owner.getGameManager().getArena(e.getPlayer());

            if (a == null) return;

            if (e.getItem() == null) return;

            if (e.getItem().getType() == Material.COMPASS) {
                if (!e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Teleport")) return;
                if (!a.getPlayer(e.getPlayer()).isSpectator()) return;
                e.getPlayer().openInventory(Utils.getSpectatorInventory(a));
                e.setCancelled(true);
            } else if (e.getItem().getType() == Material.SLIME_BALL) {
                if (!e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§5Quit")) return;
                if (!a.getPlayer(e.getPlayer()).isSpectator()) return;
                a.removePlayer(e.getPlayer());
                e.setCancelled(true);
            }
        }
    }

}
