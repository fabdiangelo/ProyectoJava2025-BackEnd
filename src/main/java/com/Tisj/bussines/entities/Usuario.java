package com.Tisj.bussines.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Data
@Entity
public abstract class Usuario {
    @Id
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
