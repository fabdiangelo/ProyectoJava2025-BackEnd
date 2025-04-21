package com.Tisj.api.controllers;

import com.Tisj.bussines.entities.Pago;
import com.Tisj.services.PagoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pagos")
@PreAuthorize("hasAuthority('ADMIN')")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> getPagos() {
        List<Pago> pagos = pagoService.getAllPagos();
        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getPago(@PathVariable Long id) {
        Pago pago = pagoService.getPagoById(id);
        if (pago != null) {
            return new ResponseEntity<>(pago, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Pago> createPago(@RequestBody Pago pago) {
        Pago nuevoPago = pagoService.createPago(pago);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }

    //TESTING Mapping
    @PutMapping("/{id}")
    public ResponseEntity<Pago> updatePago(@PathVariable Long id, @RequestBody Pago pago) {
        Pago pagoActualizado = pagoService.updatePago(id, pago);
        if (pagoActualizado != null) {
            return new ResponseEntity<>(pagoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Long id) {
        pagoService.deletePago(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
