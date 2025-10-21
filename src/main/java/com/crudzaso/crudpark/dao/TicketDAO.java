package com.crudzaso.crudpark.dao;

import com.crudzaso.crudpark.config.DatabaseConfig;
import com.crudzaso.crudpark.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones con la tabla Tickets
 */
public class TicketDAO {
    private final DatabaseConfig dbConfig;
    
    public TicketDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    /**
     * Crea un nuevo ticket
     */
    public Ticket create(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO public.\"Tickets\" (\"TicketNumber\", \"VehicleId\", \"OperatorId\", " +
                    "\"SubscriptionId\", \"RateId\", \"EntryTime\", \"TotalAmount\", \"TotalMinutes\", " +
                    "\"QRCode\", \"CreatedAt\") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING \"Id\"";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ticket.getTicketNumber());
            stmt.setInt(2, ticket.getVehicleId());
            
            if (ticket.getOperatorId() != null) {
                stmt.setInt(3, ticket.getOperatorId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            
            if (ticket.getSubscriptionId() != null) {
                stmt.setInt(4, ticket.getSubscriptionId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            if (ticket.getRateId() != null) {
                stmt.setInt(5, ticket.getRateId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setTimestamp(6, Timestamp.valueOf(ticket.getEntryTime()));
            stmt.setBigDecimal(7, ticket.getTotalAmount());
            stmt.setInt(8, ticket.getTotalMinutes());
            stmt.setString(9, ticket.getQrCode());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ticket.setId(rs.getInt("Id"));
                return ticket;
            }
        }
        return null;
    }
    
    /**
     * Busca un ticket abierto por placa de vehículo
     */
    public Ticket findOpenByPlate(String plate) throws SQLException {
        String sql = "SELECT t.\"Id\", t.\"TicketNumber\", t.\"VehicleId\", t.\"OperatorId\", " +
                    "t.\"SubscriptionId\", t.\"RateId\", t.\"EntryTime\", t.\"ExitTime\", " +
                    "t.\"TotalAmount\", t.\"TotalMinutes\", t.\"QRCode\", t.\"CreatedAt\", " +
                    "v.\"Plate\" as vehicle_plate, v.\"Type\" as vehicle_type, " +
                    "o.\"FullName\" as operator_name " +
                    "FROM public.\"Tickets\" t " +
                    "INNER JOIN public.\"Vehicles\" v ON t.\"VehicleId\" = v.\"Id\" " +
                    "LEFT JOIN public.\"Operators\" o ON t.\"OperatorId\" = o.\"Id\" " +
                    "WHERE v.\"Plate\" = ? AND t.\"ExitTime\" IS NULL " +
                    "ORDER BY t.\"EntryTime\" DESC LIMIT 1";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, plate.toUpperCase());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTicket(rs);
            }
        }
        return null;
    }
    
    /**
     * Actualiza la salida de un ticket
     */
    public void updateExit(Integer ticketId, Integer totalMinutes, java.math.BigDecimal totalAmount) throws SQLException {
        String sql = "UPDATE public.\"Tickets\" SET \"ExitTime\" = CURRENT_TIMESTAMP, " +
                    "\"TotalMinutes\" = ?, \"TotalAmount\" = ? WHERE \"Id\" = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, totalMinutes);
            stmt.setBigDecimal(2, totalAmount);
            stmt.setInt(3, ticketId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Obtiene todos los tickets abiertos
     */
    public List<Ticket> findAllOpen() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.\"Id\", t.\"TicketNumber\", t.\"VehicleId\", t.\"OperatorId\", " +
                    "t.\"SubscriptionId\", t.\"RateId\", t.\"EntryTime\", t.\"ExitTime\", " +
                    "t.\"TotalAmount\", t.\"TotalMinutes\", t.\"QRCode\", t.\"CreatedAt\", " +
                    "v.\"Plate\" as vehicle_plate, v.\"Type\" as vehicle_type, " +
                    "o.\"FullName\" as operator_name " +
                    "FROM public.\"Tickets\" t " +
                    "INNER JOIN public.\"Vehicles\" v ON t.\"VehicleId\" = v.\"Id\" " +
                    "LEFT JOIN public.\"Operators\" o ON t.\"OperatorId\" = o.\"Id\" " +
                    "WHERE t.\"ExitTime\" IS NULL " +
                    "ORDER BY t.\"EntryTime\" DESC";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }
    
    /**
     * Busca un ticket por número
     */
    public Ticket findByTicketNumber(String ticketNumber) throws SQLException {
        String sql = "SELECT t.\"Id\", t.\"TicketNumber\", t.\"VehicleId\", t.\"OperatorId\", " +
                    "t.\"SubscriptionId\", t.\"RateId\", t.\"EntryTime\", t.\"ExitTime\", " +
                    "t.\"TotalAmount\", t.\"TotalMinutes\", t.\"QRCode\", t.\"CreatedAt\", " +
                    "v.\"Plate\" as vehicle_plate, v.\"Type\" as vehicle_type, " +
                    "o.\"FullName\" as operator_name " +
                    "FROM public.\"Tickets\" t " +
                    "INNER JOIN public.\"Vehicles\" v ON t.\"VehicleId\" = v.\"Id\" " +
                    "LEFT JOIN public.\"Operators\" o ON t.\"OperatorId\" = o.\"Id\" " +
                    "WHERE t.\"TicketNumber\" = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ticketNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTicket(rs);
            }
        }
        return null;
    }
    
    /**
     * Mapea un ResultSet a un objeto Ticket
     */
    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("Id"));
        ticket.setTicketNumber(rs.getString("TicketNumber"));
        ticket.setVehicleId(rs.getInt("VehicleId"));
        
        int operatorId = rs.getInt("OperatorId");
        if (!rs.wasNull()) {
            ticket.setOperatorId(operatorId);
        }
        
        int subscriptionId = rs.getInt("SubscriptionId");
        if (!rs.wasNull()) {
            ticket.setSubscriptionId(subscriptionId);
            ticket.setHasSubscription(true);
        }
        
        int rateId = rs.getInt("RateId");
        if (!rs.wasNull()) {
            ticket.setRateId(rateId);
        }
        
        Timestamp entryTime = rs.getTimestamp("EntryTime");
        if (entryTime != null) {
            ticket.setEntryTime(entryTime.toLocalDateTime());
        }
        
        Timestamp exitTime = rs.getTimestamp("ExitTime");
        if (exitTime != null) {
            ticket.setExitTime(exitTime.toLocalDateTime());
        }
        
        ticket.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        
        int totalMinutes = rs.getInt("TotalMinutes");
        if (!rs.wasNull()) {
            ticket.setTotalMinutes(totalMinutes);
        }
        
        ticket.setQrCode(rs.getString("QRCode"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            ticket.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        // Campos adicionales del join
        ticket.setVehiclePlate(rs.getString("vehicle_plate"));
        ticket.setVehicleType(rs.getString("vehicle_type"));
        ticket.setOperatorName(rs.getString("operator_name"));
        
        return ticket;
    }
}
