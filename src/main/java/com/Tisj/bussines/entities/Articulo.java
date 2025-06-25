package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class Articulo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique=true)
    private String nombre;
    @Column(nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private Float precio;
    private String videoPresentacion;
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private Oferta oferta;

    @OneToMany(mappedBy = "articulo")
    @JsonBackReference
    private List<ArticuloCliente> articuloClientes;

    public Articulo (String nombre, String descripcion, Float precio, String videoPresentacion){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.videoPresentacion = videoPresentacion;
        this.oferta = null;
        this.activo = true;
    }

    public Articulo (String nombre, String descripcion, Float precio, String videoPresentacion, Boolean activo){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.videoPresentacion = videoPresentacion;
        this.oferta = null;
        this.activo = activo;
    }

    // Método getter manual para asegurar que esté disponible
    public Boolean getActivo() {
        return this.activo;
    }
}
