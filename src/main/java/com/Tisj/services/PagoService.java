package com.Tisj.services;

import com.Tisj.bussines.entities.Pago;
import com.Tisj.bussines.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> getAllPagos() {
        return pagoRepository.findAll(); // Obtener todos los pagos
    }

    public Pago getPagoById(Long id) {
        return pagoRepository.findById(id).orElse(null); // Obtener un pago por su ID
    }

    public Pago createPago(Pago pago) {
        return pagoRepository.save(pago); // Crear un nuevo pago
    }

    public Pago updatePago(Long id, Pago pago) {
        if (pagoRepository.existsById(id)) {
            pago.setId(id);
            return pagoRepository.save(pago); // Actualizar un pago existente
        }
        return null;
    }

    public void deletePago(Long id) {
        if (pagoRepository.existsById(id)) {
            pagoRepository.deleteById(id); // Eliminar un pago por su ID
        }
    }

    public List<Pago> getPagosByUsuarioEmail(String usuarioEmail) {
        return pagoRepository.findByUsuarioEmail(usuarioEmail); // Obtener pagos por email de usuario
    }
}