package com.crudzaso.crudpark.service;

import com.crudzaso.crudpark.dao.OperatorDAO;
import com.crudzaso.crudpark.model.Operator;
import com.crudzaso.crudpark.util.Validator;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para autenticación y gestión de operadores
 */
public class AuthService {
    
    private final OperatorDAO operatorDAO;
    private Operator currentOperator;
    
    public AuthService() {
        this.operatorDAO = new OperatorDAO();
    }
    
    /**
     * Autentica un operador por documento
     * 
     * @param document Documento del operador
     * @return Operator si la autenticación es exitosa
     * @throws Exception Si hay algún error
     */
    public Operator login(String document) throws Exception {
        // Validar documento
        if (!Validator.isValidDocument(document)) {
            throw new IllegalArgumentException("Documento inválido. Debe contener entre 6 y 15 dígitos.");
        }
        
        String formattedDocument = Validator.formatDocument(document);
        
        // Buscar operador
        Operator operator = operatorDAO.findByDocument(formattedDocument);
        
        if (operator == null) {
            throw new IllegalArgumentException("No se encontró un operador con el documento " + formattedDocument);
        }
        
        if (!operator.isActive()) {
            throw new IllegalStateException("El operador está inactivo. Contacte al administrador.");
        }
        
        // Guardar operador actual
        currentOperator = operator;
        
        return operator;
    }
    
    /**
     * Obtiene el operador actualmente autenticado
     */
    public Operator getCurrentOperator() {
        return currentOperator;
    }
    
    /**
     * Cierra la sesión del operador actual
     */
    public void logout() {
        currentOperator = null;
    }
    
    /**
     * Verifica si hay un operador autenticado
     */
    public boolean isAuthenticated() {
        return currentOperator != null;
    }
    
    /**
     * Obtiene todos los operadores activos (para selección)
     */
    public List<Operator> getAllActiveOperators() throws SQLException {
        return operatorDAO.findAllActive();
    }
}
