package com.Tisj.services;
import com.Tisj.api.requests.RequestMP;
import com.Tisj.bussines.entities.DT.DTArtCarrito;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MercadoPagoService {

    public String crearPreferencia(RequestMP dto) throws Exception {
        // Setear tu access token
        MercadoPagoConfig.setAccessToken(System.getenv("MP_ACCESS_TOKEN"));

        String externalReference = dto.getUsuarioId() + "|" + dto.getCarritoId();

        List<PreferenceItemRequest> items = dto.getItems().stream().map(producto ->
                PreferenceItemRequest.builder()
                        .title(producto.getNombre())
                        .quantity(1)
                        .unitPrice(BigDecimal.valueOf(producto.getPrecio()))
                        .currencyId("UYU")
                        .build()
        ).toList();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://solfuentes-prueba.netlify.app/Pago")
                .failure("https://solfuentes-prueba.netlify.app/")
                .pending("https://solfuentes-prueba.netlify.app/Carrito")
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .autoReturn("approved")
                .externalReference(externalReference)
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        return preference.getInitPoint(); // URL a la que redirigís al usuario
    }
}