package com.Tisj.bussines.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Tisj.bussines.entities.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByActivoTrue();
    Curso findByIdAndActivoTrue(Long id);
    Curso findByNombre(String nombre);
    
    // Método para verificar si un curso existe independientemente de su estado activo
    @Query("SELECT c FROM Curso c WHERE c.id = :id")
    Optional<Curso> findCursoById(@Param("id") Long id);
    
    // Método para verificar si un curso existe pero está inactivo
    @Query("SELECT c FROM Curso c WHERE c.id = :id AND c.activo = false")
    Optional<Curso> findInactiveCursoById(@Param("id") Long id);
}
