package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Carrito;
import com.Tisj.services.CarritoService;
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
    public ResponseEntity<Carrito> crearCarrito() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            String email = auth.getName();
            
            Carrito carritoExistente = carritoService.getCarritoByUsuarioEmail(email);
            if (carritoExistente != null) {
                return ResponseEntity.ok(carritoExistente);
            }

            Carrito nuevoCarrito = new Carrito();
            Carrito carritoCreado = carritoService.createCarrito(nuevoCarrito);
            return ResponseEntity.status(HttpStatus.CREATED).body(carritoCreado);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarrito(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            Carrito carrito = carritoService.getCarritoById(id);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            carritoService.deleteCarrito(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/{carritoId}/items/{articuloId}")
    public ResponseEntity<Carrito> agregarArticulo(
            @PathVariable Long carritoId,
            @PathVariable Long articuloId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            Carrito carrito = carritoService.getCarritoById(carritoId);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Carrito carritoActualizado = carritoService.agregarItemAlCarrito(carritoId, articuloId);
            if (carritoActualizado != null) {
                return new ResponseEntity<>(carritoActualizado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{carritoId}/items/{articuloId}")
    public ResponseEntity<Carrito> quitarArticulo(
            @PathVariable Long carritoId,
            @PathVariable Long articuloId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            Carrito carrito = carritoService.getCarritoById(carritoId);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Carrito carritoActualizado = carritoService.quitarItemDelCarrito(carritoId, articuloId);
            if (carritoActualizado != null) {
                return new ResponseEntity<>(carritoActualizado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<Carrito> cerrarCarrito(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            Carrito carrito = carritoService.getCarritoById(id);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Carrito carritoCerrado = carritoService.desactivarCarrito(id);
            if (carritoCerrado != null) {
                return new ResponseEntity<>(carritoCerrado, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("USER") || p.getAuthority().equals("ADMIN"))) {
            try {
                Carrito carrito = carritoService.getCarritoById(id);
                if (carrito != null) {
                    return new ResponseEntity<>(carrito, HttpStatus.OK);
                }
                log.warn("Carrito no encontrado con ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                log.error("Error al obtener carrito con ID {}: {}", id, e.getMessage());
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
            Carrito carrito = carritoService.getCarritoById(id);
            if (carrito == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Float total = carritoService.getMontoTotalCarrito(id);
            return new ResponseEntity<>(total, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
