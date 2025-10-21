package com.crudzaso.crudpark.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utilidad para formateo de moneda
 */
public class CurrencyFormatter {
    
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    
    /**
     * Formatea un BigDecimal como moneda colombiana
     */
    public static String format(BigDecimal amount) {
        if (amount == null) {
            return "$0";
        }
        return CURRENCY_FORMAT.format(amount);
    }
    
    /**
     * Formatea un double como moneda colombiana
     */
    public static String format(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }
    
    /**
     * Parsea un string de moneda a BigDecimal
     */
    public static BigDecimal parse(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        try {
            // Remover símbolos de moneda y espacios
            String cleaned = amountStr.replaceAll("[^0-9.,]", "");
            // Reemplazar coma por punto si es necesario
            cleaned = cleaned.replace(",", ".");
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
