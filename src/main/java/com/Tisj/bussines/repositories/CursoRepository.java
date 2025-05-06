package com.Tisj.bussines.repositories;

import java.util.List;
import java.util.Optional;

import com.Tisj.bussines.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Tisj.bussines.entities.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByActivoTrue();
    Curso findByIdAndActivoTrue(Long id);
}
