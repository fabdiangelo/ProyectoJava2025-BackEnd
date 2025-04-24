package com.Tisj.bussines.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Paquete extends Articulo{
    @ManyToMany
    @JoinTable(
            name = "paquete_curso",
            joinColumns = @JoinColumn(name = "paquete_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursos;


    public Paquete (String nombre, String descripcion, Float precio, String videoPresentacion, List<Curso> cursos){
        super(nombre, descripcion, precio, videoPresentacion);
        this.cursos = cursos;
    }


    public Paquete orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
}
