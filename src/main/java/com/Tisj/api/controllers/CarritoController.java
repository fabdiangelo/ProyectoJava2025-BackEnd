package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Carrito;
import com.Tisj.bussines.entities.DT.DTCarrito;
import com.Tisj.api.requests.RequestCarrito;
import com.Tisj.services.CarritoService;
import com.Tisj.bussines.repositories.UsuarioRepository;
import jakarta.validation.Valid;
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
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Todo: Chequear que el carrito no exista un carrito asignado a usuario, si no es asi, crear uno y asignarlo.
    @PostMapping
    public ResponseEntity<DTCarrito> crearCarrito(@Valid @RequestBody RequestCarrito request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String email = auth.getName();
            
            try {
                // Verificar si ya existe un carrito activo
                DTCarrito carritoExistente = carritoService.getCarritoByUsuarioEmail(email);
                if (carritoExistente != null && carritoExistente.isActivo()) {
                    log.info("Carrito existente encontrado para usuario: {}", email);
                    return ResponseEntity.ok(carritoExistente);
                }

                // Crear nuevo carrito
                Carrito nuevoCarrito = new Carrito();
                nuevoCarrito.setUsuario(usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")));

                // Primero crear y guardar el carrito para obtener el ID
                DTCarrito carritoCreado = carritoService.createCarrito(nuevoCarrito);

                // Luego agregar los items si se proporcionan
                if (request != null && request.getItemIds() != null) {
                    request.getItemIds().forEach(itemId -> {
                        try {
                            carritoService.agregarItemAlCarrito(carritoCreado.getId(), itemId);
                        } catch (Exception e) {
                            log.error("Error al agregar item {} al carrito: {}", itemId, e.getMessage());
                        }
                    });
                }

                // Obtener el carrito actualizado con los items agregados
                DTCarrito carritoFinal = carritoService.getCarritoById(carritoCreado.getId());

                log.info("Nuevo carrito creado para usuario: {}", email);
                return ResponseEntity.status(HttpStatus.CREATED).body(carritoFinal);
            } catch (Exception e) {
                log.error("Error al crear carrito para usuario {}: {}", email, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        log.warn("Intento de crear carrito sin autorización");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DTCarrito> actualizarCarrito(
            @PathVariable Long id,
            @Valid @RequestBody RequestCarrito request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String emailUsuarioLogueado = auth.getName();
            try {
                DTCarrito carrito = carritoService.getCarritoById(id);
                
                if (carrito == null) {
                    log.warn("Carrito no encontrado con ID: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                
                if (!carrito.getUsuario().equals(emailUsuarioLogueado) && 
                    !auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                    log.warn("Intento de actualización no autorizada del carrito con ID {} por usuario {}", 
                        id, emailUsuarioLogueado);
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }

                // Actualizar items si se proporcionan
                if (request.getItemIds() != null) {
                    // Primero limpiar el carrito actual
                    carrito.getItems().forEach(articulo -> {
                        try {
                            carritoService.quitarItemDelCarrito(id, articulo.getId());
                        } catch (Exception e) {
                            log.error("Error al quitar item {} del carrito: {}", articulo.getId(), e.getMessage());
                        }
                    });
                    // Luego agregar los nuevos items
                    request.getItemIds().forEach(itemId -> {
                        try {
                            carritoService.agregarItemAlCarrito(id, itemId);
                        } catch (Exception e) {
                            log.error("Error al agregar item {} al carrito: {}", itemId, e.getMessage());
                        }
                    });
                }

                // Actualizar estado si se proporciona
                if (request.getActivo() != null) {
                    if (request.getActivo()) {
                        carritoService.activarCarrito(id);
                    } else {
                        carritoService.desactivarCarrito(id);
                    }
                }

                DTCarrito carritoActualizado = carritoService.getCarritoById(id);
                log.info("Carrito {} actualizado exitosamente", id);
                return ResponseEntity.ok(carritoActualizado);
            } catch (Exception e) {
                log.error("Error al actualizar carrito {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarrito(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String emailUsuarioLogueado = auth.getName();
            DTCarrito carrito = carritoService.getCarritoById(id);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!carrito.getUsuario().equals(emailUsuarioLogueado) && !auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                 log.warn("Intento de eliminación no autorizada del carrito con ID {} por usuario {}", id, emailUsuarioLogueado);
                 return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            carritoService.deleteCarrito(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/{carritoId}/items/{articuloId}")
    public ResponseEntity<DTCarrito> agregarArticulo(
            @PathVariable Long carritoId,
            @PathVariable Long articuloId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String emailUsuarioLogueado = auth.getName();
            DTCarrito carrito = carritoService.getCarritoById(carritoId);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!carrito.getUsuario().equals(emailUsuarioLogueado) && !auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                 log.warn("Intento de agregar artículo no autorizado al carrito con ID {} por usuario {}", carritoId, emailUsuarioLogueado);
                 return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            DTCarrito carritoActualizado = carritoService.agregarItemAlCarrito(carritoId, articuloId);
            if (carritoActualizado != null) {
                return new ResponseEntity<>(carritoActualizado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{carritoId}/items/{articuloId}")
    public ResponseEntity<DTCarrito> quitarArticulo(
            @PathVariable Long carritoId,
            @PathVariable Long articuloId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String emailUsuarioLogueado = auth.getName();
            DTCarrito carrito = carritoService.getCarritoById(carritoId);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!carrito.getUsuario().equals(emailUsuarioLogueado) && !auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                 log.warn("Intento de quitar artículo no autorizado del carrito con ID {} por usuario {}", carritoId, emailUsuarioLogueado);
                 return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            DTCarrito carritoActualizado = carritoService.quitarItemDelCarrito(carritoId, articuloId);
            if (carritoActualizado != null) {
                return new ResponseEntity<>(carritoActualizado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarCarrito(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String emailUsuarioLogueado = auth.getName();
            DTCarrito carrito = carritoService.getCarritoById(id);
            
            if (carrito == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Carrito no encontrado");
            }
            
            if (!carrito.getUsuario().equals(emailUsuarioLogueado) && 
                !auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                log.warn("Intento de cerrar carrito no autorizado con ID {} por usuario {}", 
                    id, emailUsuarioLogueado);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para cerrar este carrito");
            }

            try {
                DTCarrito carritoCerrado = carritoService.desactivarCarrito(id);
                if (carritoCerrado != null) {
                    log.info("Carrito {} cerrado exitosamente para usuario {}", 
                        id, emailUsuarioLogueado);
                    return ResponseEntity.ok(carritoCerrado);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo cerrar el carrito");
            } catch (IllegalStateException e) {
                log.warn("Error al cerrar carrito {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
            } catch (Exception e) {
                log.error("Error inesperado al cerrar carrito {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("No tienes permiso para realizar esta acción");
    }

    @GetMapping("/me")
    public ResponseEntity<DTCarrito> obtenerCarritoUsuarioLogueado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String emailUsuarioLogueado = auth.getName();
            try {
                DTCarrito carrito = carritoService.getCarritoByUsuarioEmail(emailUsuarioLogueado);
                if (carrito != null) {
                    log.info("Carrito existente encontrado para usuario: {}", emailUsuarioLogueado);
                    return new ResponseEntity<>(carrito, HttpStatus.OK);
                } else {
                    // Si no se encuentra un carrito activo, crear uno nuevo
                    log.info("No active cart found for user {}. Creating a new one.", emailUsuarioLogueado);
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuarioRepository.findByEmail(emailUsuarioLogueado)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"))); // Asegurarse de que el usuario existe

                    DTCarrito carritoCreado = carritoService.createCarrito(nuevoCarrito);

                    log.info("New cart created for user {}: {}", emailUsuarioLogueado, carritoCreado.getId());
                    return new ResponseEntity<>(carritoCreado, HttpStatus.CREATED); // Devolver el carrito recién creado con 201
                }
            } catch (Exception e) {
                log.error("Error al obtener o crear carrito para el usuario {}: {}", emailUsuarioLogueado, e.getMessage(), e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.warn("Acceso no autorizado para obtener el carrito.");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTCarrito> obtenerCarrito(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String emailUsuarioLogueado = auth.getName();
            try {
                DTCarrito carrito = carritoService.getCarritoById(id);
                if (carrito != null) {
                    if (!carrito.getUsuario().equals(emailUsuarioLogueado) && !auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                        log.warn("Intento de acceso no autorizado al carrito con ID {} por usuario {}", id, emailUsuarioLogueado);
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }
                    return new ResponseEntity<>(carrito, HttpStatus.OK);
                }
                log.warn("Carrito no encontrado con ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                log.error("Error al obtener carrito con ID {}: {}", id, e.getMessage(), e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.warn("Acceso no autorizado al carrito con ID: {}", id);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<Float> obtenerTotalCarrito(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String emailUsuarioLogueado = auth.getName();
            DTCarrito carrito = carritoService.getCarritoById(id);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!carrito.getUsuario().equals(emailUsuarioLogueado) && !auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                 log.warn("Intento de obtener total de carrito no autorizado con ID {} por usuario {}", id, emailUsuarioLogueado);
                 return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            Float total = carritoService.getMontoTotalCarrito(id);
            return new ResponseEntity<>(total, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
