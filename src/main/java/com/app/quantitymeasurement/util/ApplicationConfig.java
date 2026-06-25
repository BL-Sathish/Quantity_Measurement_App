package com.app.quantitymeasurement.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper class to load properties dynamically from application.properties.
 * Provides typed getter methods for common configuration values.
 */
public class ApplicationConfig {

    private static final String CONFIG_FILE = "application.properties";
    private static ApplicationConfig instance;
    private final Properties properties;

    private ApplicationConfig() {
        properties = new Properties();
        loadProperties();
    }

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                System.err.println("WARNING: " + CONFIG_FILE + " not found in classpath. Using defaults.");
                return;
            }
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load " + CONFIG_FILE + ": " + e.getMessage());
        }
    }

    /**
     * Gets a string property value.
     *
     * @param key          the property key
     * @param defaultValue the default value if key is not found
     * @return the property value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Gets an integer property value.
     *
     * @param key          the property key
     * @param defaultValue the default value if key is not found or not parseable
     * @return the property value as int
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("WARNING: Invalid integer value for key '" + key + "': " + value);
            return defaultValue;
        }
    }

    // ─────────────────────────────────────────────
    // Convenience getters for database configuration
    // ─────────────────────────────────────────────

    public String getDbDriver() {
        return getProperty("db.driver", "org.h2.Driver");
    }

    public String getDbUrl() {
        return getProperty("db.url", "jdbc:h2:mem:quantity_db;DB_CLOSE_DELAY=-1;MODE=MySQL");
    }

    public String getDbUser() {
        return getProperty("db.user", "sa");
    }

    public String getDbPassword() {
        return getProperty("db.password", "");
    }

    public int getPoolInitialSize() {
        return getIntProperty("db.pool.initialSize", 5);
    }

    public int getPoolMaxSize() {
        return getIntProperty("db.pool.maxSize", 10);
    }

    public String getRepositoryType() {
        return getProperty("repository.type", "database");
    }
}
