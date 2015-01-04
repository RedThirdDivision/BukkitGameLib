/*
 * Copyright 2014 TheJeterLP.
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
package com.redthirddivision.bukkitgamelib.title;

import com.redthirddivision.bukkitgamelib.utils.ReflectionTool;
import com.redthirddivision.bukkitgamelib.utils.Utils;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;

/**
 * <strong>Project:</strong> TropicTools <br>
 * <strong>File:</strong> ActionbarTitleObject.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class ActionbarTitleObject {
    
    private String rawTitle;
    private Object title;
    
    public ActionbarTitleObject(String title) {
        setTitle(title);
    }
    
    public void send(Player p) {
        try {
            if (!Utils.isPlayerRightVersion(p)) return;
            
            Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            
            Class<?> packetClass = Class.forName(ReflectionTool.getMinecraftPackage() + ".PacketPlayOutChat");
            Class<?> chatbasecomponent = Class.forName(ReflectionTool.getMinecraftPackage() + ".IChatBaseComponent");
            
            Object packet = packetClass.getConstructor(chatbasecomponent, int.class).newInstance(title, 2);
            
            Class<?> packetClazz = Class.forName(ReflectionTool.getMinecraftPackage() + ".Packet");
            
            Method sendPacket = con.getClass().getMethod("sendPacket", packetClazz);
            sendPacket.setAccessible(true);
            sendPacket.invoke(con, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void setTitle(String title) {
        try {
            this.rawTitle = title;
            
            Class<?> chatserializer = Class.forName(ReflectionTool.getMinecraftPackage() + ".ChatSerializer");
            
            Method m = chatserializer.getMethod("a", String.class);
            m.setAccessible(true);
            Object ob = m.invoke(null, TextConverter.convert(title));
            
            this.title = ob;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public String getTitle() {
        return rawTitle;
    }
}
