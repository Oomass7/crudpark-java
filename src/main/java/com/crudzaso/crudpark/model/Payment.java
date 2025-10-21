package com.crudzaso.crudpark.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo que representa un pago
 */
public class Payment {
    private Integer id;
    private Integer ticketId;
    private Integer subscriptionId;
    private Integer operatorId;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private String referenceNumber;
    private LocalDateTime createdAt;
    
    public Payment() {}
    
    public Payment(Integer ticketId, Integer operatorId, BigDecimal amount, String paymentMethod) {
        this.ticketId = ticketId;
        this.operatorId = operatorId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentTime = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }
    
    public Integer getSubscriptionId() {
        return subscriptionId;
    }
    
    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
    
    public Integer getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }
    
    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
