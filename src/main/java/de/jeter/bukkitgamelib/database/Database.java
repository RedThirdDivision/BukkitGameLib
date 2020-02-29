package de.jeter.bukkitgamelib.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {

    private Connection conn = null;

    protected Database(String driver) {
        try {
            Class<?> driverClass = Class.forName(driver);
            Object o = driverClass.newInstance();
            if (!(o instanceof Driver)) {
                throw new IllegalArgumentException("Class is not an instance of the Driver class!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Used to reactivate the connection
     *
     * @param conn
     */
    public final void setConnection(Connection conn) {
        this.conn = conn;
    }

    /**
     * Gets the connection
     *
     * @return
     * @throws SQLException
     */
    public final Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed())
            reactivateConnection();
        return conn;
    }

    /**
     * closes the actual connection to the database
     *
     * @throws SQLException
     */
    public final void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed())
            conn.close();
    }

    /**
     * closes a statement
     *
     * @param s
     * @throws SQLException
     */
    public final void closeStatement(Statement s) throws SQLException {
        if (s != null)
            s.close();

    }

    /**
     * closes a resultset
     *
     * @param rs
     * @throws SQLException
     */
    public final void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null)
            rs.close();
    }

    /**
     * creates a new statement
     *
     * @return
     * @throws SQLException
     */
    public final Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    /**
     * creates a new PreparedStatement
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public final PreparedStatement getPreparedStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    /**
     * Creates a new Statement and executes it
     *
     * @param query
     * @throws SQLException
     */
    public final void executeStatement(String query) throws SQLException {
        Statement s = getStatement();
        s.execute(query);
        closeStatement(s);
    }

    public abstract void reactivateConnection() throws SQLException;

}
