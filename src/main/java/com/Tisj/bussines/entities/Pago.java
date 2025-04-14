package com.Tisj.bussines.entities;

import com.Tisj.bussines.entities.DT.DTFactura;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario cliente;

    private LocalDate fechaPago;
    private Float monto;

    public DTFactura realizarFacturacion(){
        return new DTFactura(
                this.id,
                cliente.getNombre(),
                cliente.getEmail(),
                this.fechaPago,
                this.monto
        );
    }
}
