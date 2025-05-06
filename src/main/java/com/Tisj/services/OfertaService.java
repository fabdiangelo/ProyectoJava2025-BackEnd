package com.Tisj.services;

import com.Tisj.bussines.entities.Oferta;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Paquete;
import com.Tisj.bussines.entities.Enum.EnumDescuento;
import com.Tisj.bussines.repositories.OfertaRepository;
import com.Tisj.bussines.repositories.CursoRepository;
import com.Tisj.bussines.repositories.PaqueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OfertaService {

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private PaqueteRepository paqueteRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Oferta> getAllOfertas() {
        List<Oferta> ofertas = ofertaRepository.findAll();
        ofertas.forEach(o -> o.getArticulos().size()); // Forzar la inicialización
        return ofertas;
    }

    @Transactional(readOnly = true)
    public Optional<Oferta> getOfertaById(Long id) {
        Optional<Oferta> ofertaOpt = ofertaRepository.findById(id);
        ofertaOpt.ifPresent(o -> o.getArticulos().size()); // Forzar la inicialización
        return ofertaOpt;
    }

    public void aplicarOfertaACurso(Long cursoId, Long ofertaId) {
        Optional<Oferta> ofertaOpt = ofertaRepository.findById(ofertaId);
        if (ofertaOpt.isPresent()) {
            Oferta oferta = ofertaOpt.get();
            Curso curso = cursoRepository.findById(cursoId).orElse(null);
            if (curso != null) {
                curso.setOferta(oferta);
                cursoRepository.save(curso);
            }
        }
    }

    public void aplicarOfertaAPaquete(Long paqueteId, Long ofertaId) {
        Optional<Oferta> ofertaOpt = ofertaRepository.findById(ofertaId);
        if (ofertaOpt.isPresent()) {
            Oferta oferta = ofertaOpt.get();
            Paquete paquete = paqueteRepository.findById(paqueteId).orElse(null);
            if (paquete != null) {
                paquete.setOferta(oferta);
                paqueteRepository.save(paquete);
            }
        }
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
