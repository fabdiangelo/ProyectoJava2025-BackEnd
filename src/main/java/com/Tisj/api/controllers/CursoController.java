package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Curso;
import com.Tisj.services.CursoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cursos")
@PreAuthorize("hasAuthority('ADMIN')")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> getCursos() {
        List<Curso> cursos = cursoService.getAllCursos();
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> getCurso(@PathVariable Long id) {
        Curso curso = cursoService.getCursoById(id);
        if (curso != null) {
            return new ResponseEntity<>(curso, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Curso> createCurso(@RequestBody Curso curso) {
        Curso nuevoCurso = cursoService.createCurso(curso);
        return new ResponseEntity<>(nuevoCurso, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> updateCurso(@PathVariable Long id, @RequestBody Curso curso) {
        Curso cursoActualizado = cursoService.updateCurso(id, curso);
        if (cursoActualizado != null) {
            return new ResponseEntity<>(cursoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Long id) {
        cursoService.deleteCurso(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
