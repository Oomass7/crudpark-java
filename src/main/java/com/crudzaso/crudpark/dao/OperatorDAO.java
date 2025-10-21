package com.crudzaso.crudpark.dao;

import com.crudzaso.crudpark.config.DatabaseConfig;
import com.crudzaso.crudpark.model.Operator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones con la tabla Operators
 */
public class OperatorDAO {
    private final DatabaseConfig dbConfig;
    
    public OperatorDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    /**
     * Busca un operador por su documento
     */
    public Operator findByDocument(String document) throws SQLException {
        String sql = "SELECT \"Id\", \"FullName\", \"Document\", \"Email\", \"Status\", \"IsActive\", \"CreatedAt\" " +
                    "FROM public.\"Operators\" WHERE \"Document\" = ? AND \"IsActive\" = true";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, document);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToOperator(rs);
            }
        }
        return null;
    }
    
    /**
     * Busca un operador por ID
     */
    public Operator findById(Integer id) throws SQLException {
        String sql = "SELECT \"Id\", \"FullName\", \"Document\", \"Email\", \"Status\", \"IsActive\", \"CreatedAt\" " +
                    "FROM public.\"Operators\" WHERE \"Id\" = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToOperator(rs);
            }
        }
        return null;
    }
    
    /**
     * Obtiene todos los operadores activos
     */
    public List<Operator> findAllActive() throws SQLException {
        List<Operator> operators = new ArrayList<>();
        String sql = "SELECT \"Id\", \"FullName\", \"Document\", \"Email\", \"Status\", \"IsActive\", \"CreatedAt\" " +
                    "FROM public.\"Operators\" WHERE \"IsActive\" = true ORDER BY \"FullName\"";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                operators.add(mapResultSetToOperator(rs));
            }
        }
        return operators;
    }
    
    /**
     * Mapea un ResultSet a un objeto Operator
     */
    private Operator mapResultSetToOperator(ResultSet rs) throws SQLException {
        Operator operator = new Operator();
        operator.setId(rs.getInt("Id"));
        operator.setFullName(rs.getString("FullName"));
        operator.setDocument(rs.getString("Document"));
        operator.setEmail(rs.getString("Email"));
        operator.setStatus(rs.getString("Status"));
        operator.setActive(rs.getBoolean("IsActive"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            operator.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return operator;
    }
}
