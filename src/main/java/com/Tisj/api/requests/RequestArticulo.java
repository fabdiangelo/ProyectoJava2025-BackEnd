package com.Tisj.api.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestArticulo {
    private String nombre;
    private String descripcion;
    private Float precio;
    private String videoPresentacion;
    private Long ofertaId;
}
