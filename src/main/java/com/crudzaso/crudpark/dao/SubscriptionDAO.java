package com.crudzaso.crudpark.dao;

import com.crudzaso.crudpark.config.DatabaseConfig;
import com.crudzaso.crudpark.model.Subscription;

import java.sql.*;

/**
 * DAO para operaciones con la tabla Subscriptions
 */
public class SubscriptionDAO {
    private final DatabaseConfig dbConfig;
    
    public SubscriptionDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    /**
     * Busca una suscripción vigente por placa de vehículo
     */
    public Subscription findActiveByVehiclePlate(String plate) throws SQLException {
        String sql = "SELECT s.\"Id\", s.\"UserId\", s.\"VehicleId\", s.\"StartDate\", s.\"EndDate\", " +
                    "s.\"MonthlyPrice\", s.\"Status\", s.\"CreatedAt\", s.\"UpdatedAt\", " +
                    "u.\"FullName\" as user_full_name, v.\"Plate\" as vehicle_plate " +
                    "FROM public.\"Subscriptions\" s " +
                    "INNER JOIN public.\"Vehicles\" v ON s.\"VehicleId\" = v.\"Id\" " +
                    "INNER JOIN public.\"Users\" u ON s.\"UserId\" = u.\"Id\" " +
                    "WHERE v.\"Plate\" = ? AND s.\"Status\" = 'Active' " +
                    "AND CURRENT_TIMESTAMP BETWEEN s.\"StartDate\" AND s.\"EndDate\"";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, plate.toUpperCase());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSubscription(rs);
            }
        }
        return null;
    }
    
    /**
     * Busca una suscripción por ID de vehículo
     */
    public Subscription findActiveByVehicleId(Integer vehicleId) throws SQLException {
        String sql = "SELECT s.\"Id\", s.\"UserId\", s.\"VehicleId\", s.\"StartDate\", s.\"EndDate\", " +
                    "s.\"MonthlyPrice\", s.\"Status\", s.\"CreatedAt\", s.\"UpdatedAt\", " +
                    "u.\"FullName\" as user_full_name, v.\"Plate\" as vehicle_plate " +
                    "FROM public.\"Subscriptions\" s " +
                    "INNER JOIN public.\"Vehicles\" v ON s.\"VehicleId\" = v.\"Id\" " +
                    "INNER JOIN public.\"Users\" u ON s.\"UserId\" = u.\"Id\" " +
                    "WHERE s.\"VehicleId\" = ? AND s.\"Status\" = 'Active' " +
                    "AND CURRENT_TIMESTAMP BETWEEN s.\"StartDate\" AND s.\"EndDate\"";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSubscription(rs);
            }
        }
        return null;
    }
    
    /**
     * Mapea un ResultSet a un objeto Subscription
     */
    private Subscription mapResultSetToSubscription(ResultSet rs) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setId(rs.getInt("Id"));
        subscription.setUserId(rs.getInt("UserId"));
        subscription.setVehicleId(rs.getInt("VehicleId"));
        subscription.setMonthlyPrice(rs.getBigDecimal("MonthlyPrice"));
        subscription.setStatus(rs.getString("Status"));
        
        Timestamp startDate = rs.getTimestamp("StartDate");
        if (startDate != null) {
            subscription.setStartDate(startDate.toLocalDateTime());
        }
        
        Timestamp endDate = rs.getTimestamp("EndDate");
        if (endDate != null) {
            subscription.setEndDate(endDate.toLocalDateTime());
        }
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            subscription.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        if (updatedAt != null) {
            subscription.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // Campos adicionales del join
        subscription.setUserFullName(rs.getString("user_full_name"));
        subscription.setVehiclePlate(rs.getString("vehicle_plate"));
        
        return subscription;
    }
}
