package de.jeter.bukkitgamelib.utils;

import de.jeter.bukkitgamelib.Main;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Locales {

    MESSAGE_PREFIX_INFO("Prefix.Info", "&a[INFO]&7 "),
    MESSAGE_PREFIX_ERROR("Prefix.Error", "&4[ERROR]&7 "),
    COMMAND_MESSAGES_WRONG_SENDER_TYPE("CommandMessages.WrongSenderType", "&4[ERROR] &7You can't execute this Command!"),
    COMMAND_MESSAGES_NO_PERMISSION("CommandMessages.NoPermission", "&4[ERROR] &7You don't have permission to do that! &c(%perm%)"),
    COMMAND_MESSAGES_NO_PERMISSION_OTHER("CommandMessages.NoPermissionOther", "&4[ERROR] &7You don't have permission to do that! &c(%perm%.other)"),
    COMMAND_MESSAGES_WRONG_USAGE("CommandMessages.WrongUsage", "&c[ERROR] &7Wrong usage! Please use &6/%cmd% help &7 to see the correct usage!"),
    COMMAND_MESSAGES_NOT_ONLINE("CommandMessages.TargetNotFound", "&c[ERROR] &7That player is not online."),
    COMMAND_MESSAGES_NOT_A_PLAYER("CommandMessages.NotAPlayer", "&c[ERROR] &7That is not a player."),
    COMMAND_MESSAGES_NOT_A_NUMBER("CommandMessages.NotANumber", "&c[ERROR] &7This is not a number!"),
    COMMAND_MESSAGES_NOT_A_WORLD("CommandMessages.WorldNotFound", "&c[ERROR] &7The world is not existing!"),
    COMMAND_MESSAGES_ENABLED("CommandMessages.Enabled", "enabled"),
    COMMAND_MESSAGES_DISABLED("CommandMessages.Disabled", "disabled"),
    COMMAND_MESSAGES_NOT_A_MOB("CommandMessages.NotAMob", "&c[ERROR] &7This is not a mob!"),
    COMMAND_MESSAGES_NOT_SPAWNABLE("CommandMessages.NotSpawnable", "&c[ERROR] &7This mob is not spawnable!"),
    COMMAND_MESSAGES_NO_FREE_SPACE("CommandMessages.NoSpace", "&c[ERROR] &7There is no space to teleport to the given location!"),
    HELP("Help.Help", "Help"),
    ;

    private Locales(String path, Object val) {
        this.path = path;
        this.value = val;
    }

    private final Object value;
    private final String path;
    private static YamlConfiguration cfg;
    private static final File f = new File(Main.getInstance().getDataFolder(), "locales.yml");

    public String getPath() {
        return path;
    }

    public Object getDefaultValue() {
        return value;
    }

    public String getString() {
        return Utils.replaceColors(cfg.getString(path));
    }
    
    public static void load() {
        Main.getInstance().getDataFolder().mkdirs();
        reload(false);
        for (Locales c : values()) {
            if (!cfg.contains(c.getPath())) {
                c.set(c.getDefaultValue(), false);
            }
        }
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void set(Object value, boolean save) {
        cfg.set(path, value);
        if (save) {
            try {
                cfg.save(f);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            reload(false);
        }
    }

    public static void reload(boolean complete) {
        if (!complete) {
            cfg = YamlConfiguration.loadConfiguration(f);
            return;
        }
        load();
    }

}
