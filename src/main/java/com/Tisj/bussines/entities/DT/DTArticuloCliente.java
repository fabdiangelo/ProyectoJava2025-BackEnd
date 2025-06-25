package com.Tisj.bussines.entities.DT;

import java.time.LocalDate;
import java.util.Set;

public class DTArticuloCliente {
    public Long id;
    public LocalDate caducidad;
    public String estado;
    public Boolean activo;
    public Long articulo;      // Solo el ID del artículo
    public String usuario;     // Solo el email del usuario
    public double progreso;
    public Set<Long> videosVistos; // IDs de los videos vistos

    public DTArticuloCliente() {}

    public DTArticuloCliente(Long id, LocalDate caducidad, String estado, Boolean activo, Long articulo, String usuario, double progreso) {
        this.id = id;
        this.caducidad = caducidad;
        this.estado = estado;
        this.activo = activo;
        this.articulo = articulo;
        this.usuario = usuario;
        this.progreso = progreso;
    }

    public DTArticuloCliente(Long id, LocalDate caducidad, String estado, Boolean activo, Long articulo, String usuario, double progreso, Set<Long> videosVistos) {
        this.id = id;
        this.caducidad = caducidad;
        this.estado = estado;
        this.activo = activo;
        this.articulo = articulo;
        this.usuario = usuario;
        this.progreso = progreso;
        this.videosVistos = videosVistos;
    }
}
