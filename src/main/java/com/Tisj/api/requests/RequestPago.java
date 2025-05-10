package com.Tisj.api.requests;

import java.math.BigDecimal;
import java.util.List;

public class RequestPago {
    private BigDecimal monto;
    private String descripcion;
    private List<String> articulosIds;
    private String moneda;
    private String returnUrl;
    private String cancelUrl;

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getArticulosIds() {
        return articulosIds;
    }

    public void setArticulosIds(List<String> articulosIds) {
        this.articulosIds = articulosIds;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
