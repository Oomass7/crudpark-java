package com.crudzaso.crudpark.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo que representa una tarifa de parqueadero
 */
public class Rate {
    private Integer id;
    private String typeVehicle;
    private BigDecimal hourPrice;
    private BigDecimal addPrice;
    private BigDecimal maxPrice;
    private Integer graceTime;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Rate() {}
    
    public Rate(String typeVehicle, BigDecimal hourPrice, BigDecimal addPrice, 
                BigDecimal maxPrice, Integer graceTime) {
        this.typeVehicle = typeVehicle;
        this.hourPrice = hourPrice;
        this.addPrice = addPrice;
        this.maxPrice = maxPrice;
        this.graceTime = graceTime;
        this.isActive = true;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTypeVehicle() {
        return typeVehicle;
    }
    
    public void setTypeVehicle(String typeVehicle) {
        this.typeVehicle = typeVehicle;
    }
    
    public BigDecimal getHourPrice() {
        return hourPrice;
    }
    
    public void setHourPrice(BigDecimal hourPrice) {
        this.hourPrice = hourPrice;
    }
    
    public BigDecimal getAddPrice() {
        return addPrice;
    }
    
    public void setAddPrice(BigDecimal addPrice) {
        this.addPrice = addPrice;
    }
    
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public Integer getGraceTime() {
        return graceTime;
    }
    
    public void setGraceTime(Integer graceTime) {
        this.graceTime = graceTime;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return typeVehicle + " - $" + hourPrice + "/hora";
    }
}
