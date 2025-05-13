package com.Tisj.services;

import com.Tisj.api.pojo.PayPal.*;
import com.Tisj.api.requests.RequestPago;
import com.Tisj.api.config.PayPalConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import java.util.Collections;
import java.math.BigDecimal;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PayPalService {
    private final RestTemplate restTemplate;
    private final PayPalConfig payPalConfig;
    private static final Logger log = LoggerFactory.getLogger(PayPalService.class);

    public AuthResponse authenticate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(payPalConfig.getClientId(), payPalConfig.getClientSecret());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
            payPalConfig.getAuthUrl(), 
            request, 
            AuthResponse.class
        );

        return response.getBody();
    }

    public ClientTokenResponse getClientToken() {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<ClientTokenResponse> response = restTemplate.postForEntity(
            payPalConfig.getClientTokenUrl(),
            request,
            ClientTokenResponse.class
        );

        return response.getBody();
    }

    public Root createOrder(RequestPago requestPago) {
        // Validar datos de entrada
        if (requestPago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que 0");
        }
        if (requestPago.getMoneda() == null || requestPago.getMoneda().length() != 3) {
            throw new IllegalArgumentException("La moneda debe ser un código válido de 3 caracteres (ej: USD, EUR)");
        }
        if (requestPago.getReturnUrl() == null || !requestPago.getReturnUrl().startsWith("http")) {
            throw new IllegalArgumentException("URL de retorno inválida");
        }
        if (requestPago.getCancelUrl() == null || !requestPago.getCancelUrl().startsWith("http")) {
            throw new IllegalArgumentException("URL de cancelación inválida");
        }

        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Root order = new Root();
        order.setIntent("CAPTURE");
        
        PurchaseUnit purchaseUnit = new PurchaseUnit();
        Amount amount = new Amount();
        amount.setCurrency_code(requestPago.getMoneda());
        amount.setValue(requestPago.getMonto().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        
        purchaseUnit.setAmount(amount);
        purchaseUnit.setDescription(requestPago.getDescripcion());
        
        // Configurar URLs de retorno y cancelación
        Root.ApplicationContext applicationContext = new Root.ApplicationContext();
        applicationContext.setReturn_url(requestPago.getReturnUrl());
        applicationContext.setCancel_url(requestPago.getCancelUrl());
        applicationContext.setBrand_name("Solariano");
        applicationContext.setLanding_page("LOGIN");
        applicationContext.setUser_action("PAY_NOW");

        order.setApplication_context(applicationContext);
        order.setPurchase_units(Collections.singletonList(purchaseUnit));

        HttpEntity<Root> request = new HttpEntity<>(order, headers);
        ResponseEntity<Root> response = restTemplate.postForEntity(
            payPalConfig.getOrdersUrl(),
            request,
            Root.class
        );

        return response.getBody();
    }

    public Root captureOrder(String orderId) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Root> response = restTemplate.postForEntity(
            payPalConfig.getCaptureUrl(orderId),
            request,
            Root.class
        );

        return response.getBody();
    }

    public Root getOrder(String orderId) {
        try {
            String accessToken = getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<Root> response = restTemplate.getForEntity(
                payPalConfig.getOrdersUrl() + "/" + orderId,
                Root.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("Error al obtener la orden de PayPal. Status: {}", response.getStatusCode());
                throw new RuntimeException("Error al obtener la orden de PayPal: " + response.getStatusCode());
            }

            return response.getBody();
        } catch (Exception e) {
            log.error("Error al obtener la orden de PayPal: {}", e.getMessage());
            throw new RuntimeException("Error al obtener la orden de PayPal: " + e.getMessage());
        }
    }

    private String getAccessToken() {
        try {
            AuthResponse authResponse = authenticate();
            if (authResponse == null || authResponse.getAccessToken() == null) {
                log.error("No se pudo obtener el token de acceso de PayPal");
                throw new RuntimeException("Error de autenticación con PayPal: No se pudo obtener el token de acceso");
            }
            return authResponse.getAccessToken();
        } catch (Exception e) {
            log.error("Error al autenticar con PayPal: {}", e.getMessage());
            throw new RuntimeException("Error de autenticación con PayPal: " + e.getMessage());
        }
    }
}

