package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByActivoTrue();
    Video findByIdAndActivoTrue(Long id);
}
