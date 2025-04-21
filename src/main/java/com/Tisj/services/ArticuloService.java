package com.Tisj.services;

import com.Tisj.bussines.entities.Articulo;
import com.Tisj.bussines.repositories.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    public List<Articulo> getAllArticulos() {
        return articuloRepository.findAll();
    }

    public Articulo getArticuloById(Long id) {
        return articuloRepository.findById(id).orElse(null);
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
