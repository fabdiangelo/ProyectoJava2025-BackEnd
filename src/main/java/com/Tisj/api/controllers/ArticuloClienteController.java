package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.ArticuloCliente;
import com.Tisj.services.ArticuloClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/articulos_cliente")
public class ArticuloClienteController {


    

    @Autowired
    private ArticuloClienteService articuloClienteService;

    @GetMapping
    public ResponseEntity<List<ArticuloCliente>> getArticulosCliente() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            List<ArticuloCliente> articulosCliente = articuloClienteService.getAllArticulosCliente();
            return new ResponseEntity<>(articulosCliente, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloCliente> getArticuloCliente(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            ArticuloCliente articuloCliente = articuloClienteService.getArticuloClienteById(id);
            if (articuloCliente != null) {
                return new ResponseEntity<>(articuloCliente, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<ArticuloCliente> createArticuloCliente(@RequestBody ArticuloCliente articuloCliente) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            ArticuloCliente nuevoArticuloCliente = articuloClienteService.createArticuloCliente(articuloCliente);
            return new ResponseEntity<>(nuevoArticuloCliente, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloCliente> updateArticuloCliente(@PathVariable Long id, @RequestBody ArticuloCliente articuloCliente) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            ArticuloCliente articuloClienteActualizado = articuloClienteService.updateArticuloCliente(id, articuloCliente);
            if (articuloClienteActualizado != null) {
                return new ResponseEntity<>(articuloClienteActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticuloCliente(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            articuloClienteService.deleteArticuloCliente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
