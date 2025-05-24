package com.Tisj.api.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestPaquete {
    private String nombre;
    private String descripcion;
    private Float precio;
    private String videoPresentacion;
    private List<String> cursos;
}
