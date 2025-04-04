package com.Tisj.bussines.entities;

import lombok.Data;

import java.util.List;

@Data
public abstract class Usuario {
    private String email;
    private String password;
    private String Nombre;
    private String emailRecuperacion;
//    private List<Curso> cursos;

    public Void modificarContrasena(){
        return null;
    }
    public Void recuperarCuenta(String email){
        return null;
    }
}
