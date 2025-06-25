package com.Tisj.services;

import com.Tisj.api.requests.RequestMP;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayPalService {
    public String crearOrdenPayPal(RequestMP dto) throws IOException {
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        String clientSecret = System.getenv("PAYPAL_CLIENT_SECRET");

        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // 1. Obtener token de acceso
        String credentials = Credentials.basic(clientId, clientSecret);

        RequestBody tokenBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        Request tokenRequest = new Request.Builder()
                .url("https://api-m.sandbox.paypal.com/v1/oauth2/token")
                .header("Authorization", credentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(tokenBody)
                .build();

        Response tokenResponse = client.newCall(tokenRequest).execute();
        String tokenJson = tokenResponse.body().string();
        String accessToken = mapper.readTree(tokenJson).get("access_token").asText();

        // 2. Crear orden con los productos
        List<Object> items = dto.getItems().stream().map(producto -> Map.of(
                "name", producto.getNombre(),
                "unit_amount", Map.of(
                        "currency_code", "USD",
                        "value", BigDecimal.valueOf(producto.getPrecio())
                                .setScale(2, RoundingMode.HALF_UP)
                                .toPlainString()
                ),
                "quantity", "1"
        )).collect(Collectors.toList());

        String total = calcularTotal(dto);

        Map<String, Object> orderPayload = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(Map.of(
                        "reference_id", dto.getUsuarioId() + "|" + dto.getCarritoId(),
                        "items", items,
                        "amount", Map.of(
                                "currency_code", "USD",
                                "value", total,
                                "breakdown", Map.of(
                                        "item_total", Map.of(
                                                "currency_code", "USD",
                                                "value", total
                                        )
                                )
                        )
                )),
                "application_context", Map.of(
                        "return_url", "https://solfuentes-prueba.netlify.app/pago-paypal",
                        "cancel_url", "https://solfuentes-prueba.netlify.app/carrito"
                )
        );

        String json = mapper.writeValueAsString(orderPayload);

        RequestBody orderBody = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request orderRequest = new Request.Builder()
                .url("https://api-m.sandbox.paypal.com/v2/checkout/orders")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .post(orderBody)
                .build();

        Response orderResponse = client.newCall(orderRequest).execute();
        String orderJson = orderResponse.body().string();

        System.out.println("Respuesta de PayPal:");
        System.out.println(orderJson);

        JsonNode root = mapper.readTree(orderJson);

        // Buscar el link de aprobación (rel: "approve")
        for (JsonNode link : root.get("links")) {
            if ("approve".equals(link.get("rel").asText())) {
                return link.get("href").asText();
            }
        }

        throw new RuntimeException("No se encontró el enlace de aprobación de PayPal");
    }

    // Función auxiliar para calcular total
    private String calcularTotal(RequestMP dto) {
        BigDecimal total = dto.getItems().stream()
                .map(i -> BigDecimal.valueOf(i.getPrecio()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Usar punto como separador decimal (PayPal requiere esto)
        return total.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    public String capturarOrden(String orderId, String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderId + "/capture")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            System.out.println("Respuesta al capturar: " + body);
            return body;
        }
    }

    public String obtenerAccessToken() throws IOException {
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        String clientSecret = System.getenv("PAYPAL_CLIENT_SECRET");

        OkHttpClient client = new OkHttpClient();

        String credentials = Credentials.basic(clientId, clientSecret);

        RequestBody tokenBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        Request tokenRequest = new Request.Builder()
                .url("https://api-m.sandbox.paypal.com/v1/oauth2/token")
                .header("Authorization", credentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(tokenBody)
                .build();

        try (Response response = client.newCall(tokenRequest).execute()) {
            String tokenJson = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(tokenJson).get("access_token").asText();
        }
    }

}

