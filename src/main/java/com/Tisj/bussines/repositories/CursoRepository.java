package com.Tisj.bussines.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
