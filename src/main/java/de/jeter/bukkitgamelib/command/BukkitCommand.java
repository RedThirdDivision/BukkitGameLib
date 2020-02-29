package de.jeter.bukkitgamelib.command;

import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BukkitCommand extends Command {

    private CommandExecutor exe = null;
    private final List<String> aliases;

    protected BukkitCommand(String name, List<String> aliases) {
        super(name);
        this.aliases = aliases;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (exe != null) {
            exe.onCommand(sender, this, commandLabel, args);
        }
        return false;
    }

    public void setExecutor(CommandExecutor exe) {
        this.exe = exe;
    }

    @Override
    public List<String> getAliases() {
        return Collections.unmodifiableList(aliases);
    }

}
