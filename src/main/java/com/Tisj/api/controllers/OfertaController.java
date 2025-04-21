package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Oferta;
import com.Tisj.services.OfertaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    @Autowired
    private OfertaService ofertaService;

    @GetMapping("/me")
        public ResponseEntity<List<Oferta>> getMyOfertas() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getAuthorities().stream()
                    .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

    @GetMapping
    public ResponseEntity<List<Oferta>> getOfertas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            List<Oferta> ofertas = ofertaService.getAllOfertas();
            return new ResponseEntity<>(ofertas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oferta> getOferta(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Optional<Oferta> ofertaOptional = ofertaService.getOfertaById(id);
            Oferta oferta = ofertaOptional.orElse(null);
            if (oferta != null) {
                return new ResponseEntity<>(oferta, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<Oferta> createOferta(@RequestBody Oferta oferta) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Oferta nuevaOferta = ofertaService.createOferta(oferta);
            return new ResponseEntity<>(nuevaOferta, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oferta> updateOferta(@PathVariable Long id, @RequestBody Oferta oferta) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Oferta ofertaActualizada = ofertaService.updateOferta(id, oferta);
            if (ofertaActualizada != null) {
                return new ResponseEntity<>(ofertaActualizada, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOferta(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            ofertaService.deleteOferta(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
