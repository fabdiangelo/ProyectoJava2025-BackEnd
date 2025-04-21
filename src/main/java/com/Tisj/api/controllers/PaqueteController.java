package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Paquete;
import com.Tisj.services.PaqueteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/paquetes")
@PreAuthorize("hasAuthority('ADMIN')")
public class PaqueteController {

    @Autowired
    private PaqueteService paqueteService;

    @GetMapping
    public ResponseEntity<List<Paquete>> getPaquetes() {
        List<Paquete> paquetes = paqueteService.getAllPaquetes();
        return new ResponseEntity<>(paquetes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paquete> getPaquete(@PathVariable Long id) {
        Paquete paquete = paqueteService.getPaqueteById(id);
        if (paquete != null) {
            return new ResponseEntity<>(paquete, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Paquete> createPaquete(@RequestBody Paquete paquete) {
        Paquete nuevoPaquete = paqueteService.createPaquete(paquete);
        return new ResponseEntity<>(nuevoPaquete, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paquete> updatePaquete(@PathVariable Long id, @RequestBody Paquete paquete) {
        Paquete paqueteActualizado = paqueteService.updatePaquete(id, paquete);
        if (paqueteActualizado != null) {
            return new ResponseEntity<>(paqueteActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaquete(@PathVariable Long id) {
        paqueteService.deletePaquete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
