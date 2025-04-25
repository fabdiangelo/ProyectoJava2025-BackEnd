package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Tisj.bussines.entities.Paquete;

@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
    
}
