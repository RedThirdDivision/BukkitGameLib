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
package com.redthirddivision.bukkitgamelib.utils;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> SelectionManager.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class SelectionManager {

    /**
     * Gets the Selection a Player made with WorldEdit. Index: 0 = min, 1 = max
     *
     * @param p the player who made the selection
     * @return an Array containg the Selection
     */
    public static Location[] getSelection(Player p) {
        try {
            LocalSession session = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(p));
            Region region = session.getSelection(BukkitAdapter.adapt(p.getWorld()));

            if (region == null) {
                return null;
            }

            Location max = BukkitAdapter.adapt(p.getWorld(), region.getMaximumPoint());
            Location min = BukkitAdapter.adapt(p.getWorld(), region.getMinimumPoint());
            return new Location[]{min, max};
        } catch (IncompleteRegionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Get the WorldEdit instance
     *
     * @return the instance of WorldEdit
     */
    public static WorldEditPlugin getWorldEdit() {
        Plugin worldEdit = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit instanceof WorldEditPlugin && worldEdit.isEnabled()) {
            return (WorldEditPlugin) worldEdit;
        } else {
            return null;
        }
    }

}
