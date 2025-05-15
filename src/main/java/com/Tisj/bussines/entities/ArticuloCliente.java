package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ArticuloCliente {
    public enum Estado {
        ACTIVO,
        COMPLETO,
        CADUCADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDate caducidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.ACTIVO;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "articulo_id", nullable = false)
    @JsonProperty("articulo")
    @JsonManagedReference
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonProperty("usuario")
    private Usuario usuario;

    public ArticuloCliente(Articulo articulo, Usuario usuario) {
        this.caducidad = LocalDate.now().plusMonths(3);
        this.estado = Estado.ACTIVO;
        this.activo = true;
        this.articulo = articulo;
        this.usuario = usuario;
    }

    public void reiniciar() {
        this.estado = Estado.ACTIVO;
        this.caducidad = LocalDate.now().plusMonths(3);
    }

    public void caducar() {
        this.estado = Estado.CADUCADO;
    }

    public void actualizarEstadoPorFecha() {
        if (this.estado != Estado.CADUCADO && LocalDate.now().isAfter(this.caducidad)) {
            this.estado = Estado.CADUCADO;
        }
    }
}
