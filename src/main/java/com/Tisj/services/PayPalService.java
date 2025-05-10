package com.Tisj.services;

import com.Tisj.api.pojo.PayPal.*;
import com.Tisj.api.requests.RequestPago;
import com.Tisj.api.Paypal.AuthResponse;
import com.Tisj.api.Paypal.ClientTokenResponse;
import com.Tisj.api.config.PayPalConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PayPalService {
    private final RestTemplate restTemplate;
    private final PayPalConfig payPalConfig;

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
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Root order = new Root();
        order.setIntent("CAPTURE");
        
        PurchaseUnit purchaseUnit = new PurchaseUnit();
        Amount amount = new Amount();
        amount.setCurrency_code(requestPago.getMoneda() != null ? requestPago.getMoneda() : "USD");
        amount.setValue(requestPago.getMonto().toString());
        
        purchaseUnit.setAmount(amount);
        purchaseUnit.setDescription(requestPago.getDescripcion());
        
        // Configurar URLs de retorno y cancelación
        Root.ApplicationContext applicationContext = new Root.ApplicationContext();
        applicationContext.setReturn_url(requestPago.getReturnUrl());
        applicationContext.setCancel_url(requestPago.getCancelUrl());
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
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Root> response = restTemplate.getForEntity(
            payPalConfig.getOrdersUrl() + "/" + orderId, 
            Root.class
        );

        return response.getBody();
    }

    public Root listOrders() {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Root> response = restTemplate.getForEntity(
            payPalConfig.getOrdersUrl(), 
            Root.class
        );

        return response.getBody();
    }

    private String getAccessToken() {
        AuthResponse authResponse = authenticate();
        return authResponse.getAccessToken();
    }
}

