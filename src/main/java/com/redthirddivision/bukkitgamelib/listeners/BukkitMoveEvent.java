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
import com.redthirddivision.bukkitgamelib.arena.GameManager;
import com.redthirddivision.bukkitgamelib.utils.Utils.MessageType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> BukkitMoveEvent.java
 * 
 * @author: <a href="http://jeter.vc-network.com">TheJeterLP</a>
 */
public class BukkitMoveEvent implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerWalkOutOfArena(PlayerMoveEvent e) {
        Game a = GameManager.getInstance().getArena(e.getPlayer());
        if (a == null || e.getPlayer().isOp()) return;
        if (!a.containsBlock(e.getTo())) {
            e.setCancelled(true);
            e.getPlayer().teleport(e.getFrom());
            a.sendMessage(e.getPlayer(), MessageType.ERROR, "You are not allowed to walk out of the arena!");
        }

    }

}
