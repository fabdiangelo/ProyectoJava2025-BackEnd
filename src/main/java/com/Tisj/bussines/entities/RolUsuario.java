package com.Tisj.bussines.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name="ROLES")
public class RolUsuario implements Serializable {


    @Id
    private Long id;
    private String nombre;
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;

    // EJECUTAR EN BD PARA CREAR ADMIN:
    // INSERT INTO roles (id, nombre) VALUES (1, 'ADMIN'), (2, 'USER');
    // INSERT INTO usuario (email, password, nombre, apellido, genero, nacimiento, email_recuperacion, activo) VALUES ('admin', 'admin', 'Sol', 'Fuentes', 'f', '1980-04-14', 'notengo@gmail.com', true);
    // INSERT INTO usuarios_roles (usuario, id_rol) VALUES ('admin', 1);
}
