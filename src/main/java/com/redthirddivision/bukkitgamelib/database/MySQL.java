package com.redthirddivision.bukkitgamelib.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends Database {

    private final String host, user, password, dbName;
    private final int port;

    /**
     * Creates a new instance for MySQL databases.
     * Statements are done by {@link de.thejeterlp.bukkit.bukkittools.database.MySQL#executeStatement(java.lang.String) }
     *
     * @param host the host where the mysql server is on.
     * @param user the username of the database-account
     * @param password the password of the suer for the database account
     * @param dbName the name of the database
     * @param port the port of the database server
     */
    public MySQL(String host, String user, String password, String dbName, int port) {
        super("com.mysql.jdbc.Driver");
        this.host = host;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.port = port;
    }

    @Override
    public void reactivateConnection() throws SQLException {
        String dsn = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        setConnection(DriverManager.getConnection(dsn, user, password));
    }

}
