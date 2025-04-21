package com.Tisj.services;

import com.Tisj.bussines.entities.Oferta;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Paquete;
import com.Tisj.bussines.entities.Enum.EnumDescuento;
import com.Tisj.bussines.repositories.OfertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfertaService {

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private CursoService cursoService;
    @Autowired
    private PaqueteService paqueteService;


    public void aplicarOfertaACurso(Long cursoId, Long ofertaId) {
        return;
    }

    public void aplicarOfertaAPaquete(Long paqueteId, Long ofertaId) {
       return;
        }

    public List<Oferta> getAllOfertas() {
        return ofertaRepository.findAll();
    }

    public Optional<Oferta> getOfertaById(Long id) {
        return ofertaRepository.findById(id);
    }

    public Oferta createOferta(Oferta oferta) {
        return ofertaRepository.save(oferta);
    }

    public Oferta updateOferta(Long id, Oferta oferta) {
        oferta.setId(id);
        return ofertaRepository.save(oferta);
    }

    public void deleteOferta(Long id) {
        ofertaRepository.deleteById(id);
    }
}
