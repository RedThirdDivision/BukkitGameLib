package de.jeter.bukkitgamelib.command;

public enum CommandResult {

    /**
     * Command was executed successful
     */
    SUCCESS(null),
    /**
     * The user has not the permission to execute the command
     */
    NO_PERMISSION(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_NO_PERMISSION.getString()),
    /**
     * The user has not the permission to execute this command for other
     * players.
     */
    NO_PERMISSION_OTHER(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_NO_PERMISSION_OTHER.getString()),
    /**
     * The sender has used the command wrong. Sends help message
     */
    ERROR(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_WRONG_USAGE.getString()),
    /**
     * Tells the sender that the given player in the arguments is not online
     */
    NOT_ONLINE(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_NOT_ONLINE.getString()),
    /**
     * Tells the sender that the given String is not a player
     */
    NOT_A_PLAYER(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_NOT_A_PLAYER.getString()),
    /**
     * Tells the sender that the given argument has to be a number
     */
    NOT_A_NUMBER(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_NOT_A_NUMBER.getString()),
    /**
     * Tells the sender that the given argument has to be a world
     */
    NOT_A_WORLD(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_NOT_A_WORLD.getString()),
    /**
     * Tells the sender that the given argument is not a mobname
     */
    NOT_A_MOB(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_NOT_A_MOB.getString()),
    /**
     * Tells the sender that the given mob cannot be spawned
     */
    NOT_SPAWNABLE(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_NOT_SPAWNABLE.getString()),
    /**
     * Tells the sender that there is no free space for him to teleport to a location.
     */
    NO_SPACE(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_NO_FREE_SPACE.getString()),
    
    /**
     * Tells the sender that this Command is not made for his sender type
     */
    WRONG_SENDER(de.jeter.bukkitgamelib.utils.Locales.COMMAND_MESSAGES_WRONG_SENDER_TYPE.getString());

    private final String msg;

    private CommandResult(String msg) {
        this.msg = msg;
    }

    protected String getMessage() {
        return this.msg;
    }
}
