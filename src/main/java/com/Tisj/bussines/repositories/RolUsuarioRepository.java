package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Long> {
    Optional<RolUsuario> findByNombre(String nombre);
}
