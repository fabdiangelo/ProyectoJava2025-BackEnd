package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Usuario {

    @Id
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private char genero;
    private LocalDate nacimiento;
    private String emailRecuperacion;
    private Boolean activo;

    @ManyToMany
    @JoinTable(name = "USUARIOS_ROLES",
            joinColumns = @JoinColumn(name = "USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_ROL"))
    private List<RolUsuario> roles;
//    private List<Articulo> articulos;

    public Void modificarContrasena(){
        return null;
    }
    public Void recuperarCuenta(String email){
        return null;
    }
}
