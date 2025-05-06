package com.Tisj.services;

import com.Tisj.bussines.entities.ArticuloCliente;
import com.Tisj.bussines.repositories.ArticuloClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Service
@Transactional
public class ArticuloClienteService {

    @Autowired
    private ArticuloClienteRepository articuloClienteRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<ArticuloCliente> getAllArticulosCliente() {
        List<ArticuloCliente> articulosCliente = articuloClienteRepository.findAll();
        articulosCliente.forEach(ac -> {
            if (ac.getArticulo() != null && ac.getArticulo().getOferta() != null) {
                ac.getArticulo().getOferta().getArticulos().size(); // Forzar la inicialización
            }
        });
        return articulosCliente;
    }

    @Transactional(readOnly = true)
    public ArticuloCliente getArticuloClienteById(Long id) {
        ArticuloCliente articuloCliente = articuloClienteRepository.findById(id).orElse(null);
        if (articuloCliente != null && articuloCliente.getArticulo() != null && 
            articuloCliente.getArticulo().getOferta() != null) {
            articuloCliente.getArticulo().getOferta().getArticulos().size(); // Forzar la inicialización
        }
        return articuloCliente;
    }

    public ArticuloCliente createArticuloCliente(ArticuloCliente articuloCliente) {
        return articuloClienteRepository.save(articuloCliente);
    }

    public ArticuloCliente updateArticuloCliente(Long id, ArticuloCliente articuloCliente) {
        ArticuloCliente articuloClienteExistente = articuloClienteRepository.findById(id).orElse(null);
        if (articuloClienteExistente != null) {
            articuloCliente.setId(articuloClienteExistente.getId());
            return articuloClienteRepository.save(articuloCliente);
        }
        return null;
    }

    public void deleteArticuloCliente(Long id) {
        articuloClienteRepository.deleteById(id);
    }
}
