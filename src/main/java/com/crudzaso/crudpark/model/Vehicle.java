package com.crudzaso.crudpark.model;

import java.time.LocalDateTime;

/**
 * Modelo que representa un vehículo
 */
public class Vehicle {
    private Integer id;
    private String plate;
    private String type;
    private Integer userId;
    private LocalDateTime createdAt;
    
    public Vehicle() {}
    
    public Vehicle(String plate, String type, Integer userId) {
        this.plate = plate;
        this.type = type;
        this.userId = userId;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getPlate() {
        return plate;
    }
    
    public void setPlate(String plate) {
        this.plate = plate;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return plate + " - " + type;
    }
}
