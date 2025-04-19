package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titulo;
    private String descripcion;
    @Lob
    private byte[] imagen;

    public Sesion(String titulo, String descripcion, byte[] imagen){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }
}
