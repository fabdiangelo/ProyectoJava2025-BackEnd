package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.ArticuloCliente;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.bussines.entities.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloClienteRepository extends JpaRepository<ArticuloCliente, Long> {
    List<ArticuloCliente> findByUsuarioEmail(String email);
    boolean existsByUsuarioAndArticulo(Usuario usuario, Articulo articulo);
    boolean existsByUsuarioAndArticuloAndActivoTrue(Usuario usuario, Articulo articulo);
}