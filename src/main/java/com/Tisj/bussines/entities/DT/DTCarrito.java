package com.Tisj.bussines.entities.DT;

import java.time.LocalDate;
import java.util.List;

public class DTCarrito {
    private Long id;
    private LocalDate vencimiento;
    private boolean activo;
    private List<Long> items; // IDs de los artículos
    private Long pago; // ID del pago
    private String usuario; // Email del usuario
    private Float montoTotal;

    public DTCarrito() {}

    public DTCarrito(Long id, LocalDate vencimiento, boolean activo, List<Long> items, Long pago, String usuario, Float montoTotal) {
        this.id = id;
        this.vencimiento = vencimiento;
        this.activo = activo;
        this.items = items;
        this.pago = pago;
        this.usuario = usuario;
        this.montoTotal = montoTotal;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getVencimiento() { return vencimiento; }
    public void setVencimiento(LocalDate vencimiento) { this.vencimiento = vencimiento; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public List<Long> getItems() { return items; }
    public void setItems(List<Long> items) { this.items = items; }

    public Long getPago() { return pago; }
    public void setPago(Long pago) { this.pago = pago; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Float getMontoTotal() { return montoTotal; }
    public void setMontoTotal(Float montoTotal) { this.montoTotal = montoTotal; }
}
