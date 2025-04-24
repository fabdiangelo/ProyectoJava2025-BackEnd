package com.Tisj.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.Tisj.bussines.entities.Pago;

@AllArgsConstructor
@Getter
public class TokenPago {
    private String nombre, email, token;
    private String[] roles;

    public TokenPago(Pago pago) {
        this.nombre = pago.getUsuario().getNombre();
        this.email = pago.getUsuario().getEmail();
        this.token = generateToken(pago);
    }

    private String generateToken(Pago pago) {
        // Implementación para generar un token basado en los datos del pago
        return "TOKEN_GENERADO_" + pago.getId();
    }
}