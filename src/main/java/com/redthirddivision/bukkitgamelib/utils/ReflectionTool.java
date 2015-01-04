package com.redthirddivision.bukkitgamelib.utils;

import org.bukkit.Bukkit;

/**
 * @author TheJeterLP
 */
public class ReflectionTool {

    /**
     * Needed to get the package of craftbukkit
     * @return 
     */
    public static String getCraftBukkitPackage() {
        return "org.bukkit.craftbukkit." + getMinecraftVersionNumber();
    }
    
   
    /**
     * Needed to use minecraft classes
     * @return 
     */
    public static String getMinecraftPackage() {
        return "net.minecraft.server." + getMinecraftVersionNumber();
    }

     /**
     * Needed to get the packages because they change after a new minecraft version.
     * @return 
     */
    public static String getMinecraftVersionNumber() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

}
