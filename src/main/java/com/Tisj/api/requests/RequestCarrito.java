package com.Tisj.api.requests;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
public class RequestCarrito {
    @NotEmpty(message = "La lista de artículos no puede estar vacía")
    private List<Long> itemIds;
    
    private Long pagoId;
    
    @NotNull(message = "El estado del carrito es requerido")
    private Boolean activo = true;
    
    private String usuarioEmail; // Email del usuario propietario del carrito
    
    // Constructor vacío
    public RequestCarrito() {}
    
    // Constructor con todos los campos
    public RequestCarrito(List<Long> itemIds, Long pagoId, Boolean activo, String usuarioEmail) {
        this.itemIds = itemIds;
        this.pagoId = pagoId;
        this.activo = activo;
        this.usuarioEmail = usuarioEmail;
    }
}
