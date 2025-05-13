package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Pago;
import com.Tisj.services.PagoService;
import com.Tisj.bussines.entities.DT.DTFactura;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pagos")
@PreAuthorize("hasAuthority('ADMIN')")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> getPagos() {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        List<Pago> pagos = pagoService.getAllPagos();
        return new ResponseEntity<>(pagos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getPago(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        Pago pago = pagoService.getPagoById(id);
        if (pago != null) {
            return new ResponseEntity<>(pago, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<Pago> createPago(@RequestBody Pago pago) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        Pago nuevoPago = pagoService.createPago(pago);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //TESTING Mapping
    @PutMapping("/{id}")
    public ResponseEntity<Pago> updatePago(@PathVariable Long id, @RequestBody Pago pago) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        Pago pagoActualizado = pagoService.updatePago(id, pago);
        if (pagoActualizado != null) {
            return new ResponseEntity<>(pagoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/usuario/{usuarioEmail}")
    public ResponseEntity<List<Pago>> getPagosByUsuarioEmail(@PathVariable String usuarioEmail) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            List<Pago> pagos = pagoService.getPagosByUsuarioEmail(usuarioEmail);
            return new ResponseEntity<>(pagos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        pagoService.deletePago(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/paypal/{orderId}/capture")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> procesarPagoPayPal(@PathVariable String orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))
        ) {
            try {
                log.info("Procesando pago de PayPal para orden: {}", orderId);
                DTFactura factura = pagoService.capturarOrdenPayPal(orderId);
                if (factura != null) {
                    log.info("Pago procesado exitosamente para orden: {}. Monto: {}, Fecha: {}", 
                        orderId, 
                        factura.getMonto(),
                        factura.getFecha());
                    return ResponseEntity.ok(factura);
                } else {
                    log.error("No se pudo procesar el pago para la orden: {}", orderId);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("No se pudo procesar el pago");
                }
            } catch (Exception e) {
                log.error("Error al procesar el pago de PayPal: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al procesar el pago: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tiene permisos para realizar esta operación");
        }
    }

    @PostMapping("/paypal/webhook")
    public ResponseEntity<?> webhookPayPal(@RequestBody String payload) {
        try {
            log.info("Recibido webhook de PayPal: {}", payload);
            // TODO: Implementar procesamiento del webhook
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al procesar webhook de PayPal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar webhook");
        }
    }
}
