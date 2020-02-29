package de.jeter.bukkitgamelib.database;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends Database {

    private final File dbFile;

    /**
     * Creates a new instance for SQLite databases.
     * Statements are done by {@link de.thejeterlp.bukkit.bukkittools.database.SQLite#executeStatement(java.lang.String) }
     *
     * @param dbFile Database file
     */
    public SQLite(File dbFile) {
        super("org.sqlite.JDBC");
        dbFile.getParentFile().mkdirs();
        this.dbFile = dbFile;
    }

    @Override
    public void reactivateConnection() throws SQLException {
        setConnection(DriverManager.getConnection("jdbc:sqlite://" + dbFile.getAbsolutePath()));
    }

}
