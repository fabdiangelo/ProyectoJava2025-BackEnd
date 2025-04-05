package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
