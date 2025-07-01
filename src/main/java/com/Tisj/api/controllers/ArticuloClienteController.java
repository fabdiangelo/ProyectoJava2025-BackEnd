package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.ArticuloCliente;
import com.Tisj.services.ArticuloClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articulos_cliente")
public class ArticuloClienteController {

    private static final Logger log = LoggerFactory.getLogger(ArticuloClienteController.class);

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
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            List<ArticuloCliente> articulosCliente = articuloClienteService.getAllArticulosCliente();
            return new ResponseEntity<>(articulosCliente, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloCliente> getArticuloCliente(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
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
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            ArticuloCliente nuevoArticuloCliente = articuloClienteService.createArticuloCliente(articuloCliente);
            return new ResponseEntity<>(nuevoArticuloCliente, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloCliente> updateArticuloCliente(@PathVariable Long id, @RequestBody ArticuloCliente articuloCliente) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
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
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
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

    @PutMapping("/{id}/marcar-visto/{videoId}")
    public ResponseEntity<Void> marcarVideoComoVisto(@PathVariable Long id, @PathVariable Long videoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String email = auth.getName();

            ArticuloCliente ac = articuloClienteService.getArticuloClienteById(id);
            if (ac == null || !ac.getUsuario().getEmail().equals(email)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            try {
                articuloClienteService.marcarVideoComoVisto(id, videoId);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
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

    /**
     * Obtener cursos próximos a vencer (dentro de los próximos 7 días)
     */
    @GetMapping("/proximos-vencer")
    public ResponseEntity<List<DTArticuloCliente>> getCursosProximosAVencer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String email = auth.getName();
            try {
                List<ArticuloCliente> cursosProximos = articuloClienteService.getCursosProximosAVencer(email);
                List<DTArticuloCliente> cursosDTO = cursosProximos.stream()
                    .map(articuloClienteService::toDTO)
                    .collect(Collectors.toList());
                return ResponseEntity.ok(cursosDTO);
            } catch (Exception e) {
                log.error("Error al obtener cursos próximos a vencer para usuario {}: {}", email, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Obtener cursos vencidos del usuario
     */
    @GetMapping("/vencidos")
    public ResponseEntity<List<DTArticuloCliente>> getCursosVencidos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String email = auth.getName();
            try {
                List<ArticuloCliente> cursosVencidos = articuloClienteService.getCursosVencidos(email);
                List<DTArticuloCliente> cursosDTO = cursosVencidos.stream()
                    .map(articuloClienteService::toDTO)
                    .collect(Collectors.toList());
                return ResponseEntity.ok(cursosDTO);
            } catch (Exception e) {
                log.error("Error al obtener cursos vencidos para usuario {}: {}", email, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Renovar la caducidad de un curso (extender por 3 meses más)
     */
    @PostMapping("/{id}/renovar")
    public ResponseEntity<DTArticuloCliente> renovarCaducidad(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String email = auth.getName();
            try {
                ArticuloCliente articuloCliente = articuloClienteService.getArticuloClienteById(id);
                if (articuloCliente == null) {
                    return ResponseEntity.notFound().build();
                }
                
                // Verificar que el usuario sea el propietario del curso
                if (!articuloCliente.getUsuario().getEmail().equals(email) && 
                    !auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                
                ArticuloCliente renovado = articuloClienteService.renovarCaducidad(id);
                if (renovado != null) {
                    DTArticuloCliente dto = articuloClienteService.toDTO(renovado);
                    return ResponseEntity.ok(dto);
                }
                return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                log.error("Error al renovar caducidad del curso {} para usuario {}: {}", id, email, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Ejecutar manualmente la actualización de cursos vencidos (solo para administradores)
     */
    @PostMapping("/actualizar-vencimientos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> actualizarCursosVencidosManual() {
        try {
            articuloClienteService.actualizarCursosVencidos();
            return ResponseEntity.ok("Actualización de cursos vencidos ejecutada exitosamente");
        } catch (Exception e) {
            log.error("Error al ejecutar actualización manual de cursos vencidos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al ejecutar la actualización: " + e.getMessage());
        }
    }
}
