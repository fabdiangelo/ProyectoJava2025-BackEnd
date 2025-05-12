package com.Tisj.api.controllers;
import com.Tisj.api.requests.RequestPago;
import com.Tisj.api.pojo.PayPal.Root;
import com.Tisj.services.PayPalService;
import com.Tisj.api.Paypal.AuthResponse;
import com.Tisj.api.Paypal.ClientTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class PayPalController {
    private final PayPalService payPalService;

    @PostMapping("/auth")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> authenticatePayPal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))
        ) {
            try {
                AuthResponse authResponse = payPalService.authenticate();
                return ResponseEntity.ok(authResponse);
            } catch (Exception e) {
                log.error("Error en la autenticación con PayPal: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error en la autenticación con PayPal: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tiene permisos para realizar esta operación");
        }
    }

    @PostMapping("/createorder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createOrder(@RequestBody RequestPago requestPago) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))
        ) {
            try {
                Root order = payPalService.createOrder(requestPago);
                return ResponseEntity.ok(order);
            } catch (Exception e) {
                log.error("Error al crear la orden: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al crear la orden: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tiene permisos para realizar esta operación");
        }
    }

    @PostMapping("/{orderId}/capture")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> captureOrder(@PathVariable String orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))
        ) {
            try {
                Root order = payPalService.captureOrder(orderId);
                return ResponseEntity.ok(order);
            } catch (Exception e) {
                log.error("Error al capturar la orden: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al capturar la orden: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tiene permisos para realizar esta operación");
        }
    }


    @GetMapping("/client-token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getClientToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))
        ) {
            try {
                ClientTokenResponse tokenResponse = payPalService.getClientToken();
                return ResponseEntity.ok(tokenResponse);
            } catch (Exception e) {
                log.error("Error al obtener el token del cliente: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al obtener el token del cliente: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tiene permisos para realizar esta operación");
        }
    }
}

