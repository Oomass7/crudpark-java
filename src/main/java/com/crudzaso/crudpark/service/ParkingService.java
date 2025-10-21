package com.crudzaso.crudpark.service;

import com.crudzaso.crudpark.dao.*;
import com.crudzaso.crudpark.model.*;
import com.crudzaso.crudpark.util.QRCodeGenerator;
import com.crudzaso.crudpark.util.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Servicio principal para la lógica de negocio del parqueadero
 */
public class ParkingService {
    
    private final TicketDAO ticketDAO;
    private final VehicleDAO vehicleDAO;
    private final SubscriptionDAO subscriptionDAO;
    private final RateDAO rateDAO;
    private final PaymentDAO paymentDAO;
    
    public ParkingService() {
        this.ticketDAO = new TicketDAO();
        this.vehicleDAO = new VehicleDAO();
        this.subscriptionDAO = new SubscriptionDAO();
        this.rateDAO = new RateDAO();
        this.paymentDAO = new PaymentDAO();
    }
    
    /**
     * Registra el ingreso de un vehículo
     * 
     * @param plate Placa del vehículo
     * @param vehicleType Tipo de vehículo
     * @param operatorId ID del operador
     * @return Ticket creado
     * @throws Exception Si hay algún error en el proceso
     */
    public Ticket registerEntry(String plate, String vehicleType, Integer operatorId) throws Exception {
        // Validar placa
        if (!Validator.isValidPlate(plate)) {
            throw new IllegalArgumentException("Placa inválida. Debe tener entre 4 y 10 caracteres alfanuméricos.");
        }
        
        String formattedPlate = Validator.formatPlate(plate);
        
        // Verificar si ya hay un ticket abierto para esta placa
        Ticket openTicket = ticketDAO.findOpenByPlate(formattedPlate);
        if (openTicket != null) {
            throw new IllegalStateException("El vehículo con placa " + formattedPlate + 
                " ya tiene un ticket abierto (Ticket #" + openTicket.getTicketNumber() + ")");
        }
        
        // Buscar o crear el vehículo
        Vehicle vehicle = vehicleDAO.findByPlate(formattedPlate);
        if (vehicle == null) {
            // Crear vehículo nuevo (invitado)
            vehicle = new Vehicle(formattedPlate, vehicleType, null);
            vehicle = vehicleDAO.create(vehicle);
            
            if (vehicle == null) {
                throw new SQLException("Error al crear el vehículo");
            }
        }
        
        // Verificar si tiene mensualidad vigente
        Subscription subscription = subscriptionDAO.findActiveByVehicleId(vehicle.getId());
        
        // Obtener la tarifa según el tipo de vehículo
        Rate rate = rateDAO.findByVehicleType(vehicleType);
        if (rate == null) {
            // Si no hay tarifa específica, usar la primera activa
            List<Rate> rates = rateDAO.findAllActive();
            if (rates.isEmpty()) {
                throw new IllegalStateException("No hay tarifas configuradas en el sistema");
            }
            rate = rates.get(0);
        }
        
        // Generar número de ticket
        String ticketNumber = generateTicketNumber();
        
        // Crear el ticket
        Ticket ticket = new Ticket();
        ticket.setTicketNumber(ticketNumber);
        ticket.setVehicleId(vehicle.getId());
        ticket.setOperatorId(operatorId);
        ticket.setRateId(rate.getId());
        ticket.setEntryTime(LocalDateTime.now());
        ticket.setTotalAmount(BigDecimal.ZERO);
        ticket.setTotalMinutes(0);
        
        if (subscription != null) {
            ticket.setSubscriptionId(subscription.getId());
        }
        
        // Generar código QR
        long timestamp = ticket.getEntryTime().toEpochSecond(ZoneOffset.UTC);
        String qrText = QRCodeGenerator.generateQRText(ticketNumber, formattedPlate, timestamp);
        ticket.setQrCode(qrText);
        
        // Guardar el ticket
        ticket = ticketDAO.create(ticket);
        
        if (ticket == null) {
            throw new SQLException("Error al crear el ticket");
        }
        
        // Agregar información adicional para mostrar
        ticket.setVehiclePlate(formattedPlate);
        ticket.setVehicleType(vehicleType);
        ticket.setHasSubscription(subscription != null);
        
        return ticket;
    }
    
    /**
     * Registra la salida de un vehículo y calcula el cobro
     * 
     * @param plate Placa del vehículo
     * @return Ticket actualizado con el monto a pagar
     * @throws Exception Si hay algún error en el proceso
     */
    public Ticket registerExit(String plate) throws Exception {
        String formattedPlate = Validator.formatPlate(plate);
        
        // Buscar ticket abierto
        Ticket ticket = ticketDAO.findOpenByPlate(formattedPlate);
        if (ticket == null) {
            throw new IllegalStateException("No se encontró un ticket abierto para la placa " + formattedPlate);
        }
        
        // Calcular tiempo de estadía
        LocalDateTime exitTime = LocalDateTime.now();
        long totalMinutes = java.time.Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        
        // Calcular monto a pagar
        BigDecimal amount = BigDecimal.ZERO;
        
        // Si tiene mensualidad, no se cobra
        if (ticket.getSubscriptionId() == null) {
            // Obtener la tarifa
            Rate rate = rateDAO.findById(ticket.getRateId());
            if (rate != null) {
                amount = calculateAmount(totalMinutes, rate);
            }
        }
        
        // Actualizar el ticket
        ticketDAO.updateExit(ticket.getId(), (int) totalMinutes, amount);
        
        // Actualizar el objeto ticket
        ticket.setExitTime(exitTime);
        ticket.setTotalMinutes((int) totalMinutes);
        ticket.setTotalAmount(amount);
        
        return ticket;
    }
    
    /**
     * Registra un pago
     * 
     * @param ticketId ID del ticket
     * @param paymentMethod Método de pago (CASH, CARD, TRANSFER)
     * @param operatorId ID del operador
     * @return Payment creado
     * @throws Exception Si hay algún error
     */
    public Payment registerPayment(Integer ticketId, String paymentMethod, Integer operatorId) throws Exception {
        // Buscar el ticket por ID
        Ticket ticket = ticketDAO.findById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket no encontrado con ID: " + ticketId);
        }
        
        if (ticket.getTotalAmount() == null || ticket.getTotalAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("Este ticket no requiere pago");
        }
        
        // Crear el pago
        Payment payment = new Payment();
        payment.setTicketId(ticketId);
        payment.setOperatorId(operatorId);
        payment.setAmount(ticket.getTotalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentTime(LocalDateTime.now());
        
        payment = paymentDAO.create(payment);
        
        if (payment == null) {
            throw new SQLException("Error al registrar el pago");
        }
        
        return payment;
    }
    
    /**
     * Calcula el monto a cobrar según la tarifa y el tiempo
     * Aplica la regla de tiempo de gracia de 30 minutos
     */
    private BigDecimal calculateAmount(long totalMinutes, Rate rate) {
        // Tiempo de gracia (por defecto 30 minutos)
        int graceTime = rate.getGraceTime() != null ? rate.getGraceTime() : 30;
        
        // Si está dentro del tiempo de gracia, no se cobra
        if (totalMinutes <= graceTime) {
            return BigDecimal.ZERO;
        }
        
        // Calcular minutos a cobrar (descontando tiempo de gracia)
        long chargeableMinutes = totalMinutes - graceTime;
        
        // Calcular horas completas y minutos adicionales
        long hours = chargeableMinutes / 60;
        long additionalMinutes = chargeableMinutes % 60;
        
        // Calcular monto
        BigDecimal amount = BigDecimal.ZERO;
        
        // Cobrar horas completas
        if (hours > 0) {
            amount = rate.getHourPrice().multiply(BigDecimal.valueOf(hours));
        }
        
        // Cobrar fracción adicional si hay minutos extra
        if (additionalMinutes > 0 && rate.getAddPrice() != null) {
            amount = amount.add(rate.getAddPrice());
        }
        
        // Aplicar tope diario si existe
        if (rate.getMaxPrice() != null && amount.compareTo(rate.getMaxPrice()) > 0) {
            amount = rate.getMaxPrice();
        }
        
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Genera un número de ticket único
     */
    private String generateTicketNumber() {
        // Formato: YYYYMMDDHHMMSS + random
        LocalDateTime now = LocalDateTime.now();
        String timestamp = String.format("%04d%02d%02d%02d%02d%02d",
            now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
            now.getHour(), now.getMinute(), now.getSecond());
        
        int random = (int) (Math.random() * 1000);
        return timestamp + String.format("%03d", random);
    }
    
    /**
     * Obtiene todos los tickets abiertos
     */
    public List<Ticket> getOpenTickets() throws SQLException {
        return ticketDAO.findAllOpen();
    }
    
    /**
     * Busca un ticket por número
     */
    public Ticket findTicketByNumber(String ticketNumber) throws SQLException {
        return ticketDAO.findByTicketNumber(ticketNumber);
    }
}
