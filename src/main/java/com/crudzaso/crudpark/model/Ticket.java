package com.crudzaso.crudpark.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo que representa un ticket de parqueadero
 */
public class Ticket {
    private Integer id;
    private String ticketNumber;
    private Integer vehicleId;
    private Integer operatorId;
    private Integer subscriptionId;
    private Integer rateId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private BigDecimal totalAmount;
    private Integer totalMinutes;
    private String qrCode;
    private LocalDateTime createdAt;
    
    // Campos adicionales para joins
    private String vehiclePlate;
    private String vehicleType;
    private String operatorName;
    private boolean hasSubscription;
    
    public Ticket() {}
    
    public Ticket(String ticketNumber, Integer vehicleId, Integer operatorId, 
                  Integer subscriptionId, Integer rateId) {
        this.ticketNumber = ticketNumber;
        this.vehicleId = vehicleId;
        this.operatorId = operatorId;
        this.subscriptionId = subscriptionId;
        this.rateId = rateId;
        this.entryTime = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
        this.totalMinutes = 0;
    }
    
    /**
     * Verifica si el ticket está abierto (vehículo dentro del parqueadero)
     */
    public boolean isOpen() {
        return exitTime == null;
    }
    
    /**
     * Determina el tipo de ticket basado en si tiene suscripción
     */
    public String getTicketType() {
        return subscriptionId != null ? "Mensualidad" : "Invitado";
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTicketNumber() {
        return ticketNumber;
    }
    
    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
    
    public Integer getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public Integer getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
    
    public Integer getSubscriptionId() {
        return subscriptionId;
    }
    
    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
    
    public Integer getRateId() {
        return rateId;
    }
    
    public void setRateId(Integer rateId) {
        this.rateId = rateId;
    }
    
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    
    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
    
    public LocalDateTime getExitTime() {
        return exitTime;
    }
    
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public Integer getTotalMinutes() {
        return totalMinutes;
    }
    
    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }
    
    public String getQrCode() {
        return qrCode;
    }
    
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getVehiclePlate() {
        return vehiclePlate;
    }
    
    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public String getOperatorName() {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    
    public boolean isHasSubscription() {
        return hasSubscription;
    }
    
    public void setHasSubscription(boolean hasSubscription) {
        this.hasSubscription = hasSubscription;
    }
}
