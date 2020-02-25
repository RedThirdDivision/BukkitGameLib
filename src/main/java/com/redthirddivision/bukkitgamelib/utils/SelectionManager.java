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
