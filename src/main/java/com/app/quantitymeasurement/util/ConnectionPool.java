package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Custom JDBC connection pool implementation.
 * Manages a pool of database connections with configurable initial and maximum sizes.
 * Supports connection validation, borrowing, returning, and clean shutdown.
 */
public class ConnectionPool {

    private final String url;
    private final String user;
    private final String password;
    private final int maxSize;
    private final BlockingQueue<Connection> availableConnections;
    private final List<Connection> allConnections;
    private boolean isShutdown = false;

    /**
     * Creates a new ConnectionPool with the given configuration.
     *
     * @param url         JDBC URL
     * @param user        Database username
     * @param password    Database password
     * @param initialSize Initial number of connections to create
     * @param maxSize     Maximum number of connections allowed
     */
    public ConnectionPool(String url, String user, String password, int initialSize, int maxSize) {
        if (url == null || url.isEmpty()) {
            throw new DatabaseException("Database URL cannot be null or empty");
        }
        if (initialSize < 1 || maxSize < 1 || initialSize > maxSize) {
            throw new DatabaseException("Invalid pool size configuration: initialSize=" + initialSize + ", maxSize=" + maxSize);
        }

        this.url = url;
        this.user = user;
        this.password = password;
        this.maxSize = maxSize;
        this.availableConnections = new ArrayBlockingQueue<>(maxSize);
        this.allConnections = new ArrayList<>();

        // Pre-create initial connections
        for (int i = 0; i < initialSize; i++) {
            Connection conn = createNewConnection();
            availableConnections.offer(conn);
            allConnections.add(conn);
        }
    }

    /**
     * Borrows a connection from the pool.
     * If no idle connections are available and pool has not reached max size, creates a new one.
     *
     * @return a valid JDBC Connection
     * @throws DatabaseException if pool is shut down or connection cannot be obtained
     */
    public synchronized Connection borrowConnection() {
        if (isShutdown) {
            throw new DatabaseException("Connection pool has been shut down");
        }

        Connection conn = availableConnections.poll();
        if (conn != null) {
            if (isValid(conn)) {
                return conn;
            }
            // Connection is stale, remove it and try to create a new one
            allConnections.remove(conn);
            closeQuietly(conn);
        }

        // No available connections, try to grow the pool
        if (allConnections.size() < maxSize) {
            conn = createNewConnection();
            allConnections.add(conn);
            return conn;
        }

        // Pool is at max capacity, block until one becomes available
        try {
            conn = availableConnections.take();
            if (isValid(conn)) {
                return conn;
            }
            allConnections.remove(conn);
            closeQuietly(conn);
            // Create a replacement
            conn = createNewConnection();
            allConnections.add(conn);
            return conn;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DatabaseException("Interrupted while waiting for a connection", e);
        }
    }

    /**
     * Returns a connection back to the pool for reuse.
     *
     * @param connection the connection to return
     */
    public synchronized void returnConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        if (isShutdown) {
            closeQuietly(connection);
            return;
        }
        if (isValid(connection)) {
            availableConnections.offer(connection);
        } else {
            allConnections.remove(connection);
            closeQuietly(connection);
        }
    }

    /**
     * Shuts down the pool and closes all connections.
     */
    public synchronized void shutdown() {
        isShutdown = true;
        for (Connection conn : allConnections) {
            closeQuietly(conn);
        }
        allConnections.clear();
        availableConnections.clear();
    }

    /**
     * Returns statistics about the connection pool.
     *
     * @return formatted string with pool stats
     */
    public synchronized String getPoolStats() {
        return String.format("ConnectionPool[total=%d, available=%d, active=%d, maxSize=%d]",
                allConnections.size(),
                availableConnections.size(),
                allConnections.size() - availableConnections.size(),
                maxSize);
    }

    public int getAvailableCount() {
        return availableConnections.size();
    }

    public int getTotalCount() {
        return allConnections.size();
    }

    // ─────────────────────────────────────────────
    // Internal helpers
    // ─────────────────────────────────────────────

    private Connection createNewConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create database connection: " + e.getMessage(), e);
        }
    }

    private boolean isValid(Connection connection) {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    private void closeQuietly(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Ignore close errors
        }
    }
}
