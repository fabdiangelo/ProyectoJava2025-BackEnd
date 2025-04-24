package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Carrito;
import com.Tisj.services.CarritoService;
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
@RequestMapping("/api/carrito")
public class CarritoController {

   @Autowired
   private CarritoService carritoService;

   @GetMapping
   public ResponseEntity<List<Carrito>> getCarritos() {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
       return new ResponseEntity<>(carritoService.getAllCarritos(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
   }

   @GetMapping("/{id}")
   public ResponseEntity<Carrito> getCarrito(@PathVariable Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getAuthorities().stream()
            .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
       return buildResponse(carritoService.getCarritoById(id));
    } else {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
   }

   @PostMapping
   public ResponseEntity<Carrito> createCarrito(@RequestBody Carrito carrito) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getAuthorities().stream()
            .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
       return new ResponseEntity<>(carritoService.createCarrito(carrito), HttpStatus.CREATED);
    } else {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
   }

//    @PutMapping("/{id}")
//    public ResponseEntity<Carrito> updateCarrito(@PathVariable Long id, @RequestBody Carrito carrito) {
//     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//     if (auth.getAuthorities().stream()
//             .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
//        return buildResponse(carritoService.updateCarrito(id, carrito));
//     } else {
//         return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//    }
       
//    }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteCarrito(@PathVariable Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getAuthorities().stream()
            .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
       carritoService.deleteCarrito(id);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
   }

   private <T> ResponseEntity<T> buildResponse(T body) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getAuthorities().stream()
            .anyMatch(p -> p.getAuthority().equals("ADMIN"))) {
       return body != null ? new ResponseEntity<>(body, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
