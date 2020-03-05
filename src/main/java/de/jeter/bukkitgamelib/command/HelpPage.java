package de.jeter.bukkitgamelib.command;

import de.jeter.bukkitgamelib.Main;
import de.jeter.bukkitgamelib.utils.LocaleManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;

class HelpPage {

    private final List<CommandHelp> helpPages = new ArrayList<>();
    private final List<String> HELP_TEXT = new ArrayList<>();
    private final Map<String, String> helpTexts = new HashMap<>();
    private final String command;

    /**
     * Creates a new helppage for the provided command.
     *
     * @param command the command this HelpPage is for
     */
    public HelpPage(String command) {
        this.command = command;
        for (String key : LocaleManager.getHelpTexts().keySet()) {
            if (key.toLowerCase().contains(command.toLowerCase())) {
                helpTexts.put(key, LocaleManager.getHelpTexts().get(key));
            }
        }
    }

    /**
     * Prepares the help message
     */
    public void prepare() {
        if (helpPages == null || helpPages.isEmpty()) {
            return;
        }
        HELP_TEXT.add("§a------------------------§1" + de.jeter.bukkitgamelib.utils.Locales.HELP.getString() + "§a-------------------------");
        for (CommandHelp ch : helpPages) {
            HELP_TEXT.add("§6/" + ch.getText());
        }
        HELP_TEXT.add("§a-----------------------------------------------------");
    }

    /**
     * Automatically gets all help texts from the given arguments of the and
     * prepares the message.
     *
     * @param command the command this HelpPage is for
     * @param arguments the arguments to search for
     */
    public HelpPage(String command, String... arguments) {
        this.command = command;
        for (String key : LocaleManager.getHelpTexts().keySet()) {
            if (key.toLowerCase().contains(command.toLowerCase())) {
                helpTexts.put(key, LocaleManager.getHelpTexts().get(key));
            }
        }

        for (int i = 0; i < arguments.length; i++) {
            int num = i + 1;

            for (String loc : helpTexts.keySet()) {

                String[] l = loc.toString().toLowerCase().split("_");

                for (String s : l) {
                    if (!s.equalsIgnoreCase(command)) {
                        continue;
                    }
                    if (loc.toString().toLowerCase().contains("" + num)) {
                        if (arguments[i].isEmpty()) {
                            addPage(arguments[i], helpTexts.get(loc));
                        } else {
                            addPage(" " + arguments[i], helpTexts.get(loc));
                        }
                    }
                }

            }
        }

        prepare();
    }

    /**
     * Manually add a message to a given argument.
     *
     * @param argument the argument the message is for
     * @param description the message
     */
    public final void addPage(String argument, String description) {
        helpPages.add(new CommandHelp(command + argument, description));
    }

    /**
     * Checks if the sender wants to see the help by typing /command help or
     * /command ? and sends the text if he did.
     *
     * @param s the commandsender
     * @param args the arguments
     * @return true if helptext was sent
     */
    public boolean sendHelp(CommandSender s, CommandArgs args) {
        if (args.getLength() == 1 && (args.getString(0).equalsIgnoreCase("?") || args.getString(0).equalsIgnoreCase("help")) && !HELP_TEXT.isEmpty()) {
            for (String string : HELP_TEXT) {
                s.sendMessage(string);
            }
            return true;
        }
        return false;
    }

    private class CommandHelp {

        private final String FULL_TEXT;

        public CommandHelp(String cmd, String description) {
            this.FULL_TEXT = "§6" + cmd + "§7" + " - " + description;
        }

        public String getText() {
            return FULL_TEXT;
        }

    }

}
