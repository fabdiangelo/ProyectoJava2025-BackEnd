package com.Tisj.services;

import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> getAllCursos() {
        return cursoRepository.findAll(); // Obtener todos los cursos
    }

    // public Curso getCursoById(Long id) {
    //     return cursoRepository.findById(id).orElse(null); // Obtener un curso por su ID
    // }

    public Curso createCurso(Curso curso) {
        return cursoRepository.save(curso); // Crear un nuevo curso
    }

    public Curso updateCurso(Long id, Curso curso) {
        if (cursoRepository.existsById(id)) {
            curso.setId(id);
            return cursoRepository.save(curso); // Actualizar un curso existente
        }
        return null;
    }

    public void deleteCurso(Long id) {
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id); // Eliminar un curso por su ID
        }
    }

    public Curso getCursoById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCursoById'");
    }
}
