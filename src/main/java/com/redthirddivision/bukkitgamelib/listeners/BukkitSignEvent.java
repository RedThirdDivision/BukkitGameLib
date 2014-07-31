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
import com.redthirddivision.bukkitgamelib.utils.Utils;
import com.redthirddivision.bukkitgamelib.utils.Utils.MessageType;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BukkitSignEvent implements Listener {

    @EventHandler
    public void onLobbySign(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock().getState() instanceof Sign) {
            Sign s = (Sign) event.getClickedBlock().getState();
            if (s.getLine(0).equalsIgnoreCase("§6[" + GameManager.getInstance().getPlugin().getName() + "]")) {
                String name = s.getLine(1);
                Game a = GameManager.getInstance().getArena(name);

                if (a == null) {
                    Utils.sendMessage(event.getPlayer(), MessageType.ERROR, "That game is not exisiting.");
                    return;
                }

                a.addPlayer(event.getPlayer());
            }
        }
    }
}
