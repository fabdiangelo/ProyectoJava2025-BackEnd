package com.Tisj.api.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestOferta {
    private String nombre;
    private String descripcion;
    private String valor;
    private LocalDate inicio;
    private LocalDate fin;
    private boolean activo;
}
