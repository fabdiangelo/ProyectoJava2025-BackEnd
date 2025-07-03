package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.ArticuloCliente;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.bussines.entities.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticuloClienteRepository extends JpaRepository<ArticuloCliente, Long> {
    List<ArticuloCliente> findByUsuarioEmail(String email);
    boolean existsByUsuarioAndArticulo(Usuario usuario, Articulo articulo);
    boolean existsByUsuarioAndArticuloAndActivoTrue(Usuario usuario, Articulo articulo);
    
    // Nuevos métodos para manejo de vencimientos
    List<ArticuloCliente> findByUsuarioEmailAndEstado(String email, ArticuloCliente.Estado estado);
    List<ArticuloCliente> findByUsuarioEmailAndEstadoAndCaducidadBefore(String email, ArticuloCliente.Estado estado, LocalDate fecha);

    Optional<ArticuloCliente> findByUsuarioAndArticulo(Usuario usuario, Articulo articulo);
}