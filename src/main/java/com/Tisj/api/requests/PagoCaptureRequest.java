package com.Tisj.api.requests;

public class PagoCaptureRequest {
    private String paymentId;
    private String status; // "APPROVED", "CANCELLED", etc.
    private String usuario; // Email o identificador del usuario

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
