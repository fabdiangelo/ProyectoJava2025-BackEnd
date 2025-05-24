package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Curso extends Articulo{
    private Integer duracionTotal;

    @ManyToMany(mappedBy = "cursos")
    @JsonIgnore
    private List<Paquete> paquetes;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Video> videos = new ArrayList<>();

    public Curso (String nombre, String descripcion, Float precio, String videoPresentacion){
        super(nombre, descripcion, precio, videoPresentacion);
        this.duracionTotal = 0;
        this.paquetes = new ArrayList<>();
        this.videos = new ArrayList<>();
    }
}
