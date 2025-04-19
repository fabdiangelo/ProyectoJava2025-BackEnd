package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Articulo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    private String descripcion;
    private Float precio;
    private String videoPresentacion;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private Oferta oferta;

    public Articulo (String nombre, String descripcion, Float precio, String videoPresentacion){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.videoPresentacion = videoPresentacion;
        this.oferta = null;
    }
}
