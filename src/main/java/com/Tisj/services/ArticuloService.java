package com.Tisj.services;

import com.Tisj.bussines.entities.Articulo;
import com.Tisj.bussines.repositories.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Service
@Transactional
public class ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Articulo> getAllArticulos() {
        List<Articulo> articulos = articuloRepository.findAll();
        articulos.forEach(a -> {
            if (a.getOferta() != null) {
                a.getOferta().getArticulos().size(); // Forzar la inicialización
            }
        });
        return articulos;
    }

    @Transactional(readOnly = true)
    public Articulo getArticuloById(Long id) {
        Articulo articulo = articuloRepository.findById(id).orElse(null);
        if (articulo != null && articulo.getOferta() != null) {
            articulo.getOferta().getArticulos().size(); // Forzar la inicialización
        }
        return articulo;
    }

    public Articulo createArticulo(Articulo articulo) {
        return articuloRepository.save(articulo);
    }

    public Articulo updateArticulo(Long id, Articulo articulo) {
        Articulo articuloExistente = articuloRepository.findById(id).orElse(null);
        if (articuloExistente != null) {
            articulo.setId(articuloExistente.getId());
            return articuloRepository.save(articulo);
        }
        return null;
    }

    public void deleteArticulo(Long id) {
        articuloRepository.deleteById(id);
    }
}
