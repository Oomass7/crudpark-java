package com.crudzaso.crudpark.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Utilidad para formateo de fechas y horas
 */
public class DateTimeFormatter {
    
    private static final java.time.format.DateTimeFormatter DATE_FORMAT = 
        java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private static final java.time.format.DateTimeFormatter TIME_FORMAT = 
        java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
    
    private static final java.time.format.DateTimeFormatter DISPLAY_FORMAT = 
        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    // Constructor privado para evitar instanciación
    private DateTimeFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Formatea una fecha y hora para mostrar
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DISPLAY_FORMAT);
    }
    
    /**
     * Formatea solo la fecha
     */
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_FORMAT);
    }
    
    /**
     * Formatea solo la hora
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(TIME_FORMAT);
    }
    
    /**
     * Calcula la diferencia en minutos entre dos fechas
     */
    public static long calculateMinutesBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(start, end);
    }
    
    /**
     * Formatea una duración en minutos a formato legible (Xh Ym)
     */
    public static String formatDuration(int totalMinutes) {
        if (totalMinutes < 60) {
            return totalMinutes + " min";
        }
        
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        
        if (minutes == 0) {
            return hours + " h";
        }
        
        return hours + " h " + minutes + " min";
    }
}
