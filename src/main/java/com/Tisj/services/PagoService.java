package com.Tisj.services;

import com.Tisj.bussines.entities.Pago;
import com.Tisj.bussines.repositories.PagoRepository;
import com.Tisj.api.pojo.PayPal.Root;
import com.Tisj.bussines.entities.DT.DTFactura;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.bussines.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.time.LocalDate;

@Slf4j
@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PayPalService payPalService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Pago> getAllPagos() {
        return pagoRepository.findAll(); // Obtener todos los pagos
    }

    public Pago getPagoById(Long id) {
        return pagoRepository.findById(id).orElse(null); // Obtener un pago por su ID
    }

    public Pago createPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Pago updatePago(Long id, Pago pago) {
        if (pagoRepository.existsById(id)) {
            pago.setId(id);
            return pagoRepository.save(pago); // Actualizar un pago existente
        }
        return null;
    }

    public void deletePago(Long id) {
        if (pagoRepository.existsById(id)) {
            pagoRepository.deleteById(id); // Eliminar un pago por su ID
        }
    }

    public List<Pago> getPagosByUsuarioEmail(String usuarioEmail) {
        return pagoRepository.findByUsuarioEmail(usuarioEmail); // Obtener pagos por email de usuario
    }

    public DTFactura capturarOrdenPayPal(String orderId) {
        try {
            log.info("Iniciando captura de orden PayPal: {}", orderId);
            
            // Primero obtenemos la orden para verificar su estado
            Root order = payPalService.getOrder(orderId);
            if (order == null) {
                log.error("No se encontró la orden PayPal: {}", orderId);
                throw new RuntimeException("No se encontró la orden");
            }

            // Si la orden existe, procedemos a capturarla
            Root capturedOrder = payPalService.captureOrder(orderId);
            if (capturedOrder == null) {
                log.error("Error al capturar la orden PayPal: {}", orderId);
                throw new RuntimeException("Error al capturar la orden");
            }

            if (!"COMPLETED".equals(capturedOrder.getStatus())) {
                log.error("La orden PayPal no está completada. Estado: {}", capturedOrder.getStatus());
                throw new RuntimeException("La orden no está completada");
            }

            // Validar que existan los datos necesarios
            if (capturedOrder.getPayer() == null || 
                capturedOrder.getPayer().getName() == null || 
                capturedOrder.getPurchase_units() == null || 
                capturedOrder.getPurchase_units().isEmpty() ||
                capturedOrder.getPurchase_units().get(0).getAmount() == null) {
                log.error("Datos incompletos en la respuesta de PayPal para orden: {}", orderId);
                throw new RuntimeException("Datos incompletos en la respuesta de PayPal");
            }

            String nombre = capturedOrder.getPayer().getName().getGiven_name();
            String apellido = capturedOrder.getPayer().getName().getSurname();
            String email = capturedOrder.getPayer().getEmail_address();
            String montoStr = capturedOrder.getPurchase_units().get(0).getAmount().getValue();
            String moneda = capturedOrder.getPurchase_units().get(0).getAmount().getCurrency_code();

            // Validar que los datos no sean nulos
            if (nombre == null || apellido == null || email == null || montoStr == null || moneda == null) {
                log.error("Datos nulos en la respuesta de PayPal para orden: {}", orderId);
                throw new RuntimeException("Datos incompletos en la respuesta de PayPal");
            }

            // Buscar el usuario por email
            Usuario usuario = usuarioRepository.findById(email).orElse(null);
            if (usuario == null) {
                log.error("No se encontró el usuario con email: {}", email);
                throw new RuntimeException("Usuario no encontrado");
            }

            // Crear el pago
            Pago pago = new Pago(
                usuario,
                Float.parseFloat(montoStr),
                "PAYPAL",
                orderId
            );
            pago = pagoRepository.save(pago);

            log.info("Pago creado exitosamente para orden PayPal: {}", orderId);

            // Crear la factura con los datos del pago
            DTFactura factura = new DTFactura(
                pago.getId(),
                nombre + " " + apellido,
                email,
                LocalDate.now(),
                Float.parseFloat(montoStr)
            );

            log.info("Factura generada: ID={}, Monto={} {}, Fecha={}", 
                factura.getId(), 
                factura.getMonto(), 
                moneda,
                factura.getFecha());

            return factura;
        } catch (Exception e) {
            log.error("Error al capturar el pago de PayPal: {}", e.getMessage());
            throw new RuntimeException("Error al capturar el pago: " + e.getMessage());
        }
    }
}