package com.Tisj.bussines.entities.DT;

import com.Tisj.bussines.entities.Articulo;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Paquete;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTArticulo {
    private Long id;
    private String nombre;
    private String descripcion;
    private Float precio;
    private String videoPresentacion;
    private boolean activo;
    private String tipo;

    public DTArticulo() {}

    public DTArticulo(Long id, String nombre, String descripcion, Float precio, String videoPresentacion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.videoPresentacion = videoPresentacion;
        this.activo = activo;
    }

    public DTArticulo(Articulo art) {
        this.id = art.getId();
        this.nombre = art.getNombre();
        this.descripcion = art.getDescripcion();
        this.precio = art.getPrecio();
        this.videoPresentacion = art.getVideoPresentacion();
        this.activo = art.getActivo();
        this.tipo = art instanceof Curso ? "curso" :
                art instanceof Paquete ? "paquete" : "articulo";
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Float getPrecio() { return precio; }
    public void setPrecio(Float precio) { this.precio = precio; }
    public String getVideoPresentacion() { return videoPresentacion; }
    public void setVideoPresentacion(String videoPresentacion) { this.videoPresentacion = videoPresentacion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
} 