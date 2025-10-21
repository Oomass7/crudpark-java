package com.crudzaso.crudpark.dao;

import com.crudzaso.crudpark.config.DatabaseConfig;
import com.crudzaso.crudpark.model.Vehicle;

import java.sql.*;

/**
 * DAO para operaciones con la tabla Vehicles
 */
public class VehicleDAO {
    private final DatabaseConfig dbConfig;
    
    public VehicleDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    /**
     * Busca un vehículo por placa
     */
    public Vehicle findByPlate(String plate) throws SQLException {
        String sql = "SELECT \"Id\", \"Plate\", \"Type\", \"UserId\", \"CreatedAt\" " +
                    "FROM public.\"Vehicles\" WHERE \"Plate\" = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, plate.toUpperCase());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVehicle(rs);
            }
        }
        return null;
    }
    
    /**
     * Crea un nuevo vehículo (para invitados)
     */
    public Vehicle create(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO public.\"Vehicles\" (\"Plate\", \"Type\", \"UserId\", \"CreatedAt\") " +
                    "VALUES (?, ?, ?, CURRENT_TIMESTAMP) RETURNING \"Id\"";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vehicle.getPlate().toUpperCase());
            stmt.setString(2, vehicle.getType());
            
            if (vehicle.getUserId() != null) {
                stmt.setInt(3, vehicle.getUserId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                vehicle.setId(rs.getInt("Id"));
                return vehicle;
            }
        }
        return null;
    }
    
    /**
     * Mapea un ResultSet a un objeto Vehicle
     */
    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getInt("Id"));
        vehicle.setPlate(rs.getString("Plate"));
        vehicle.setType(rs.getString("Type"));
        
        int userId = rs.getInt("UserId");
        if (!rs.wasNull()) {
            vehicle.setUserId(userId);
        }
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            vehicle.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return vehicle;
    }
}
