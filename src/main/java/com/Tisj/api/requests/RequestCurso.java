package com.Tisj.api.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestCurso {
    private String nombre;
    private String descripcion;
    private Float precio;
    private String videoPresentacion;
    private Integer duracionTotal;
    private String pdf;
    private List<Long> videoIds;
}
