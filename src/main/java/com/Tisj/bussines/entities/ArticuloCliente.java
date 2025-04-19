package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ArticuloCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate caducidad;
    private boolean completado;

    @ManyToOne
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    public ArticuloCliente(Articulo articulo){
        this.caducidad = LocalDate.now().plusMonths(3);
        this.completado = false;
        this.articulo = articulo;
    }
}
