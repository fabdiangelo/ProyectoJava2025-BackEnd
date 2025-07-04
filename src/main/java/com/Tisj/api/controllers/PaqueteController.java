package com.Tisj.api.controllers;

import com.Tisj.api.requests.RequestPaquete;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Paquete;
import com.Tisj.bussines.entities.Video;
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
public class PaqueteController {

    @Autowired
    private PaqueteService paqueteService;

    @PostMapping
    public ResponseEntity<Paquete> createPaquete(@RequestBody RequestPaquete reqPaquete)
     {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Paquete nuevoPaquete = paqueteService.createPaquete(paqueteService.reqToPaquete(reqPaquete));
            if(nuevoPaquete == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(nuevoPaquete, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public ResponseEntity<List<Paquete>> getPaquetes() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth.getAuthorities().stream()
//                .anyMatch(p -> (p.getAuthority().equals("ADMIN")
//                        || p.getAuthority().equals("USER")))) {
            List<Paquete> paquetes = paqueteService.getAllPaquetes();
            if(paquetes.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(paquetes, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paquete> getPaquete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN")
                        || p.getAuthority().equals("USER"))) {
            Paquete paquete = paqueteService.getPaqueteById(id);
            if (paquete == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(paquete, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}/cursos")
    public ResponseEntity<List<Curso>> getCursosDelPaquete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> (p.getAuthority().equals("ADMIN")
                        || p.getAuthority().equals("USER")))) {
            List<Curso> cursos = paqueteService.getCursosDelPaquete(id);
            if (cursos == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paquete> updatePaquete(@PathVariable Long id, @RequestBody RequestPaquete reqPaquete) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Paquete paqueteActualizado = paqueteService.updatePaquete(id, paqueteService.reqToPaquete(reqPaquete));
            if (paqueteActualizado == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(paqueteActualizado, HttpStatus.OK);
    }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}/cursos/{cursoId}")
    public ResponseEntity<Paquete> updateVideosCurso(@PathVariable Long id, @PathVariable Long cursoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Paquete paqueteActualizado = paqueteService.updateCursosPaquete(id, cursoId);
            if (paqueteActualizado == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(paqueteActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaquete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            if(paqueteService.deletePaquete(id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
