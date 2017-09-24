package com.netshell.libraries.dbmodules.dbcommon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

final class ConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
    private static final ThreadLocal<LocalConnection> localConnection = new ThreadLocal<>();
    private static final JDBCProperties PROPERTIES = JDBCProperties.getInstance();

    static Connection getConnection(boolean newConnection) throws SQLException {
        return newConnection
                ? getNewConnection()
                : getConnection();
    }

    static void closeConnection(boolean commit) {
        LOGGER.trace("Invoking closeConnection() with commit {0}", commit);
        final LocalConnection c = ConnectionManager.localConnection.get();
        if (c == null) {
            LOGGER.trace("No Connection Initialized.");
            return;
        }

        try (Connection connection = c.connection) {
            if (commit) {
                LOGGER.debug("Commit all transaction on this connection");
                connection.commit();
            } else {
                LOGGER.debug("Rolling back all transaction on this connection");
                connection.rollback();
            }
            connection.setAutoCommit(c.initialAutoCommit);
        } catch (SQLException e) {
            LOGGER.error("Error Occurred while closing connection");
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.localConnection.remove();
        }
    }

    private static Connection getNewConnection() throws SQLException {
        LOGGER.debug("Creating new connection");
        return DriverManager.getConnection(
                PROPERTIES.getJdbcConnectionString(),
                PROPERTIES.getJdbcUser(),
                PROPERTIES.getJdbcPassword());
    }

    private static Connection getConnection() throws SQLException {

        LocalConnection c = localConnection.get();

        if (c == null) {
            LOGGER.debug("No connection found. Creating new one");
            c = new LocalConnection();
            c.connection = getNewConnection();
            c.initialAutoCommit = c.connection.getAutoCommit();
            LOGGER.trace("Initial AutoCommit {0}", c.initialAutoCommit);
            c.connection.setAutoCommit(false);
            localConnection.set(c);
        }

        return c.connection;
    }

    private static final class LocalConnection {
        Connection connection;
        boolean initialAutoCommit;
    }

}
