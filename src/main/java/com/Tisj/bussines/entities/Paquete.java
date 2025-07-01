package com.Tisj.bussines.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Paquete extends Articulo{
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "paquete_curso",
            joinColumns = @JoinColumn(name = "paquete_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    @JsonManagedReference
    @JsonIgnoreProperties("paquetes")
    private List<Curso> cursos;


    public Paquete (String nombre, String descripcion, Float precio, String videoPresentacion, List<Curso> cursos){
        super(nombre, descripcion, precio, videoPresentacion);
        this.cursos = cursos;
    }

    public Paquete (String nombre, String descripcion, Float precio, String videoPresentacion, List<Curso> cursos, Boolean activo){
        super(nombre, descripcion, precio, videoPresentacion, activo);
        this.cursos = cursos;
    }
}
