package com.crudzaso.crudpark.dao;

import com.crudzaso.crudpark.config.DatabaseConfig;
import com.crudzaso.crudpark.model.Payment;

import java.sql.*;

/**
 * DAO para operaciones con la tabla Payments
 */
public class PaymentDAO {
    private final DatabaseConfig dbConfig;
    
    public PaymentDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    /**
     * Crea un nuevo pago
     */
    public Payment create(Payment payment) throws SQLException {
        String sql = "INSERT INTO public.\"Payments\" (\"TicketId\", \"SubscriptionId\", \"OperatorId\", " +
                    "\"Amount\", \"PaymentMethod\", \"PaymentTime\", \"ReferenceNumber\") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING \"Id\"";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (payment.getTicketId() != null) {
                stmt.setInt(1, payment.getTicketId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            
            if (payment.getSubscriptionId() != null) {
                stmt.setInt(2, payment.getSubscriptionId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            if (payment.getOperatorId() != null) {
                stmt.setInt(3, payment.getOperatorId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            stmt.setBigDecimal(4, payment.getAmount());
            stmt.setString(5, payment.getPaymentMethod());
            stmt.setTimestamp(6, Timestamp.valueOf(payment.getPaymentTime()));
            stmt.setString(7, payment.getReferenceNumber());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                payment.setId(rs.getInt("Id"));
                return payment;
            }
        }
        return null;
    }
    
    /**
     * Busca un pago por ID de ticket
     */
    public Payment findByTicketId(Integer ticketId) throws SQLException {
        String sql = "SELECT \"Id\", \"TicketId\", \"SubscriptionId\", \"OperatorId\", \"Amount\", " +
                    "\"PaymentMethod\", \"PaymentTime\", \"ReferenceNumber\", \"CreatedAt\" " +
                    "FROM public.\"Payments\" WHERE \"TicketId\" = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        }
        return null;
    }
    
    /**
     * Mapea un ResultSet a un objeto Payment
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("Id"));
        
        int ticketId = rs.getInt("TicketId");
        if (!rs.wasNull()) {
            payment.setTicketId(ticketId);
        }
        
        int subscriptionId = rs.getInt("SubscriptionId");
        if (!rs.wasNull()) {
            payment.setSubscriptionId(subscriptionId);
        }
        
        int operatorId = rs.getInt("OperatorId");
        if (!rs.wasNull()) {
            payment.setOperatorId(operatorId);
        }
        
        payment.setAmount(rs.getBigDecimal("Amount"));
        payment.setPaymentMethod(rs.getString("PaymentMethod"));
        
        Timestamp paymentTime = rs.getTimestamp("PaymentTime");
        if (paymentTime != null) {
            payment.setPaymentTime(paymentTime.toLocalDateTime());
        }
        
        payment.setReferenceNumber(rs.getString("ReferenceNumber"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            payment.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return payment;
    }
}
