package com.Tisj.api.controllers;
import com.Tisj.api.requests.RequestMP;
import com.Tisj.bussines.entities.DT.DTCarrito;
import com.Tisj.services.CarritoService;
import com.Tisj.services.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.lang.System;


@Slf4j
@RestController
@RequestMapping("/api/paypal")
@RequiredArgsConstructor
public class PayPalController {
    private final PayPalService payPalService;

    @Autowired
    private CarritoService carritoService;

    @PostMapping("/crear-preferencia")
    public String crearPreferencia(@RequestBody RequestMP req) {
        try {
            return payPalService.crearOrdenPayPal(req);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear la preferencia de pago");
        }
    }

    @PostMapping("/capturar")
    public ResponseEntity<DTCarrito> capturarOrden(@RequestParam String orderId) {
        try {
            // 1. Obtener token de acceso
            String accessToken = payPalService.obtenerAccessToken();

            // 2. Capturar la orden
            String capturaJson = payPalService.capturarOrden(orderId, accessToken);

            // 3. Parsear la respuesta
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(capturaJson);

            String status = json.get("status").asText();
            if (!"COMPLETED".equals(status)) {
                System.out.println("La orden no fue completada: " + status);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // 4. Obtener reference_id: "usuarioId|carritoId"
            JsonNode purchaseUnit = json.get("purchase_units").get(0);
            String referenceId = purchaseUnit.get("reference_id").asText();

            String[] partes = referenceId.split("\\|");
            if (partes.length != 2) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            System.out.println("Valor para carritoId: '" + partes[1] + "'");
            Long carritoId = Long.valueOf(partes[1]);

            // 5. Desactivar carrito
            DTCarrito res = carritoService.desactivarCarrito(carritoId);

            System.out.println("Orden capturada con éxito. Artículos asignados al usuario ");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }



//    @PostMapping("/webhook")
//    public ResponseEntity<String> manejarWebhook(@RequestBody String payload, @RequestHeader("Paypal-Transmission-Sig") String sig) {
//        java.lang.System.out.println("Webhook recibido de PayPal:");
//        java.lang.System.out.println(payload);
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode json = mapper.readTree(payload);
//
//            String eventType = json.get("event_type").asText();
//
//            if (eventType.equals("PAYMENT.CAPTURE.COMPLETED")) {
//                String referenceId = json.get("resource")
//                        .get("supplementary_data")
//                        .get("related_ids")
//                        .get("order_id").asText();
//
//                // ⚠️ Separá usuario y carrito si los pusiste así
//                String[] partes = referenceId.split("\\|");
//                String usuarioId = partes[0];
//                String carritoId = partes[1];
//
//                carritoService.desactivarCarrito(Long.valueOf(carritoId));
//
//                System.out.println("Pago aprobado y artículos asignados a usuario " + usuarioId);
//            }
//
//            return ResponseEntity.ok("Webhook recibido");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error procesando webhook");
//        }
//    }
}

