package com.Tisj.api.controllers;

import com.Tisj.api.requests.RequestOferta;
import com.Tisj.bussines.entities.DT.DTArticulo;
import com.Tisj.bussines.entities.DT.DTOferta;
import com.Tisj.bussines.entities.Oferta;
import com.Tisj.bussines.entities.Articulo;
import com.Tisj.services.OfertaService;
import com.Tisj.services.ArticuloService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    @Autowired
    private OfertaService ofertaService;

    @Autowired
    private ArticuloService articuloService;

    // Utilitario para mapear Oferta a DTOferta
    private DTOferta mapToDTOferta(Oferta oferta) {
        List<DTArticulo> dtArticulos = oferta.getArticulos() != null ?
            oferta.getArticulos().stream().map(this::mapToDTArticulo).collect(Collectors.toList()) :
            null;
        return new DTOferta(
            oferta.getId(),
            oferta.getNombre(),
            oferta.getDescripcion(),
            oferta.getValor(),
            oferta.getInicio(),
            oferta.getFin(),
            oferta.isActivo(),
            dtArticulos
        );
    }

    private DTArticulo mapToDTArticulo(Articulo articulo) {
        return new DTArticulo(
            articulo.getId(),
            articulo.getNombre(),
            articulo.getDescripcion(),
            articulo.getPrecio(),
            articulo.getVideoPresentacion(),
            articulo.isActivo()
        );
    }

    @GetMapping
    public ResponseEntity<List<DTOferta>> getOfertas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            List<Oferta> ofertas = ofertaService.getAllOfertas();
            List<DTOferta> dtOfertas = ofertas.stream().map(this::mapToDTOferta).collect(Collectors.toList());
            return new ResponseEntity<>(dtOfertas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTOferta> getOferta(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Optional<Oferta> ofertaOptional = ofertaService.getOfertaById(id);
            Oferta oferta = ofertaOptional.orElse(null);
            if (oferta != null) {
                DTOferta dtOferta = mapToDTOferta(oferta);
                return new ResponseEntity<>(dtOferta, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<Oferta> createOferta(@Valid @RequestBody RequestOferta requestOferta) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Oferta oferta = new Oferta();
            oferta.setNombre(requestOferta.getNombre());
            oferta.setDescripcion(requestOferta.getDescripcion());
            oferta.setValor(requestOferta.getValor());
            oferta.setInicio(requestOferta.getInicio());
            oferta.setFin(requestOferta.getFin());
            oferta.setActivo(requestOferta.isActivo());

            // Asociar artículos si se pasan IDs
            if (requestOferta.getArticulosIds() != null && !requestOferta.getArticulosIds().isEmpty()) {
                List<Articulo> articulos = requestOferta.getArticulosIds().stream()
                        .map(articuloService::getArticuloById)
                        .filter(a -> a != null)
                        .collect(Collectors.toList());
                articulos.forEach(a -> a.setOferta(oferta));
                oferta.setArticulos(articulos);
            }
            Oferta nuevaOferta = ofertaService.createOferta(oferta);
            return new ResponseEntity<>(nuevaOferta, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oferta> updateOferta(@PathVariable Long id, @Valid @RequestBody RequestOferta requestOferta) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Oferta oferta = new Oferta();
            oferta.setId(id);
            oferta.setNombre(requestOferta.getNombre());
            oferta.setDescripcion(requestOferta.getDescripcion());
            oferta.setValor(requestOferta.getValor());
            oferta.setInicio(requestOferta.getInicio());
            oferta.setFin(requestOferta.getFin());
            oferta.setActivo(requestOferta.isActivo());
            
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

    @PostMapping("/{id}/articulos")
    public ResponseEntity<?> asociarArticulosAOferta(@PathVariable Long id, @RequestBody List<Long> articulosIds) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Optional<Oferta> ofertaOpt = ofertaService.getOfertaById(id);
            if (ofertaOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Oferta oferta = ofertaOpt.get();
            List<Articulo> articulos = articulosIds.stream()
                    .map(articuloService::getArticuloById)
                    .filter(a -> a != null)
                    .collect(Collectors.toList());
            articulos.forEach(a -> a.setOferta(oferta));
            oferta.setArticulos(articulos);
            ofertaService.updateOferta(id, oferta);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
