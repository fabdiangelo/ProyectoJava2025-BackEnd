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
        String email = auth.getName();
        // Solo usuarios activos pueden ver sus cursos
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null || usuario.getActivo() == null || !usuario.getActivo()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
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
            // Ensure ID is null to force auto-generation
            articuloCliente.setId(null);
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

    // Endpoint POST para crear ArticuloCliente usando el DTO esencial para el usuario logueado
    @PostMapping("/usuario")
    public ResponseEntity<DTArticuloCliente> crearArticuloClienteDesdeDTO(@RequestBody DTArticuloCliente dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String userEmail = auth.getName(); // Obtener el email del usuario logueado
            if (dto.articulo == null) { // Accediendo directamente al campo público 'articulo'
                log.warn("ID de artículo no proporcionado en el DTO");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            try {
                // Ignorar cualquier ID que pueda venir en el DTO, será autogenerado
                dto.id = null;
                
                // Llamar al método del servicio con el email del usuario y el ID del artículo
                DTArticuloCliente guardado = articuloClienteService.createArticuloClienteForUser(userEmail, dto.articulo);
                if (guardado == null) {
                    // El servicio retornó null, probablemente porque el usuario ya tiene el artículo
                    log.warn("El usuario {} ya tiene el artículo {}", userEmail, dto.articulo);
                    return new ResponseEntity<>(HttpStatus.CONFLICT); // Usar CONFLICT para recurso existente
                }
                return new ResponseEntity<>(guardado, HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                log.error("Error al crear ArticuloCliente: {}", e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Manejar casos donde el usuario/artículo no se encuentra
            } catch (Exception e) {
                log.error("Error inesperado al crear ArticuloCliente para usuario {}: {}", userEmail, e.getMessage(), e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.warn("Intento de crear ArticuloCliente sin autorización");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
