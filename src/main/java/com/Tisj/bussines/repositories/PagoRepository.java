package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioEmail(String usuarioEmail); // Obtener pagos por email de usuario
}