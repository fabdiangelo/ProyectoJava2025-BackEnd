package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Paquete;
import com.Tisj.services.PaqueteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        List<Paquete> paquetes = paqueteService.getAllPaquetes();
        return new ResponseEntity<>(paquetes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paquete> getPaquete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        Paquete paquete = paqueteService.getPaqueteById(id);
        if (paquete != null) {
            return new ResponseEntity<>(paquete, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<Paquete> createPaquete(@RequestBody Paquete paquete)
     {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        Paquete nuevoPaquete = paqueteService.createPaquete(paquete);
        return new ResponseEntity<>(nuevoPaquete, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paquete> updatePaquete(@PathVariable Long id, @RequestBody Paquete paquete) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        Paquete paqueteActualizado = paqueteService.updatePaquete(id, paquete);
        if (paqueteActualizado != null) {
            return new ResponseEntity<>(paqueteActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaquete(@PathVariable Long id) 
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
        paqueteService.deletePaquete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // @PostMapping("/{paqueteId}/cursos/{cursoId}")
    // public ResponseEntity<Void> associateCursoToPaquete(@PathVariable Long paqueteId, @PathVariable Long cursoId) {
    //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //     if (auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
    //         paqueteService.associateCursoToPaquete(paqueteId, cursoId);
    //         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //     } else {
    //         return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    //     }
    // }

    // @DeleteMapping("/{paqueteId}/cursos/{cursoId}")
    // public ResponseEntity<Void> disassociateCursoFromPaquete(@PathVariable Long paqueteId, @PathVariable Long cursoId) {
    //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //     if (auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
    //         paqueteService.disassociateCursoFromPaquete(paqueteId, cursoId);
    //         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //     } else {
    //         return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    //     }
    // }
    
}
