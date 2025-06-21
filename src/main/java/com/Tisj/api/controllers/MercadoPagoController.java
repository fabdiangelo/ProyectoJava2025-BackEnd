package com.Tisj.api.controllers;

import com.Tisj.api.requests.RequestMP;
import com.Tisj.bussines.entities.DT.DTArtCarrito;
import com.Tisj.bussines.entities.DT.DTCarrito;
import com.Tisj.bussines.entities.Pago;
import com.Tisj.services.CarritoService;
import com.Tisj.services.MercadoPagoService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.net.MPRequest;
import com.mercadopago.resources.payment.Payment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.lang.System;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mercado-pago")
//@CrossOrigin(origins = "*") // Ajustá el origen si querés restringirlo
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private CarritoService carritoService;

    @PostMapping("/crear-preferencia")
    public String crearPreferencia(@RequestBody RequestMP productos) {
        try {
            return mercadoPagoService.crearPreferencia(productos);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear la preferencia de pago");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirWebhook(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("Payload recibido: " + payload);

            String tipo = (String) payload.get("type");
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            String idStr = String.valueOf(data.get("id"));

            System.out.println("Tipo de evento: " + tipo);
            System.out.println("ID recibido: " + idStr);

            if ("payment".equals(tipo) && idStr != null) {
                Long paymentId = Long.valueOf(idStr);
                System.out.println("paymentId=" + paymentId);
                // Consultar a la API de Mercado Pago
                MercadoPagoConfig.setAccessToken(System.getenv("MP_ACCESS_TOKEN"));
                PaymentClient client = new PaymentClient();
                Payment pago = client.get(paymentId);

                System.out.println("Estado del pago: " + pago.getStatus());

                if ("approved".equals(pago.getStatus())) {
                    String externalReference = pago.getExternalReference(); // e.g. "123|456"
                    String[] partes = externalReference.split("\\|");
                    Long usuarioId = Long.valueOf(partes[0]);
                    Long carritoId = Long.valueOf(partes[1]);

                    // asignar los artículos de ese carrito al usuario
                    DTCarrito dt = carritoService.desactivarCarrito(carritoId);
                    System.out.println(dt);
                    System.out.println("Pago aprobado y artículos asignados a usuario " + usuarioId);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Compra realizada");
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