package com.Tisj.api.controllers;

import com.Tisj.api.requests.RequestCurso;
import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Video;
import com.Tisj.services.CursoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity<Curso> createCurso(@RequestBody RequestCurso reqCurso) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (reqCurso.getVideos() == null || reqCurso.getVideos().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Curso nuevo = cursoService.nuevoCurso(reqCurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping
    public ResponseEntity<List<Curso>> getCursos() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth.getAuthorities().stream()
//                .anyMatch(p -> (p.getAuthority().equals("ADMIN")
//                        || p.getAuthority().equals("USER")))) {
            List<Curso> cursos = cursoService.getAllCursos();
            if (cursos.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(cursos, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> getCurso(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> (p.getAuthority().equals("ADMIN")
                        || p.getAuthority().equals("USER")))) {
            Curso curso = cursoService.getCursoById(id);
            if (curso == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(curso, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> updateCurso(@PathVariable Long id,
                                             @RequestBody RequestCurso reqCurso) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            if (reqCurso.getVideos() == null || reqCurso.getVideos().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Curso cursoActualizado = cursoService.updateCurso(id, reqCurso);
            if (cursoActualizado == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(cursoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @PutMapping("/{id}/videos/{videoId}")
    public ResponseEntity<Curso> updateVideosCurso(@PathVariable Long id, @PathVariable Long videoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Curso cursoActualizado = cursoService.updateVideosCurso(id, videoId);
            if (cursoActualizado == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(cursoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            if(cursoService.deleteCurso(id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}/carrito")
    public ResponseEntity<Map<String, Object>> getCursoParaCarrito(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> (p.getAuthority().equals("ADMIN")
                        || p.getAuthority().equals("USER")))) {
            Curso curso = cursoService.getCursoById(id);
            if (curso == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Crear un mapa con solo la información necesaria para el carrito
            Map<String, Object> cursoInfo = new HashMap<>();
            cursoInfo.put("id", curso.getId());
            cursoInfo.put("nombre", curso.getNombre());
            cursoInfo.put("descripcion", curso.getDescripcion());
            cursoInfo.put("precio", curso.getPrecio());
            cursoInfo.put("videoPresentacion", curso.getVideoPresentacion());
            cursoInfo.put("duracionTotal", curso.getDuracionTotal());
            
            return new ResponseEntity<>(cursoInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
