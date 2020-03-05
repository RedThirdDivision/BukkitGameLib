package de.jeter.bukkitgamelib.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BukkitCommand extends Command {

    private CommandManager exe = null;
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

    public void setExecutor(CommandManager exe) {
        this.exe = exe;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alais, String[] args) {
        if (this.exe != null) {
            return exe.onTabComplete(sender, this, alais, args);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

}
