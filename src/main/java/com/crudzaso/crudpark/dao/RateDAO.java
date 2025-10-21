package com.crudzaso.crudpark.dao;

import com.crudzaso.crudpark.config.DatabaseConfig;
import com.crudzaso.crudpark.model.Rate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones con la tabla Rates
 */
public class RateDAO {
    private final DatabaseConfig dbConfig;
    
    public RateDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    /**
     * Obtiene todas las tarifas activas
     */
    public List<Rate> findAllActive() throws SQLException {
        List<Rate> rates = new ArrayList<>();
        String sql = "SELECT \"Id\", \"TypeVehicle\", \"HourPrice\", \"AddPrice\", \"MaxPrice\", " +
                    "\"GraceTime\", \"IsActive\", \"CreatedAt\", \"UpdatedAt\" " +
                    "FROM public.\"Rates\" WHERE \"IsActive\" = true ORDER BY \"TypeVehicle\"";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                rates.add(mapResultSetToRate(rs));
            }
        }
        return rates;
    }
    
    /**
     * Busca una tarifa por tipo de vehículo
     */
    public Rate findByVehicleType(String vehicleType) throws SQLException {
        String sql = "SELECT \"Id\", \"TypeVehicle\", \"HourPrice\", \"AddPrice\", \"MaxPrice\", " +
                    "\"GraceTime\", \"IsActive\", \"CreatedAt\", \"UpdatedAt\" " +
                    "FROM public.\"Rates\" WHERE \"TypeVehicle\" = ? AND \"IsActive\" = true";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vehicleType);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRate(rs);
            }
        }
        return null;
    }
    
    /**
     * Busca una tarifa por ID
     */
    public Rate findById(Integer id) throws SQLException {
        String sql = "SELECT \"Id\", \"TypeVehicle\", \"HourPrice\", \"AddPrice\", \"MaxPrice\", " +
                    "\"GraceTime\", \"IsActive\", \"CreatedAt\", \"UpdatedAt\" " +
                    "FROM public.\"Rates\" WHERE \"Id\" = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRate(rs);
            }
        }
        return null;
    }
    
    /**
     * Mapea un ResultSet a un objeto Rate
     */
    private Rate mapResultSetToRate(ResultSet rs) throws SQLException {
        Rate rate = new Rate();
        rate.setId(rs.getInt("Id"));
        rate.setTypeVehicle(rs.getString("TypeVehicle"));
        rate.setHourPrice(rs.getBigDecimal("HourPrice"));
        rate.setAddPrice(rs.getBigDecimal("AddPrice"));
        rate.setMaxPrice(rs.getBigDecimal("MaxPrice"));
        
        int graceTime = rs.getInt("GraceTime");
        if (!rs.wasNull()) {
            rate.setGraceTime(graceTime);
        }
        
        rate.setActive(rs.getBoolean("IsActive"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            rate.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        if (updatedAt != null) {
            rate.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return rate;
    }
}
