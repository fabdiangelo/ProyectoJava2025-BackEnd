package com.Tisj.api.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestVideo {
    private String nombre;
    private String descripcion;
    private Float duracion;
    private String link;
}
