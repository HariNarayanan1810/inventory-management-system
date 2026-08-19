package com.example.inventorydashboard.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConfig {

    private static final Properties PROPERTIES = loadProperties();

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
    }

    public static String getUrl() {
        return get("spring.datasource.url", "jdbc:postgresql://localhost:5432/inventory_management");
    }

    public static String getUsername() {
        return get("spring.datasource.username", "postgres");
    }

    public static String getPassword() {
        return get("spring.datasource.password", "postgres");
    }

    private static String get(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        String envKey = key.toUpperCase().replace('.', '_');
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return resolvePlaceholder(PROPERTIES.getProperty(key, defaultValue));
    }

    private static String resolvePlaceholder(String value) {
        if (value == null || !value.startsWith("${") || !value.endsWith("}")) {
            return value;
        }

        String expression = value.substring(2, value.length() - 1);
        String[] parts = expression.split(":", 2);
        String envValue = System.getenv(parts[0]);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return parts.length == 2 ? parts[1] : "";
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getResourceAsStream("/application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load application.properties", ex);
        }
        return properties;
    }
}
