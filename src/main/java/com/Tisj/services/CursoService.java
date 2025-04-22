package com.Tisj.services;

import com.Tisj.bussines.entities.Curso;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CursoService {

    public List<Curso> getAllCursos() {
        // TODO: Implementar la lógica para obtener todos los cursos
        return new ArrayList<>();
    }

    public Curso getCursoById(Long id) {
        // TODO: Implementar la lógica para obtener un curso por su ID
        return null;
    }

    public Curso createCurso(Curso curso) {
        // TODO: Implementar la lógica para crear un nuevo curso
        return null;
    }

    public Curso updateCurso(Long id, Curso curso) {
        // TODO: Implementar la lógica para actualizar un curso existente
        return null;
    }

    public void deleteCurso(Long id) {
        // TODO: Implementar la lógica para eliminar un curso
    }
}
