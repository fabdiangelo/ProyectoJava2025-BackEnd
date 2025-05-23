package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
    private Float duracion;
    private String link;
    private boolean activo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curso_id")
    @JsonIgnore
    private Curso curso;

    public Video (String nombre, String descripcion, Float duracion, String link){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.link = link;
        this.activo = true;
    }
}
