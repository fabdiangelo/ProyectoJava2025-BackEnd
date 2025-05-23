package com.Tisj.bussines.entities;

import com.Tisj.bussines.entities.Enum.EnumDescuento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Oferta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    private String descripcion;
    private EnumDescuento valor;
    private LocalDate inicio;
    private LocalDate fin;
    private boolean activo;

    @OneToMany(mappedBy = "oferta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnoreProperties("oferta")
    private List<Articulo> articulos;
}
