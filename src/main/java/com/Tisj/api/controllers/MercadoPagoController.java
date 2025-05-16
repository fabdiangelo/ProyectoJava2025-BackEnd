package com.Tisj.api.controllers;

import com.Tisj.services.MercadoPagoService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.lang.System;

@RestController
@RequestMapping("/api/mercado-pago")
//@CrossOrigin(origins = "*") // Ajustá el origen si querés restringirlo
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping("/crear-preferencia")
    public String crearPreferencia() {
        try {
            return mercadoPagoService.crearPreferencia();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear la preferencia de pago");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirWebhook(HttpServletRequest request) {
        try {
            String tipo = request.getParameter("type"); // ej: payment
            String idStr = request.getParameter("data.id"); // ID del pago
            System.out.println("Tipo de evento: " + tipo);
            System.out.println("ID recibido: " + idStr);

            // Validación: solo procesamos pagos
            if (tipo.equals("payment") && idStr != null) {
                Long paymentId = Long.valueOf(idStr);

                // Consultamos a Mercado Pago para verificar el estado
                MercadoPagoConfig.setAccessToken(System.getenv("MP_ACCESS_TOKEN"));
                PaymentClient client = new PaymentClient();
                Payment pago = client.get(paymentId);

                if ("approved".equals(pago.getStatus())) {
                    // Aquí podés guardar el pago en la base de datos, marcar un pedido como pagado, etc.
                    System.out.println("Pago aprobado: " + paymentId);
                } else {
                    System.out.println("Pago NO aprobado: " + pago.getStatus());
                }
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("ERROR");
        }
    }
}