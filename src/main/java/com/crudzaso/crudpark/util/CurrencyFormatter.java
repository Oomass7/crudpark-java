package com.crudzaso.crudpark.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utilidad para formateo de moneda
 */
public class CurrencyFormatter {
    
    // Constructor privado para evitar instanciación
    private CurrencyFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Formatea un BigDecimal como moneda colombiana
     */
    public static String format(BigDecimal amount) {
        if (amount == null) {
            return "$0";
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("es").setRegion("CO").build());
        return currencyFormat.format(amount);
    }
    
    /**
     * Formatea un double como moneda colombiana
     */
    public static String format(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("es").setRegion("CO").build());
        return currencyFormat.format(amount);
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
