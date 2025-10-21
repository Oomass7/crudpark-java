package com.crudzaso.crudpark.model;

import java.time.LocalDateTime;

/**
 * Modelo que representa un operador del parqueadero
 */
public class Operator {
    private Integer id;
    private String fullName;
    private String document;
    private String email;
    private String status;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    public Operator() {}
    
    public Operator(Integer id, String fullName, String document, String email, 
                   String status, boolean isActive) {
        this.id = id;
        this.fullName = fullName;
        this.document = document;
        this.email = email;
        this.status = status;
        this.isActive = isActive;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getDocument() {
        return document;
    }
    
    public void setDocument(String document) {
        this.document = document;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
    
    @Override
    public String toString() {
        return fullName + " (" + document + ")";
    }
}
