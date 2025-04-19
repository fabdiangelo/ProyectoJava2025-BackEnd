package com.Tisj.bussines.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Curso extends Articulo{
    private Integer duracionTotal;
    private String pdf;

    @ManyToMany(mappedBy = "cursos")
    private List<Paquete> paquetes;

    @ManyToMany
    @JoinTable(
            name = "curso-videos",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id")
    )
    private List<Video> videos;

    public Curso (String nombre, String descripcion, Float precio, String videoPresentacion, Integer duracionTotal, String pdf, List<Video> videos){
        super(nombre, descripcion, precio, videoPresentacion);
        this.duracionTotal = duracionTotal;
        this.pdf = pdf;
        this.paquetes = new ArrayList<>();
        this.videos = videos;
    }
}
