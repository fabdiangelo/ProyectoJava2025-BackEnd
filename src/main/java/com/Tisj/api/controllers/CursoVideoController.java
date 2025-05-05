package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Curso;
import com.Tisj.bussines.entities.Video;
import com.Tisj.services.CursoService;
import com.Tisj.services.VideoService;
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
@RequestMapping("/api/curso/{cursoId}/videos")
public class CursoVideoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private VideoService videoService;

    @PostMapping
    public ResponseEntity<Curso> addVideoToCurso(@PathVariable Long cursoId, @RequestBody Long videoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Curso curso = cursoService.updateVideosCurso(cursoId, videoId);
            if (curso == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(curso, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Curso> removeVideoFromCurso(@PathVariable Long cursoId, @PathVariable Long videoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            Curso curso = cursoService.updateVideosCurso(cursoId, -videoId); // Assuming negative videoId means remove
            if (curso == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(curso, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public ResponseEntity<List<Video>> getVideosForCurso(@PathVariable Long cursoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> (p.getAuthority().equals("ADMIN") || p.getAuthority().equals("USER")))) {
            List<Video> videos = cursoService.getVideosCursoById(cursoId);
            if (videos == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(videos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}