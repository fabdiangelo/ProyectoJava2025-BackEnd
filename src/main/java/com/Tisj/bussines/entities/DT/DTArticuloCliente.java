package com.Tisj.bussines.entities.DT;

import java.time.LocalDate;

public class DTArticuloCliente {
    public Long id;
    public LocalDate caducidad;
    public String estado;
    public Boolean activo;
    public Long articulo;      // Solo el ID del artículo
    public String usuario;     // Solo el email del usuario

    public DTArticuloCliente() {}

    public DTArticuloCliente(Long id, LocalDate caducidad, String estado, Boolean activo, Long articulo, String usuario) {
        this.id = id;
        this.caducidad = caducidad;
        this.estado = estado;
        this.activo = activo;
        this.articulo = articulo;
        this.usuario = usuario;
    }
}
