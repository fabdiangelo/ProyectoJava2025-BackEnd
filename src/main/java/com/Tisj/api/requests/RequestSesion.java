package com.Tisj.api.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSesion {
    private String titulo;
    private String descripcion;
    private byte[] imagen;
}
