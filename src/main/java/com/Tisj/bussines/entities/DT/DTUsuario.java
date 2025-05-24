package com.Tisj.bussines.entities.DT;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DTUsuario {
    private String email, nombre, apellido;
    private boolean activo;
    private List<String> roles;

    public DTUsuario(String email, String nombre, String apellido, boolean activo, List<String> roles){
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = activo;
        this.roles = roles;
    }
}
