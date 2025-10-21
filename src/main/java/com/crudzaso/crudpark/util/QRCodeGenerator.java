package com.crudzaso.crudpark.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

/**
 * Utilidad para generar códigos QR
 */
public class QRCodeGenerator {
    
    /**
     * Genera un código QR como imagen BufferedImage
     * 
     * @param text Texto a codificar en el QR
     * @param width Ancho de la imagen
     * @param height Alto de la imagen
     * @return BufferedImage con el código QR
     */
    public static BufferedImage generateQRCode(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    /**
     * Genera el texto del QR según el formato especificado en los requisitos
     * Formato: TICKET:{id}|PLATE:{placa}|DATE:{timestamp}
     * 
     * @param ticketNumber Número de ticket
     * @param plate Placa del vehículo
     * @param timestamp Timestamp de entrada
     * @return String formateado para el QR
     */
    public static String generateQRText(String ticketNumber, String plate, long timestamp) {
        return String.format("TICKET:%s|PLATE:%s|DATE:%d", ticketNumber, plate, timestamp);
    }
}
