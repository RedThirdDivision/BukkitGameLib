package de.jeter.bukkitgamelib.database;

import de.jeter.bukkitgamelib.Main;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {

    private Connection conn = null;
    private final Type type;

    public enum Type {
        SQLite,
        MYSQL;
    }

    public Database(String driver, Type type) {
        this.type = type;
        try {
            Class d = Class.forName(driver);
            Object o = d.newInstance();
            if (!(o instanceof Driver)) {
                Main.getInstance().getLogger().severe("Driver is not an instance of the Driver class!");
            } else {
                Driver dr = (Driver) o;
                DriverManager.registerDriver(dr);
            }
        } catch (Exception ex) {
            Main.getInstance().getLogger().severe("Driver not found! " + driver);
            ex.printStackTrace();
        }
    }

    /**
     * Gets the type of the loaded database.
     *
     * @return {@link de.eldercraft.eldersignmanager.database.Database.Type}
     */
    public Type getType() {
        return type;
    }

    /**
     * Tests the database connection.
     *
     * @return true if connection was successful
     */
    public final boolean testConnection() {
        try {
            getConnection();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Gets the connection
     *
     * @return the database connection
     * @throws SQLException if connection failed
     */
    public final Connection getConnection() throws SQLException {
        if ((conn == null || conn.isClosed()) || !isDbConnected()) {
            reactivateConnection();
        }
        return conn;
    }

    public boolean isDbConnected() {
        final String CHECK_SQL_QUERY = "SELECT 1";
        boolean isConnected = false;
        try {           
            PreparedStatement statement = conn.prepareStatement(CHECK_SQL_QUERY);
            statement.execute();
            isConnected = true;
        } catch (SQLException | NullPointerException e) {
            Main.getInstance().getLogger().warning("Connection timeout! Reconnecting...");
        }
        return isConnected;
    }

    /**
     * Used to reactivate the connection
     *
     * @param conn if connection failed
     */
    public final void setConnection(Connection conn) {
        this.conn = conn;
    }

    /**
     * closes the actual connection to the database
     *
     * @throws SQLException if connection can't be closed.
     */
    public final void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    /**
     * closes a statement
     *
     * @param s the statement to close
     * @throws SQLException if the statement can't be closed
     */
    public final void closeStatement(Statement s) throws SQLException {
        if (s != null) {
            s.close();
        }

    }

    /**
     * closes a resultset
     *
     * @param rs the resultset to close
     * @throws SQLException if the ResultSet can't be closed
     */
    public final void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    /**
     * creates a new statement
     *
     * @return The created statement
     * @throws SQLException if Statement can't be created
     */
    public final Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    /**
     * creates a new PreparedStatement
     *
     * @param query The query for the prepared statement
     * @return the PreparedStatement
     * @throws SQLException if the Statement can't be created.
     */
    public final PreparedStatement getPreparedStatement(String query) throws SQLException {
        PreparedStatement ret = getConnection().prepareStatement(query);
        return ret;
    }

    /**
     * Creates a new Statement and executes it
     *
     * @param query the query to execute
     * @throws SQLException if statement can't be executed
     */
    public final void executeStatement(String query) throws SQLException {
        Statement s = getStatement();
        s.execute(query);
        closeStatement(s);
        closeConnection();
    }

    /**
     * Reactivates the connection to the Database.
     *
     * @throws SQLException if connection failed
     */
    public abstract void reactivateConnection() throws SQLException;

}
