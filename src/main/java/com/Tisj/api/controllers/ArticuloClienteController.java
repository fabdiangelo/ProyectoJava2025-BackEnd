package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.ArticuloCliente;
import com.Tisj.services.ArticuloClienteService;
import com.Tisj.api.pojo.ArticuloClienteDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.Tisj.bussines.entities.DT.DTArticuloCliente;
import com.Tisj.bussines.entities.Articulo;
import com.Tisj.bussines.entities.Usuario;
import com.Tisj.bussines.repositories.ArticuloRepository;
import com.Tisj.bussines.repositories.UsuarioRepository;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/articulos_cliente")
public class ArticuloClienteController {

    @Autowired
    private ArticuloClienteService articuloClienteService;

    @Autowired
    private ArticuloRepository articuloRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoints para usuarios (DTO)
    @GetMapping("/usuario/mis-cursos")
    public ResponseEntity<List<DTArticuloCliente>> getMisCursosUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String email = auth.getName();
            List<DTArticuloCliente> misCursos = articuloClienteService.getArticulosClienteByUsuarioEmailDT(email);
            return new ResponseEntity<>(misCursos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<DTArticuloCliente> getArticuloClienteUsuario(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            DTArticuloCliente articuloCliente = articuloClienteService.getArticuloClienteByIdDT(id);
            if (articuloCliente != null) {
                return new ResponseEntity<>(articuloCliente, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    // Endpoints comunes (entidad completa)
    @GetMapping
    public ResponseEntity<List<ArticuloCliente>> getArticulosCliente() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            List<ArticuloCliente> articulosCliente = articuloClienteService.getAllArticulosCliente();
            return new ResponseEntity<>(articulosCliente, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloCliente> getArticuloCliente(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            ArticuloCliente articuloCliente = articuloClienteService.getArticuloClienteById(id);
            if (articuloCliente != null) {
                return new ResponseEntity<>(articuloCliente, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<ArticuloCliente> createArticuloCliente(@RequestBody ArticuloCliente articuloCliente) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            ArticuloCliente nuevoArticuloCliente = articuloClienteService.createArticuloCliente(articuloCliente);
            return new ResponseEntity<>(nuevoArticuloCliente, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloCliente> updateArticuloCliente(@PathVariable Long id, @RequestBody ArticuloCliente articuloCliente) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            ArticuloCliente articuloClienteActualizado = articuloClienteService.updateArticuloCliente(id, articuloCliente);
            if (articuloClienteActualizado != null) {
                return new ResponseEntity<>(articuloClienteActualizado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticuloCliente(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
            articuloClienteService.deleteArticuloCliente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/completar/{id}")
    public ResponseEntity<ArticuloCliente> completarArticuloCliente(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            ArticuloCliente actualizado = articuloClienteService.completarArticuloCliente(id);
            if (actualizado != null) {
                return new ResponseEntity<>(actualizado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/reiniciar/{id}")
    public ResponseEntity<ArticuloCliente> reiniciarArticuloCliente(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            ArticuloCliente actualizado = articuloClienteService.reiniciarArticuloCliente(id);
            if (actualizado != null) {
                return new ResponseEntity<>(actualizado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    // Endpoint POST para crear ArticuloCliente usando el DTO esencial
    @PostMapping("/usuario")
    public ResponseEntity<DTArticuloCliente> crearArticuloClienteDesdeDTO(@RequestBody DTArticuloCliente dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            DTArticuloCliente guardado = articuloClienteService.createArticuloClienteFromDTO(dto);
            if (guardado == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
