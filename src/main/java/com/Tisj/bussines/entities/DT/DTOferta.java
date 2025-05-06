package com.Tisj.bussines.entities.DT;

import com.Tisj.bussines.entities.Enum.EnumDescuento;
import java.time.LocalDate;
import java.util.List;

public class DTOferta {
    private Long id;
    private String nombre;
    private String descripcion;
    private EnumDescuento valor;
    private LocalDate inicio;
    private LocalDate fin;
    private boolean activo;
    private List<DTArticulo> articulos;

    public DTOferta() {}

    public DTOferta(Long id, String nombre, String descripcion, EnumDescuento valor, LocalDate inicio, LocalDate fin, boolean activo, List<DTArticulo> articulos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valor = valor;
        this.inicio = inicio;
        this.fin = fin;
        this.activo = activo;
        this.articulos = articulos;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public EnumDescuento getValor() { return valor; }
    public void setValor(EnumDescuento valor) { this.valor = valor; }
    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }
    public LocalDate getFin() { return fin; }
    public void setFin(LocalDate fin) { this.fin = fin; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public List<DTArticulo> getArticulos() { return articulos; }
    public void setArticulos(List<DTArticulo> articulos) { this.articulos = articulos; }
}
