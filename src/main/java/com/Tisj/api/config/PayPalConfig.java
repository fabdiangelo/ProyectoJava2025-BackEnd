package com.Tisj.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import java.time.Duration;

@Configuration
public class PayPalConfig {
    @Value("${TU_CLIENT_ID}")
    private String clientId;

    @Value("${TU_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${paypal.mode:sandbox}")
    private String mode;

    // URL base para Sandbox
    private static final String PAYPAL_SANDBOX_URL = "https://api-m.sandbox.paypal.com";
    private static final String PAYPAL_LIVE_URL = "https://api-m.paypal.com";

    // Endpoints específicos
    private static final String AUTH_URL = "/v1/oauth2/token";
    private static final String ORDERS_URL = "/v2/checkout/orders";
    private static final String CAPTURE_URL = "/v2/checkout/orders/%s/capture";

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.connectTimeout(Duration.ofSeconds(10)).readTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAuthUrl() {
        return getBaseUrl() + AUTH_URL;
    }

    public String getOrdersUrl() {
        return getBaseUrl() + ORDERS_URL;
    }

    public String getCaptureUrl(String orderId) {
        return getBaseUrl() + String.format(CAPTURE_URL, orderId);
    }

    public String getClientTokenUrl() {
        return getBaseUrl() + "/v1/identity/generate-token";
    }

    public String getBaseUrl() {
        return "sandbox".equalsIgnoreCase(mode) ? PAYPAL_SANDBOX_URL : PAYPAL_LIVE_URL;
    }

    public boolean isSandbox() {
        return "sandbox".equalsIgnoreCase(mode);
    }
}
