package com.Tisj.bussines.entities.DT;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DTUsuario {
    private String email, nombre, apellido;
    private char genero;
    private LocalDate nacimiento;
    private boolean activo;
    private List<String> roles;

    public DTUsuario(String email, String nombre, String apellido, char genero, LocalDate nacimiento, boolean activo, List<String> roles){
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.nacimiento = nacimiento;
        this.activo = activo;
        this.roles = roles;
    }
}
