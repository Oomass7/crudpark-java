package com.crudzaso.crudpark.util;

/**
 * Utilidad para validaciones de datos
 */
public class Validator {
    
    /**
     * Valida que una placa no esté vacía y tenga un formato básico
     */
    public static boolean isValidPlate(String plate) {
        if (plate == null || plate.trim().isEmpty()) {
            return false;
        }
        
        // Validación básica: entre 4 y 10 caracteres alfanuméricos
        String cleanPlate = plate.trim().replaceAll("[^A-Za-z0-9]", "");
        return cleanPlate.length() >= 4 && cleanPlate.length() <= 10;
    }
    
    /**
     * Valida que un documento no esté vacío
     */
    public static boolean isValidDocument(String document) {
        if (document == null || document.trim().isEmpty()) {
            return false;
        }
        
        // Validación básica: solo números y longitud razonable
        String cleanDoc = document.trim().replaceAll("[^0-9]", "");
        return cleanDoc.length() >= 6 && cleanDoc.length() <= 15;
    }
    
    /**
     * Valida que un string no esté vacío
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Limpia y formatea una placa (mayúsculas, sin espacios)
     */
    public static String formatPlate(String plate) {
        if (plate == null) {
            return "";
        }
        return plate.trim().toUpperCase().replaceAll("\\s+", "");
    }
    
    /**
     * Limpia y formatea un documento (solo números)
     */
    public static String formatDocument(String document) {
        if (document == null) {
            return "";
        }
        return document.trim().replaceAll("[^0-9]", "");
    }
}
