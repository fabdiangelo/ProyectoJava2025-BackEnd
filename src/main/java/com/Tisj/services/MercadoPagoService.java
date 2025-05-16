package com.Tisj.services;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MercadoPagoService {

    public String crearPreferencia() throws Exception {
        // Setear tu access token
        MercadoPagoConfig.setAccessToken(System.getenv("MP_ACCESS_TOKEN"));

        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title("Producto de prueba")
                .quantity(1)
                .unitPrice(new BigDecimal("100.00"))
                .currencyId("UYU") // o ARS según tu país
                .build();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://solfuentes-prueba.netlify.app/Pago")
                .failure("https://solfuentes-prueba.netlify.app/")
                .pending("https://solfuentes-prueba.netlify.app/Carrito")
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(List.of(item))
                .backUrls(backUrls)
                .autoReturn("approved")
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        return preference.getInitPoint(); // URL a la que redirigís al usuario
    }
}