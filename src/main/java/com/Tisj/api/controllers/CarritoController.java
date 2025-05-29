package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Carrito;
import com.Tisj.bussines.entities.DT.DTCarrito;
import com.Tisj.api.requests.RequestCarrito;
import com.Tisj.services.CarritoService;
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

    @PostMapping
    public ResponseEntity<DTCarrito> crearCarrito(@Valid @RequestBody(required = false) RequestCarrito request) {
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
                if (request != null && request.getItemIds() != null) {
                    request.getItemIds().forEach(itemId -> {
                        try {
                            carritoService.agregarItemAlCarrito(nuevoCarrito.getId(), itemId);
                        } catch (Exception e) {
                            log.error("Error al agregar item {} al carrito: {}", itemId, e.getMessage());
                        }
                    });
                }
                
                DTCarrito carritoCreado = carritoService.createCarrito(nuevoCarrito);
                log.info("Nuevo carrito creado para usuario: {}", email);
                return ResponseEntity.status(HttpStatus.CREATED).body(carritoCreado);
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
                    carrito.getItems().forEach(itemId -> {
                        try {
                            carritoService.quitarItemDelCarrito(id, itemId);
                        } catch (Exception e) {
                            log.error("Error al quitar item {} del carrito: {}", itemId, e.getMessage());
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
    public ResponseEntity<DTCarrito> cerrarCarrito(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
             String emailUsuarioLogueado = auth.getName();
            DTCarrito carrito = carritoService.getCarritoById(id);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!carrito.getUsuario().equals(emailUsuarioLogueado) && !auth.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
                 log.warn("Intento de cerrar carrito no autorizado con ID {} por usuario {}", id, emailUsuarioLogueado);
                 return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            DTCarrito carritoCerrado = carritoService.desactivarCarrito(id);
            if (carritoCerrado != null) {
                return new ResponseEntity<>(carritoCerrado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
                    return new ResponseEntity<>(carrito, HttpStatus.OK);
                }
                log.warn("Carrito no encontrado para el usuario: {}", emailUsuarioLogueado);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                log.error("Error al obtener carrito para el usuario {}: {}", emailUsuarioLogueado, e.getMessage(), e);
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
