package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.ArticuloCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloClienteRepository extends JpaRepository<ArticuloCliente, Long> {
}