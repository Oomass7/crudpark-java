package com.crudzaso.crudpark.view;

import com.crudzaso.crudpark.model.Operator;
import com.crudzaso.crudpark.service.AuthService;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana de inicio de sesión para operadores
 */
public class LoginFrame extends JFrame {
    
    private final AuthService authService;
    private JTextField documentField;
    private JButton loginButton;
    
    public LoginFrame() {
        this.authService = new AuthService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("CrudPark - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setPreferredSize(new Dimension(0, 80));
        titlePanel.setLayout(new GridBagLayout());
        
        JLabel titleLabel = new JLabel("CrudPark - Crudzaso");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Icono de usuario
        JLabel iconLabel = new JLabel("👤", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(iconLabel, gbc);
        
        // Etiqueta de documento
        JLabel documentLabel = new JLabel("Documento:");
        documentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(documentLabel, gbc);
        
        // Campo de documento
        documentField = new JTextField(20);
        documentField.setFont(new Font("Arial", Font.PLAIN, 14));
        documentField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(documentField, gbc);
        
        // Botón de inicio de sesión
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(250, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(loginButton, gbc);
        
        // Agregar paneles al frame
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Event listeners
        loginButton.addActionListener(e -> handleLogin());
        documentField.addActionListener(e -> handleLogin());
        
        // Focus en el campo de documento
        SwingUtilities.invokeLater(() -> documentField.requestFocus());
    }
    
    private void handleLogin() {
        String document = documentField.getText().trim();
        
        if (document.isEmpty()) {
            showError("Por favor ingrese su documento");
            return;
        }
        
        loginButton.setEnabled(false);
        loginButton.setText("Iniciando sesión...");
        
        // Ejecutar en un hilo separado para no bloquear la UI
        SwingWorker<Operator, Void> worker = new SwingWorker<>() {
            @Override
            protected Operator doInBackground() throws Exception {
                return authService.login(document);
            }
            
            @Override
            protected void done() {
                try {
                    Operator operator = get();
                    showSuccess("Bienvenido, " + operator.getFullName());
                    
                    // Abrir ventana principal
                    SwingUtilities.invokeLater(() -> {
                        MainFrame mainFrame = new MainFrame(operator);
                        mainFrame.setVisible(true);
                        dispose();
                    });
                    
                } catch (Exception ex) {
                    showError(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                } finally {
                    loginButton.setEnabled(true);
                    loginButton.setText("Iniciar Sesión");
                }
            }
        };
        
        worker.execute();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
