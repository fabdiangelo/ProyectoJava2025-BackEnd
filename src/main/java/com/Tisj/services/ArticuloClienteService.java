package com.Tisj.services;

import com.Tisj.bussines.entities.ArticuloCliente;
import com.Tisj.bussines.repositories.ArticuloClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloClienteService {

    @Autowired
    private ArticuloClienteRepository articuloClienteRepository;

    public List<ArticuloCliente> getAllArticulosCliente() {
        return articuloClienteRepository.findAll();
    }

    public ArticuloCliente getArticuloClienteById(Long id) {
        return articuloClienteRepository.findById(id).orElse(null);
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
