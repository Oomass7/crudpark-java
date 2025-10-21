package com.crudzaso.crudpark.view;

import com.crudzaso.crudpark.model.Operator;
import com.crudzaso.crudpark.model.Rate;
import com.crudzaso.crudpark.model.Ticket;
import com.crudzaso.crudpark.service.ParkingService;
import com.crudzaso.crudpark.dao.RateDAO;
import com.crudzaso.crudpark.util.DateTimeFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Ventana principal de la aplicación
 */
public class MainFrame extends JFrame {

    private final Operator currentOperator;
    private final ParkingService parkingService;
    private final RateDAO rateDAO;

    private JTextField entryPlateField;
    private JComboBox<String> vehicleTypeCombo;
    private JTextField exitPlateField;
    private JTable openTicketsTable;
    private DefaultTableModel tableModel;

    public MainFrame(Operator operator) {
        this.currentOperator = operator;
        this.parkingService = new ParkingService();
        this.rateDAO = new RateDAO();
        initComponents();
        loadOpenTickets();
    }

    private void initComponents() {
        setTitle("CrudPark - Sistema de Parqueadero");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel superior con información del operador
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Tab de ingreso
        JPanel entryPanel = createEntryPanel();
        tabbedPane.addTab("Ingreso de Vehículo", entryPanel);

        // Tab de salida
        JPanel exitPanel = createExitPanel();
        tabbedPane.addTab("Salida de Vehículo", exitPanel);

        // Tab de vehículos dentro
        JPanel vehiclesPanel = createVehiclesPanel();
        tabbedPane.addTab("Vehículos Dentro", vehiclesPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 73, 94));
        panel.setPreferredSize(new Dimension(0, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("CrudPark - Crudzaso");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JLabel operatorLabel = new JLabel("Operador: " + currentOperator.getFullName());
        operatorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        operatorLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> handleLogout());

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(operatorLabel, BorderLayout.CENTER);
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createEntryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Registrar Ingreso de Vehículo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Placa
        JLabel plateLabel = new JLabel("Placa:");
        plateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(plateLabel, gbc);

        entryPlateField = new JTextField(20);
        entryPlateField.setFont(new Font("Arial", Font.PLAIN, 14));
        entryPlateField.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(entryPlateField, gbc);

        // Tipo de vehículo
        JLabel typeLabel = new JLabel("Tipo de Vehículo:");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(typeLabel, gbc);

        vehicleTypeCombo = new JComboBox<>();
        vehicleTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        vehicleTypeCombo.setPreferredSize(new Dimension(300, 35));
        loadVehicleTypes();
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(vehicleTypeCombo, gbc);

        // Botón de registro
        JButton registerButton = new JButton("Registrar Ingreso");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(300, 45));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> handleEntry());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        panel.add(registerButton, gbc);

        return panel;
    }

    private JPanel createExitPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Registrar Salida de Vehículo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Placa
        JLabel plateLabel = new JLabel("Placa:");
        plateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(plateLabel, gbc);

        exitPlateField = new JTextField(20);
        exitPlateField.setFont(new Font("Arial", Font.PLAIN, 14));
        exitPlateField.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(exitPlateField, gbc);

        // Botón de registro
        JButton registerButton = new JButton("Registrar Salida");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(300, 45));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> handleExit());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        panel.add(registerButton, gbc);

        return panel;
    }

    private JPanel createVehiclesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titleLabel = new JLabel("Vehículos Actualmente en el Parqueadero");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Tabla
        String[] columns = {"Ticket", "Placa", "Tipo", "Entrada", "Operador", "Tipo Ticket"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        openTicketsTable = new JTable(tableModel);
        openTicketsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        openTicketsTable.setRowHeight(25);
        openTicketsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(openTicketsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón de actualizar
        JButton refreshButton = new JButton("Actualizar Lista");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshButton.addActionListener(e -> loadOpenTickets());
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private void loadVehicleTypes() {
        try {
            List<Rate> rates = rateDAO.findAllActive();
            vehicleTypeCombo.removeAllItems();

            for (Rate rate : rates) {
                vehicleTypeCombo.addItem(rate.getTypeVehicle());
            }

            if (vehicleTypeCombo.getItemCount() == 0) {
                vehicleTypeCombo.addItem("Sedán");
                vehicleTypeCombo.addItem("SUV");
                vehicleTypeCombo.addItem("Moto");
            }
        } catch (SQLException e) {
            showError("Error al cargar tipos de vehículo: " + e.getMessage());
        }
    }

    private void handleEntry() {
        String plate = entryPlateField.getText().trim();
        String vehicleType = (String) vehicleTypeCombo.getSelectedItem();

        if (plate.isEmpty()) {
            showError("Por favor ingrese la placa del vehículo");
            return;
        }

        if (vehicleType == null) {
            showError("Por favor seleccione el tipo de vehículo");
            return;
        }

        try {
            Ticket ticket = parkingService.registerEntry(plate, vehicleType, currentOperator.getId());

            // Mostrar ticket
            TicketDialog ticketDialog = new TicketDialog(this, ticket, currentOperator, true);
            ticketDialog.setVisible(true);

            // Limpiar campos
            entryPlateField.setText("");

            // Actualizar lista
            loadOpenTickets();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handleExit() {
        String plate = exitPlateField.getText().trim();

        if (plate.isEmpty()) {
            showError("Por favor ingrese la placa del vehículo");
            return;
        }

        try {
            Ticket ticket = parkingService.registerExit(plate);

            // Mostrar ticket de salida
            TicketDialog ticketDialog = new TicketDialog(this, ticket, currentOperator, false);
            ticketDialog.setVisible(true);

            // Si requiere pago, mostrar diálogo de pago
            if (ticket.getTotalAmount() != null && ticket.getTotalAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                PaymentDialog paymentDialog = new PaymentDialog(this, ticket, currentOperator, parkingService);
                paymentDialog.setVisible(true);
            }

            // Limpiar campos
            exitPlateField.setText("");

            // Actualizar lista
            loadOpenTickets();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void loadOpenTickets() {
        try {
            List<Ticket> tickets = parkingService.getOpenTickets();
            tableModel.setRowCount(0);

            for (Ticket ticket : tickets) {
                Object[] row = {
                    ticket.getTicketNumber(),
                    ticket.getVehiclePlate(),
                    ticket.getVehicleType(),
                    DateTimeFormatter.formatDateTime(ticket.getEntryTime()),
                    ticket.getOperatorName(),
                    ticket.getTicketType()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Error al cargar tickets: " + e.getMessage());
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
