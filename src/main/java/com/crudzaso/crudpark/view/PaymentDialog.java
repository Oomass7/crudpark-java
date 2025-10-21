package com.crudzaso.crudpark.view;

import com.crudzaso.crudpark.model.Operator;
import com.crudzaso.crudpark.model.Payment;
import com.crudzaso.crudpark.model.Ticket;
import com.crudzaso.crudpark.service.ParkingService;
import com.crudzaso.crudpark.util.CurrencyFormatter;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para procesar pagos
 */
public class PaymentDialog extends JDialog {
    
    private final Ticket ticket;
    private final Operator operator;
    private final ParkingService parkingService;
    
    private JComboBox<String> paymentMethodCombo;
    
    public PaymentDialog(Frame parent, Ticket ticket, Operator operator, ParkingService parkingService) {
        super(parent, "Procesar Pago", true);
        this.ticket = ticket;
        this.operator = operator;
        this.parkingService = parkingService;
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Panel de información
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información del Pago"));
        
        JLabel plateLabel = new JLabel("Placa: " + ticket.getVehiclePlate());
        plateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        plateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel ticketLabel = new JLabel("Ticket: " + ticket.getTicketNumber());
        ticketLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ticketLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel amountLabel = new JLabel("Monto a Pagar: " + CurrencyFormatter.format(ticket.getTotalAmount()));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountLabel.setForeground(new Color(231, 76, 60));
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(plateLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(ticketLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(amountLabel);
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Panel de método de pago
        JPanel paymentPanel = new JPanel(new GridBagLayout());
        paymentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel methodLabel = new JLabel("Método de Pago:");
        methodLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        paymentPanel.add(methodLabel, gbc);
        
        paymentMethodCombo = new JComboBox<>(new String[]{"CASH", "CARD", "TRANSFER"});
        paymentMethodCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentMethodCombo.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        gbc.gridy = 0;
        paymentPanel.add(paymentMethodCombo, gbc);
        
        mainPanel.add(paymentPanel, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton confirmButton = new JButton("Confirmar Pago");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(46, 204, 113));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> handlePayment());
        
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void handlePayment() {
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
        
        try {
            Payment payment = parkingService.registerPayment(
                ticket.getId(), 
                paymentMethod, 
                operator.getId()
            );
            
            JOptionPane.showMessageDialog(this,
                "Pago registrado exitosamente\n" +
                "Método: " + paymentMethod + "\n" +
                "Monto: " + CurrencyFormatter.format(payment.getAmount()),
                "Pago Exitoso",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al procesar el pago: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
