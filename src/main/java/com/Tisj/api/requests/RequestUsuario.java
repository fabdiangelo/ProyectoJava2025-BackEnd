package com.Tisj.api.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RequestUsuario {
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private Boolean activo;
}
