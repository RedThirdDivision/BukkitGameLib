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
 * <strong>File:</strong> TitleObject.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class TitleObject {

    private String rawTitle;
    private String rawSubtitle;

    private Object title;
    private Object subtitle;

    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public TitleObject(String title, TitleType type, int fadeIn, int fadeOut, int stay) {
        if (type == TitleType.TITLE) {
            setTitle(title);
        } else if (type == TitleType.SUBTITLE) {
            setSubtitle(title);
        }

        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public TitleObject(String title, String subtitle, int fadeIn, int fadeOut, int stay) {
        rawTitle = title;
        rawSubtitle = subtitle;
        setTitle(title);
        setSubtitle(subtitle);

        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public void send(Player p) {
        try {
            if (!Utils.isPlayerRightVersion(p)) return;

            Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);

            Class<?> chatbasecomponent = Class.forName(ReflectionTool.getMinecraftPackage() + ".IChatBaseComponent");
            Class<?> packet = Class.forName(ReflectionTool.getMinecraftPackage() + ".Packet");

            Method sendPacket = con.getClass().getMethod("sendPacket", packet);
            sendPacket.setAccessible(true);

            Class<?> injectorClass = Class.forName("org.spigotmc.ProtocolInjector$PacketTitle");
            Class<?> enumClass = Class.forName("org.spigotmc.ProtocolInjector$PacketTitle$Action");

            /*for (int i = 0; i < injectorClass.getConstructors().length; i++) {
                Constructor<?> c = injectorClass.getConstructors()[i];
                System.out.println("Constructor " + i + " \n" + c + "\n");
            }*/

            Object packetTitle = null;

            for (Object o : enumClass.getEnumConstants()) {
                if (o.toString().equals("TIMES")) {
                    packetTitle = injectorClass.getConstructor(enumClass, int.class, int.class, int.class).newInstance(o, fadeIn, stay, fadeOut);
                }
            }

            if (packetTitle != null) {
                sendPacket.invoke(con, packetTitle);
            } else {
                throw new NullPointerException("No TIMES in " + enumClass.getName());
            }

            if (title != null) {
                Object packetTitle2 = null;

                for (Object o : enumClass.getEnumConstants()) {
                    if (o.toString().equals("TITLE")) {
                        packetTitle2 = injectorClass.getConstructor(enumClass, chatbasecomponent).newInstance(o, title);
                    }
                }

                if (packetTitle2 != null) {
                    sendPacket.invoke(con, packetTitle2);
                } else {
                    throw new NullPointerException("No TITLE in " + enumClass.getName());
                }
            }
            if (subtitle != null) {
                Object packetTitle2 = null;

                for (Object o : enumClass.getEnumConstants()) {
                    if (o.toString().equals("SUBTITLE")) {
                        packetTitle2 = injectorClass.getConstructor(enumClass, chatbasecomponent).newInstance(o, subtitle);
                    }
                }

                if (packetTitle2 != null) {
                    sendPacket.invoke(con, packetTitle2);
                } else {
                    throw new NullPointerException("No SUBTITLE in " + enumClass.getName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getTitle() {
        return rawTitle;
    }

    public String getSubtitle() {
        return rawSubtitle;
    }

    private void setTitle(String title) {
        try {
            rawTitle = title;
            Class<?> chatserializer = Class.forName(ReflectionTool.getMinecraftPackage() + ".ChatSerializer");

            Method m = chatserializer.getMethod("a", String.class);
            m.setAccessible(true);
            Object ob = m.invoke(null, TextConverter.convert(title));

            this.title = ob;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setSubtitle(String subtitle) {
        try {
            rawSubtitle = subtitle;
            Class<?> chatserializer = Class.forName(ReflectionTool.getMinecraftPackage() + ".ChatSerializer");

            Method m = chatserializer.getMethod("a", String.class);
            m.setAccessible(true);
            Object ob = m.invoke(null, TextConverter.convert(subtitle));

            this.subtitle = ob;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public enum TitleType {

        TITLE,
        SUBTITLE
    }
}
