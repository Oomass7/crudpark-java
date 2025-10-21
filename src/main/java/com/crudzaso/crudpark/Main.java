package com.crudzaso.crudpark;

import com.crudzaso.crudpark.config.DatabaseConfig;
import com.crudzaso.crudpark.view.LoginFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

/**
 * Clase principal de la aplicación CrudPark
 */
public class Main {
    
    public static void main(String[] args) {
        // Configurar Look and Feel moderno
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("No se pudo configurar FlatLaf, usando Look and Feel por defecto");
        }
        
        // Verificar conexión a la base de datos
        System.out.println("=================================");
        System.out.println("  CrudPark - Sistema de Parqueadero");
        System.out.println("=================================");
        System.out.println();
        
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();
        System.out.print("Verificando conexión a la base de datos... ");
        
        if (dbConfig.testConnection()) {
            System.out.println("✓ Conexión exitosa");
            System.out.println();
            
            // Iniciar la aplicación
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        } else {
            System.out.println("✗ Error de conexión");
            System.err.println();
            System.err.println("No se pudo conectar a la base de datos.");
            System.err.println("Por favor verifique:");
            System.err.println("1. PostgreSQL está ejecutándose");
            System.err.println("2. La base de datos 'crudpark' existe");
            System.err.println("3. Las credenciales en config.properties son correctas");
            System.err.println();
            System.err.println("Cree un archivo 'config.properties' basado en 'config.properties.example'");
            
            JOptionPane.showMessageDialog(null,
                "No se pudo conectar a la base de datos.\n" +
                "Verifique la configuración en config.properties",
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE);
            
            System.exit(1);
        }
    }
}
