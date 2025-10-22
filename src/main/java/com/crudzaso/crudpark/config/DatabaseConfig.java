package com.crudzaso.crudpark.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Configuración y gestión de conexiones a la base de datos PostgreSQL
 */
public class DatabaseConfig {
    private static DatabaseConfig instance;
    private Properties properties;
    
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    
    private DatabaseConfig() {
        loadConfiguration();
    }
    
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }
    
    private void loadConfiguration() {
        properties = new Properties();
        
        // Cargar desde el classpath (resources)
        try (var stream = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (stream != null) {
                properties.load(stream);
                System.out.println("✓ Configuración cargada desde database.properties");
            } else {
                throw new IOException("Archivo no encontrado en classpath");
            }
        } catch (IOException e) {
            System.out.println("⚠ No se encontró database.properties, usando valores por defecto");
            properties.setProperty("db.host", "localhost");
            properties.setProperty("db.port", "5432");
            properties.setProperty("db.name", "crudpark");
            properties.setProperty("db.user", "postgres");
            properties.setProperty("db.password", "postgres");
        }
        
        host = properties.getProperty("db.host");
        port = properties.getProperty("db.port");
        database = properties.getProperty("db.name");
        user = properties.getProperty("db.user");
        password = properties.getProperty("db.password");
    }
    
    /**
     * Obtiene una conexión a la base de datos
     */
    public Connection getConnection() throws SQLException {
        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL no encontrado", e);
        }
    }
    
    /**
     * Prueba la conexión a la base de datos
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar con la base de datos: " + e.getMessage());
            return false;
        }
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
