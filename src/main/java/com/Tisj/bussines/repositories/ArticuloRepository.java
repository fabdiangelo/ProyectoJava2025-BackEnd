package com.Tisj.bussines.repositories;

import com.Tisj.bussines.entities.Articulo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    @Query("""
    SELECT a FROM Articulo a
    WHERE (:search IS NULL OR 
           LOWER(a.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR 
           LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :search, '%')))
      AND (
           (:tipos IS NULL) OR
           (:incluyeCurso = TRUE AND TYPE(a) = Curso) OR
           (:incluyePaquete = TRUE AND TYPE(a) = Paquete)
      )
      AND (
        a.activo
      )
      AND (:precioMin IS NULL OR a.precio >= :precioMin)
      AND (:precioMax IS NULL OR a.precio <= :precioMax)
""")
    Page<Articulo> buscarFiltrados(@Param("search") String search,
                                   @Param("incluyeCurso") boolean incluyeCurso,
                                   @Param("incluyePaquete") boolean incluyePaquete,
                                   @Param("tipos") boolean tiposNoNulo,
                                   @Param("precioMin") Float precioMin,
                                   @Param("precioMax") Float precioMax,
                                   Pageable pageable);

    Articulo findTopByOrderByPrecioDesc();
}