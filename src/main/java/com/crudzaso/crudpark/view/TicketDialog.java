package com.crudzaso.crudpark.view;

import com.crudzaso.crudpark.model.Operator;
import com.crudzaso.crudpark.model.Ticket;
import com.crudzaso.crudpark.util.CurrencyFormatter;
import com.crudzaso.crudpark.util.DateTimeFormatter;
import com.crudzaso.crudpark.util.QRCodeGenerator;
import com.google.zxing.WriterException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * Diálogo para mostrar e imprimir tickets
 */
public class TicketDialog extends JDialog {
    
    private final Ticket ticket;
    private final Operator operator;
    private final boolean isEntry;
    
    public TicketDialog(Frame parent, Ticket ticket, Operator operator, boolean isEntry) {
        super(parent, "Ticket de " + (isEntry ? "Ingreso" : "Salida"), true);
        this.ticket = ticket;
        this.operator = operator;
        this.isEntry = isEntry;
        initComponents();
    }
    
    private void initComponents() {
        setSize(450, 650);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Panel del ticket
        JPanel ticketPanel = createTicketPanel();
        mainPanel.add(ticketPanel, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton printButton = new JButton("Imprimir Ticket");
        printButton.setFont(new Font("Arial", Font.BOLD, 14));
        printButton.setBackground(new Color(52, 152, 219));
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.addActionListener(e -> printTicket());
        
        JButton closeButton = new JButton("Cerrar");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createTicketPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        // Título
        JLabel titleLabel = new JLabel("CrudPark - Crudzaso", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(15));
        panel.add(titleLabel);
        
        // Línea separadora
        panel.add(Box.createVerticalStrut(10));
        panel.add(createSeparator());
        panel.add(Box.createVerticalStrut(10));
        
        // Información del ticket
        panel.add(createInfoLabel("Ticket #: " + ticket.getTicketNumber()));
        panel.add(createInfoLabel("Placa: " + ticket.getVehiclePlate()));
        panel.add(createInfoLabel("Tipo: " + ticket.getTicketType()));
        panel.add(createInfoLabel("Tipo Vehículo: " + ticket.getVehicleType()));
        panel.add(createInfoLabel("Ingreso: " + DateTimeFormatter.formatDateTime(ticket.getEntryTime())));
        
        if (!isEntry && ticket.getExitTime() != null) {
            panel.add(createInfoLabel("Salida: " + DateTimeFormatter.formatDateTime(ticket.getExitTime())));
            panel.add(createInfoLabel("Tiempo: " + DateTimeFormatter.formatDuration(ticket.getTotalMinutes())));
            
            if (ticket.getTotalAmount() != null) {
                String amountText = "Total: " + CurrencyFormatter.format(ticket.getTotalAmount());
                JLabel amountLabel = createInfoLabel(amountText);
                amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
                amountLabel.setForeground(new Color(231, 76, 60));
                panel.add(amountLabel);
            }
        }
        
        panel.add(createInfoLabel("Operador: " + operator.getFullName()));
        
        // Código QR
        if (ticket.getQrCode() != null) {
            panel.add(Box.createVerticalStrut(10));
            panel.add(createSeparator());
            panel.add(Box.createVerticalStrut(10));
            
            try {
                BufferedImage qrImage = QRCodeGenerator.generateQRCode(ticket.getQrCode(), 200, 200);
                JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
                qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(qrLabel);
                
                JLabel qrTextLabel = new JLabel("QR: " + ticket.getQrCode(), SwingConstants.CENTER);
                qrTextLabel.setFont(new Font("Courier New", Font.PLAIN, 9));
                qrTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(Box.createVerticalStrut(5));
                panel.add(qrTextLabel);
            } catch (WriterException e) {
                System.err.println("Error generando QR: " + e.getMessage());
            }
        }
        
        // Mensaje final
        panel.add(Box.createVerticalStrut(10));
        panel.add(createSeparator());
        panel.add(Box.createVerticalStrut(10));
        JLabel thanksLabel = new JLabel("Gracias por su visita", SwingConstants.CENTER);
        thanksLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        thanksLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(thanksLabel);
        panel.add(Box.createVerticalStrut(15));
        
        return panel;
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(350, 1));
        return separator;
    }
    
    private void printTicket() {
        PrinterJob job = PrinterJob.getPrinterJob();
        
        // Configurar tamaño de papel para impresora térmica 58mm
        Paper paper = new Paper();
        double width = 58 * 72.0 / 25.4;  // 58mm a puntos (1 punto = 1/72 pulgada)
        double height = 200 * 72.0 / 25.4; // Altura variable (200mm)
        paper.setSize(width, height);
        paper.setImageableArea(5, 5, width - 10, height - 10); // Márgenes pequeños
        
        PageFormat pageFormat = job.defaultPage();
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        
        job.setPrintable(new TicketPrintable(), pageFormat);
        
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "Ticket enviado a impresión", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Error al imprimir: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Clase interna para imprimir el ticket
     */
    private class TicketPrintable implements Printable {
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
            
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            
            int y = 10;
            int lineHeight = 15;
            int x = 5; // Margen izquierdo pequeño
            
            // Configurar fuente más pequeña para 58mm
            g2d.setFont(new Font("Courier New", Font.BOLD, 9));
            
            // Título
            g2d.drawString("=======================", x, y);
            y += lineHeight;
            g2d.drawString(" CrudPark - Crudzaso", x, y);
            y += lineHeight;
            g2d.drawString("=======================", x, y);
            y += lineHeight + 5;
            
            // Información
            g2d.setFont(new Font("Courier New", Font.PLAIN, 8));
            g2d.drawString("Ticket: " + ticket.getTicketNumber(), x, y);
            y += lineHeight;
            g2d.drawString("Placa: " + ticket.getVehiclePlate(), x, y);
            y += lineHeight;
            g2d.drawString("Tipo: " + ticket.getTicketType(), x, y);
            y += lineHeight;
            
            // Dividir fecha y hora en dos líneas si es muy largo
            String entryDateTime = DateTimeFormatter.formatDateTime(ticket.getEntryTime());
            g2d.drawString("Ingreso:", x, y);
            y += lineHeight;
            g2d.drawString(" " + entryDateTime, x, y);
            y += lineHeight;
            
            if (!isEntry && ticket.getExitTime() != null) {
                String exitDateTime = DateTimeFormatter.formatDateTime(ticket.getExitTime());
                g2d.drawString("Salida:", x, y);
                y += lineHeight;
                g2d.drawString(" " + exitDateTime, x, y);
                y += lineHeight;
                g2d.drawString("Tiempo: " + DateTimeFormatter.formatDuration(ticket.getTotalMinutes()), x, y);
                y += lineHeight;
                
                if (ticket.getTotalAmount() != null) {
                    g2d.setFont(new Font("Courier New", Font.BOLD, 9));
                    g2d.drawString("Total: " + CurrencyFormatter.format(ticket.getTotalAmount()), x, y);
                    y += lineHeight;
                    g2d.setFont(new Font("Courier New", Font.PLAIN, 8));
                }
            }
            
            g2d.drawString("Operador:", x, y);
            y += lineHeight;
            g2d.drawString(" " + operator.getFullName(), x, y);
            y += lineHeight + 5;
            
            // QR Code más pequeño para 58mm
            if (ticket.getQrCode() != null) {
                try {
                    BufferedImage qrImage = QRCodeGenerator.generateQRCode(ticket.getQrCode(), 100, 100);
                    // Centrar el QR (58mm ≈ 165 puntos, QR 100px, centrado = (165-100)/2 ≈ 32)
                    g2d.drawImage(qrImage, 30, y, null);
                    y += 110;
                } catch (WriterException e) {
                    System.err.println("Error generando QR para impresión: " + e.getMessage());
                }
            }
            
            // Mensaje final
            g2d.setFont(new Font("Courier New", Font.PLAIN, 8));
            g2d.drawString("-----------------------", x, y);
            y += lineHeight;
            g2d.drawString(" Gracias por su visita", x, y);
            y += lineHeight;
            g2d.drawString("=======================", x, y);
            
            return PAGE_EXISTS;
        }
    }
}
