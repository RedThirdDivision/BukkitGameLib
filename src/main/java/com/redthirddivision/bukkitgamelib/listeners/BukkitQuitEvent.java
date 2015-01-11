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
import com.redthirddivision.bukkitgamelib.Minigame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> BukkitQuitEvent.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
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
