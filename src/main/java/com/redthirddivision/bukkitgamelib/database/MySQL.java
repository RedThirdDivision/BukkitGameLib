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
package com.redthirddivision.bukkitgamelib.database;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <strong>Project:</strong> R3DBukkitGameLib <br>
 * <strong>File:</strong> MySQL.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
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
