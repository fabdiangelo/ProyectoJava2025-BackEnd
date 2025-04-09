package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
}