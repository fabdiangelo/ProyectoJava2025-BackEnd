package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
}