package com.Tisj.bussines.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    private String descripcion;
    private Float duracion;
    private String link;

    @ManyToMany(mappedBy = "videos")
    private List<Curso> cursos = new ArrayList<>();

    public Video (String nombre, String descripcion, Float duracion, String link){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.link = link;
    }
}
