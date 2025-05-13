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

    @Column(nullable = false)
    private LocalDate caducidad;
    @Column(nullable = false)
    private Boolean isCompleto;
    @Column(nullable = false)
    private Boolean isCaducado;

    @ManyToOne
    @JoinColumn(name = "articulo_id", nullable = false)
    private Articulo articulo;

    public ArticuloCliente(Articulo articulo){
        this.caducidad = LocalDate.now().plusMonths(3);
        this.isCompleto = false;
        this.isCaducado = false;
        this.articulo = articulo;
    }

    public void reiniciar() {
        this.isCompleto = false;
        this.isCaducado = false;
        this.caducidad = LocalDate.now().plusMonths(3);
    }

    public void caducar() {
        this.isCaducado = true;
    }
}
