package com.Tisj.bussines.entities.DT;

public class DTArticulo {
    private Long id;
    private String nombre;
    private String descripcion;
    private Float precio;
    private String videoPresentacion;
    private boolean activo;

    public DTArticulo() {}

    public DTArticulo(Long id, String nombre, String descripcion, Float precio, String videoPresentacion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.videoPresentacion = videoPresentacion;
        this.activo = activo;
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