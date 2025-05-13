package com.Tisj.bussines.entities;

import com.Tisj.bussines.entities.DT.DTFactura;
import com.Tisj.api.response.TokenPago;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDate fechaPago;
    private Float monto;
    private String metodoPago;
    private String externalPaymentId;

    public Pago (Usuario usuario, Float monto, String metodoPago, String externalPaymentId){
        this.usuario = usuario;
        this.fechaPago = LocalDate.now();
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.externalPaymentId = externalPaymentId;
    }

    public DTFactura realizarFacturacion(){
        return new DTFactura(
                this.id,
                usuario.getNombre(),
                usuario.getEmail(),
                this.fechaPago,
                this.monto
        );
    }

    public TokenPago generarTokenPago() {
        return new TokenPago(this);
    }
}
