package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Carrito;
import com.Tisj.services.CarritoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/me")
    public ResponseEntity<Carrito> getMyCarrito() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Assuming email is the username
        Carrito carrito = carritoService.getCarritoByUsuarioEmail(email);
        if (carrito != null) {
            return new ResponseEntity<>(carrito, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")

    @GetMapping
    public ResponseEntity<List<Carrito>> getCarritos() {
        List<Carrito> carritos = carritoService.getAllCarritos();
        return new ResponseEntity<>(carritos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> getCarrito(@PathVariable Long id) {
        Carrito carrito = carritoService.getCarritoById(id);
        if (carrito != null) {
            return new ResponseEntity<>(carrito, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Carrito> createCarrito(@RequestBody Carrito carrito) {
        Carrito nuevoCarrito = carritoService.createCarrito(carrito);
        return new ResponseEntity<>(nuevoCarrito, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carrito> updateCarrito(@PathVariable Long id, @RequestBody Carrito carrito) {
        Carrito carritoActualizado = carritoService.updateCarrito(id, carrito);
        if (carritoActualizado != null) {
            return new ResponseEntity<>(carritoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarrito(@PathVariable Long id) {
        carritoService.deleteCarrito(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
